package de.tud.nlp4web.project.evaluation.provider.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import de.tud.nlp4web.project.evaluation.common.impl.AbstractDatabaseBean;
import de.tud.nlp4web.project.evaluation.model.intermediate.QuestionSetStats;
import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionEvaluationResult;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import de.tud.nlp4web.project.evaluation.provider.api.model.settings.SettingsKey;
import lombok.extern.log4j.Log4j;

@RequestScoped
@Log4j
public class EvaluationSessionManager extends AbstractDatabaseBean {

	@Inject
	private SettingsManager settingsMgnr;
	
	@Inject
	private QuestionSetManager questionSetMgnr;
	
	public EvaluationSession createNewSession() throws IOException {
		
		EvaluationSession evalSession = new EvaluationSession();
		
		int remainingSets = settingsMgnr.getSetting(SettingsKey.EVALUATION_SESSION_SETS_AMOUNT).getIntValue();
		int evaluationsPerDb = settingsMgnr.getSetting(SettingsKey.EVALUATION_EVALS_PER_DB).getIntValue();
		List<QuestionSet> questionSets = new LinkedList<>();
		
		// Add individual baseline
		QuestionSet qsIb = questionSetMgnr.getIndividualBaseline();
		if (qsIb.isLocked()) {
			questionSets.add(qsIb);
			remainingSets -= 1;
		} else {
			log.warn("Creating an evaluation session without IB due to the fact that it is not locked");
		}
			
		// Add difficulty baselines as long as there are some that have to be evaluated
		List<QuestionSet> dbQuestionSets = questionSetMgnr.getAllQuestionSets(QuestionSetType.DB);
		List<QuestionSetStats> dbQuestionSetStats = new LinkedList<>();
		for (QuestionSet qs : dbQuestionSets) {
			dbQuestionSetStats.add(questionSetMgnr.getStats(qs));
		}
		
		// Sort the sets by the amount of complete evaluations to prefer seldomly-evaluated sets
		Collections.sort(dbQuestionSetStats, new Comparator<QuestionSetStats>() {
			@Override
			public int compare(QuestionSetStats qs1, QuestionSetStats qs2) {
				return qs1.getEvaluationCount() - qs2.getEvaluationCount();
			}
		});
		
		// Now add the sets to the session...
		while(remainingSets > 0 && dbQuestionSetStats.size() > 0) {
			QuestionSetStats stats = dbQuestionSetStats.get(0);
			QuestionSet nextSet = stats.getQuestionSet();
			if (nextSet.isLocked()) {
				if (stats.getEvaluationCount() < evaluationsPerDb) { // if further evaluation is needed
					questionSets.add(nextSet);
					remainingSets -= 1;
				}
			}
			dbQuestionSetStats.remove(0);
		}
		
		// Finally, add ES sets to the evaluation session
		// Find the ES Sets with the minimum evaluation count. Fetch all stats and merge them into a list ordered by evaluation count
		List<QuestionSetStats> esQuestionSetStats = new LinkedList<>();
		for (QuestionSet qs : questionSetMgnr.getAllQuestionSets(QuestionSetType.ES)) {
			if (qs.isLocked()) {
				QuestionSetStats stats = questionSetMgnr.getStats(qs);
				int i = 0;
				while(i<esQuestionSetStats.size() &&
						esQuestionSetStats.get(i).getEvaluationCount() <= stats.getEvaluationCount() ) {
					i+=1;
				}
				esQuestionSetStats.add(i, stats);
			}
		}
		
		for(int i = 0; i < esQuestionSetStats.size() && remainingSets > 0; i++) {
			QuestionSetStats stats = esQuestionSetStats.get(i);
			// Caution: It is important that the questions selected here haven't already been added as DB above
			boolean found = false;
			for(QuestionSet qs : questionSets) {
				found |= qs.getId() == stats.getQuestionSet().getId();
			}
			if (!found) {
				remainingSets -= 1;
				questionSets.add(stats.getQuestionSet());
			}
		}
		
		evalSession.setQuestionSets(questionSets);
		evalSession.setResumeKey(generateResumeKey());
		
		try (
			PreparedStatement stmtInsertSession = getDbConnection().prepareStatement(
					"INSERT INTO eval_session(start, resume_key) values(CURRENT_TIMESTAMP, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			PreparedStatement stmtInsertSessionSet = getDbConnection().prepareStatement(
					"INSERT INTO eval_session_sets(questionset_id, eval_session_id, evaluate_as) values(?,?,?)");
		) {
			stmtInsertSession.setString(1, evalSession.getResumeKey());
			stmtInsertSession.executeUpdate();
			try (ResultSet genKeyRs = stmtInsertSession.getGeneratedKeys()) {
				if (!genKeyRs.next()) {
					throw new IOException("Could not fetch auto increment id for evaluation session during creation");
				}
				evalSession.setId(genKeyRs.getInt(1));
				for(QuestionSet questionSet : questionSets) {
					stmtInsertSessionSet.setInt(1, questionSet.getId());
					stmtInsertSessionSet.setInt(2, evalSession.getId());
					stmtInsertSessionSet.setString(3, questionSet.getType() == QuestionSetType.IB ? null : questionSet.getType().toString());
					stmtInsertSessionSet.executeUpdate();
				}
			}
		} catch (SQLException ex) {
			throw new IOException("Could not store new session.", ex);
		}

		if (evalSession.getQuestionSets().isEmpty()) {
			String msg = "Created a session with no question sets because no sets have been locked. Rolling back...";
			log.error(msg);
			throw new IOException(msg);
		} else if (remainingSets > 0) {
			log.warn("The new session with ID " + evalSession.getId() + " contains too less question sets (missing " + remainingSets + " due to too less locked ES sets)");
		}
		
		return evalSession;
		
	}
	
