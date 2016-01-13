package de.tud.nlp4web.project.evaluation.provider.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import de.tud.nlp4web.project.evaluation.common.impl.AbstractDatabaseBean;
import de.tud.nlp4web.project.evaluation.model.intermediate.QuestionSetStats;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import de.tud.nlp4web.project.evaluation.provider.api.model.settings.SettingsKey;
import lombok.extern.log4j.Log4j;

@RequestScoped
@Log4j
public class QuestionSetManager extends AbstractDatabaseBean {

	@Inject
	private QuestionManager questionMgnr;

	@Inject
	private SettingsManager settingsMgnr;
	
	/**
	 * Fetches the one (and only) IB question set from the database
	 */
	public QuestionSet getIndividualBaseline() throws IOException {
		List<QuestionSet> ibSets = getAllQuestionSets(QuestionSetType.IB);
		if (ibSets.isEmpty()) throw new IOException("No IB Question Set found");
		if (ibSets.size()>1)  throw new IOException("Multiple IB Question Sets found");
		return ibSets.get(0);
	}

	/**
	 * Fetches a question set by id. By passing the expected type of the question set the correlating answers are chosen
	 * @param id id of the question set
	 * @param type type of the question set (IB, ES, DB)
	 * @return the questionset
	 * @throws IOException if a technical error occures or no / more than one set is found
	 */
	public QuestionSet getQuestionSet(int id, QuestionSetType type) throws IOException {
		try(
			PreparedStatement stmt = getDbConnection().prepareStatement("SELECT id, name, type, comment, locked FROM questionset WHERE id = ?");
		) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					QuestionSet qs = parseQuestionSetDataFromRS(rs, type);
					addQuestions(qs);
					if (rs.next()) throw new IOException("Multiple IB Question Sets found");
					return qs;
				} else {
					throw new IOException("No IB Question Set found");
				}
			}
		} catch (SQLException ex) {
			throw new IOException("Error fetching IB Question Set", ex);
		}
		
	}
	
	/**
	 *  Fetches all QuestionSets for a given type 
	 */
	public List<QuestionSet> getAllQuestionSets(QuestionSetType type) throws IOException {
		List<QuestionSet> result = new LinkedList<>();
		String typeString = type == QuestionSetType.IB ? "IB" : "DB/ES";
		try(
			PreparedStatement stmt = getDbConnection().prepareStatement("SELECT id, name, type, comment, locked FROM questionset WHERE type = ?");
		) {
			stmt.setString(1, typeString);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					QuestionSet qs = parseQuestionSetDataFromRS(rs, type);
					addQuestions(qs);
					result.add(qs);
				}
			}
		} catch (SQLException ex) {
			throw new IOException("Error fetching " + type.toString() + " question sets.", ex);
		}
		
		return result;
	}
	
	/**
	 * Parses the QuestionSet Data from a ResultSet that points to a row of the questionset table
	 * @param rs ResultSet
	 * @return QuestionSet object (still without concrete Questions)
	 * @throws SQLException Database Error
	 */
	private QuestionSet parseQuestionSetDataFromRS(ResultSet rs, QuestionSetType type) throws SQLException, IOException {
		QuestionSet questionset = new QuestionSet();

		questionset.setId(rs.getInt("id"));
		questionset.setName(rs.getString("name"));
		questionset.setComment(rs.getString("comment"));

		String dbType = rs.getString("type");
		if (dbType.equals("IB") && type == QuestionSetType.IB) {
			questionset.setType(QuestionSetType.IB);
		} else if (dbType.equals("DB/ES") && (type == QuestionSetType.ES || type == QuestionSetType.DB)) {
			questionset.setType(type);
		} else {
			throw new IllegalArgumentException("The question set type from db (" + dbType + ") did not match to the expected type (" + type + ") for "
					+ " question set with ID " + questionset.getId() + " (" + questionset.getName() + ")"); 
		}

		String locks = rs.getString("locked");
		if (locks != null && locks.contains(questionset.getType().toString())) {
			questionset.setLocked(true);
		} else {
			questionset.setLocked(false);
		}
		
		questionset.setQuestions(new LinkedList<Question>());
		
		return questionset;
	}
	
	/**
	 * Adds the questions and the questionset-specific answers to a question set
	 */
	private void addQuestions(QuestionSet qs) throws IOException {
		QuestionSetType answerType = qs.getType();
		List<Question> questionList = qs.getQuestions();
		try(PreparedStatement stmt = getDbConnection().prepareStatement("SELECT id FROM question WHERE questionset_id = ?")) {
			stmt.setInt(1, qs.getId());
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					int questionId = rs.getInt(1);
					questionList.add(questionMgnr.getQuestion(questionId).convertToModel(answerType));
				}
			}
		} catch (SQLException ex) {
			throw new IOException("Couldn't load questions", ex);
		}
	}

	/**
	 * Fetches statistical information about evaluation of a specific questionset
	 * @param nextSet the questionset
	 * @return statistical information
	 * @throws IOException
	 */
	public QuestionSetStats getStats(QuestionSet questionset) throws IOException {
		int timeoutLimit = settingsMgnr.getSetting(SettingsKey.EVALUATION_TIMEOUT_LIMIT).getIntValue();
		Timestamp timeoutReferenceTimestamp = new Timestamp(System.currentTimeMillis() - ((long)timeoutLimit) * 1000);
		
		QuestionSetStats stats = new QuestionSetStats();
		stats.setQuestionSet(questionset);
		try (PreparedStatement stmt = getDbConnection()
				.prepareStatement("SELECT es.start, es.end FROM eval_session es JOIN eval_session_sets ess ON (es.id=ess.eval_session_id)"
						+ " WHERE ess.questionset_id=? and ess.evaluate_as=?")) {
			stmt.setInt(1, questionset.getId());
			stmt.setString(2, questionset.getType() == QuestionSetType.IB ? null : questionset.getType().toString());
			try (ResultSet rs = stmt.executeQuery()) {
				int completeEvaluations = 0;
				int pendingEvaluations = 0;
				int timeoutEvaluations = 0;
				while(rs.next()) {
					if (rs.getTimestamp(2) == null) {
						if (rs.getTimestamp(1).before(timeoutReferenceTimestamp)) {
							timeoutEvaluations++;
						} else {
							pendingEvaluations++;
						}
					} else {
						completeEvaluations++;
					}
				}
				stats.setCompleteEvaluationCount(completeEvaluations);
				stats.setPendingEvaluationCount(pendingEvaluations);
				stats.setTimeoutEvaluationCount(timeoutEvaluations);
			}
		} catch (SQLException ex) {
			throw new IOException("Error gathering stats for question set " + questionset.getId() + " (" + questionset.getType()+ ")", ex);
		}
		return stats;
	}
	
	/**
	 * Updates the metadata (comment and name) of a question set. Only these fields are read from the model passed to this method
	 * @param qs the model with the name, comment and id field set
	 * @throws IOException if an error occurs
	 */
	public void changeMetadata(QuestionSet qs) throws IOException {
		try (PreparedStatement stmt = getDbConnection().prepareStatement("UPDATE questionset SET name=?, comment=? WHERE ID=?")) {
			stmt.setString(1, qs.getName());
			stmt.setString(2, qs.getComment());
			stmt.setInt(3, qs.getId());
			int updateCount = stmt.executeUpdate();
			if (updateCount == 0) {
				throw new IOException("The question set with ID " + qs.getId() + " could not be found, thus no metadata was updated");
			}
			log.info("Metadata updated for question set " + qs.getId());
		} catch (SQLException ex) {
			throw new IOException("Error updating metadata for question set with ID " + qs.getId(), ex);
		}
	}
	
	/**
	 * Creates a new ES/DB QuestionSet combination and returns both question sets
	 * @throws IOException
	 */
	public List<QuestionSet> createQuestionSet() throws IOException {
		
		try (PreparedStatement stmt = getDbConnection().prepareStatement(
				"INSERT INTO questionset(type, name, comment) VALUES('DB/ES','New question set','')", PreparedStatement.RETURN_GENERATED_KEYS)){
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				rs.next();
				int id = rs.getInt(1);
				
				List<QuestionSet> result = new LinkedList<>();
				result.add(getQuestionSet(id, QuestionSetType.DB));
				result.add(getQuestionSet(id, QuestionSetType.ES));
				return result;
			}
		} catch (SQLException ex) {
			throw new IOException("Could not create new question set.", ex);
		}
	}
	
	public void lock(QuestionSet questionSet) throws IOException {
		try (PreparedStatement stmt = getDbConnection().prepareStatement("UPDATE questionset SET locked=concat(coalesce(concat(locked,','),''), ?) WHERE ID=?")) {
			stmt.setString(1, questionSet.getType().toString());
			stmt.setInt(2, questionSet.getId());
			int updateCount = stmt.executeUpdate();
			if (updateCount != 1) {
				throw new IOException("Could not lock question set. Unexpected update count. expected 1, got " + updateCount);
			}
		} catch (SQLException e) {
			throw new IOException("Could not lock question set.", e);
		}
	}
}