	/** Generates a resume key (32-char-String of a specific character set) */
	private String generateResumeKey() {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		StringBuilder resumeKey = new StringBuilder();
		for(int i = 0; i < 32; i++) resumeKey.append(chars[((int)(Math.random()*chars.length))]);
		return resumeKey.toString();
	}

	
	public EvaluationSession getSessionByKey(String resumeKey) throws IOException {
		
		try (
			PreparedStatement stmt = getDbConnection().prepareStatement("SELECT"
				+ "  es.id as es_id, es.start, es.end, es.resume_key,"
				+ "  coalesce(ess.evaluate_as, 'IB') as evaluate_as,"
				+ "  qs.id as qs_id, qs.name as qs_name, qs.type as qs_type,"
				+ "  q.id as q_id, q.questiontext,"
				+ "  a.id as a_id, a.answertext, a.correct,"
				+ "  qe.chosen_answer_id, qe.needed_time"
				+ " FROM eval_session es"
				+ " LEFT JOIN eval_session_sets ess"
				+ "  ON (ess.eval_session_id = es.id)"
				+ " LEFT JOIN questionset qs"
				+ "  ON (qs.id = ess.questionset_id)"
				+ " LEFT JOIN question q"
				+ "  ON (q.questionset_id = qs.id)"
				+ " LEFT JOIN answer a"
				+ "  ON (a.question_id = q.id and (a.type = ess.evaluate_as or (a.type = 'IB' and ess.evaluate_as is null)))"
				+ " LEFT JOIN question_evaluation qe"
				+ "  ON (qe.eval_session_id = es.id AND qe.question_id = q.id and qe.chosen_answer_id = a.id)"
				+ " WHERE es.resume_key = ?");	
		) {
			stmt.setString(1, resumeKey);
			try (ResultSet rs = stmt.executeQuery()) {
				
				EvaluationSession session = null;
				while(rs.next()) {
					if (session == null) {
						// Create session (first run through the loop)
						session = new EvaluationSession();
						session.setId(rs.getInt("es_id"));
						session.setResumeKey(rs.getString("resume_key"));
						session.setStartDate(new Date(rs.getTimestamp("start", getDbCalendar()).getTime()));
						if (rs.getObject("end") != null) {
							session.setEndDate(new Date(rs.getTimestamp("end", getDbCalendar()).getTime()));
						}
						session.setQuestionSets(new LinkedList<QuestionSet>());
					}
					
					QuestionSet questionSet = null;
					if (rs.getObject("qs_id") != null) {
						questionSet = session.getQuestionSet(rs.getInt("qs_id"));
						if (questionSet == null) {
							// New Question Set
							questionSet = new QuestionSet();
							questionSet.setId(rs.getInt("qs_id"));
							questionSet.setName(rs.getString("qs_name"));
							questionSet.setType(QuestionSetType.valueOf(rs.getString("evaluate_as")));
							questionSet.setQuestions(new LinkedList<Question>());
							session.getQuestionSets().add(questionSet);
						}
					}
					
					if (questionSet != null) {
						// Look for question data
						
						Question question = null;
						if (rs.getObject("q_id") != null) {
							question = questionSet.getQuestion(rs.getInt("q_id"));
							if (question == null) {
								question = new Question();
								question.setId(rs.getInt("q_id"));
								question.setText(rs.getString("questiontext"));
								questionSet.getQuestions().add(question);
							}
						}
						
						if (question != null) {
							// Look for answer data
							
							if (rs.getObject("a_id") != null) {
								Answer answer = new Answer();
								answer.setId(rs.getInt("a_id"));
								answer.setText(rs.getString("answertext"));
								if (rs.getBoolean("correct") == true) {
									question.setCorrectAnswer(answer);
								} else {
									question.addAnswer(answer);
								}
								
								if (rs.getObject("chosen_answer_id") != null) {
									QuestionEvaluationResult evalResult = new QuestionEvaluationResult();
									evalResult.setChosenAnswer(answer);
									evalResult.setTimeToAnswer(rs.getInt("needed_time"));
									session.answerQuestion(question, evalResult);
								}
							}
						}
					}
					
				}
				
				return session;
			}
		} catch (SQLException ex) {
			throw new IOException("Error fetching evaluation session", ex); 
		}
		
	}
	
	public void storeEvaluationResult(EvaluationSession session, Question question, QuestionEvaluationResult result) throws IOException {
		try (PreparedStatement stmt = getDbConnection().prepareStatement("INSERT INTO question_evaluation(chosen_answer_id, eval_session_id, needed_time, question_id) VALUES (?,?,?,?)")) {
			
			stmt.setInt(1, result.getChosenAnswer().getId());
			stmt.setInt(2, session.getId());
			stmt.setInt(3, result.getTimeToAnswer());
			stmt.setInt(4, question.getId());
			
			stmt.executeUpdate();
			
		} catch (SQLException ex) {
			throw new IOException("Could not store evaluation result", ex);
		}
	}
	
	public void closeSession(EvaluationSession session) throws IOException {
		try (PreparedStatement stmt = getDbConnection().prepareStatement("UPDATE eval_session SET end = ? WHERE ID = ?")) {
			
			stmt.setTimestamp(1, new Timestamp(session.getEndDate().getTime()), getDbCalendar());
			stmt.setInt(2, session.getId());
			
			int updateCount = stmt.executeUpdate();
			if (updateCount != 1) {
				throw new IOException("Unexpected update count when closing an evaluation session (id=" + session.getId() + ", uc=" + updateCount + ")");
			}
			
		} catch (SQLException ex) {
			throw new IOException("Could not close the session", ex);
		}
	}
	
	public List<String> getAllResumeKeys() throws IOException {
		try (
				PreparedStatement stmt = getDbConnection().prepareStatement("SELECT resume_key FROM eval_session ORDER BY id ASC");
				ResultSet rs = stmt.executeQuery();
		) {
			List<String> resumeKeys = new LinkedList<>();
			while(rs.next()) {
				resumeKeys.add(rs.getString(1));
			}
			
			return resumeKeys;
		} catch(SQLException ex) {
			throw new IOException("Could not fetch resume keys", ex);
		}
	}
}
