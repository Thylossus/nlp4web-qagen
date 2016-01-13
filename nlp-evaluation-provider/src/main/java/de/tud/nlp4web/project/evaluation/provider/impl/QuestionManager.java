package de.tud.nlp4web.project.evaluation.provider.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;

import de.tud.nlp4web.project.evaluation.common.impl.AbstractDatabaseBean;
import de.tud.nlp4web.project.evaluation.model.intermediate.DbAnswer;
import de.tud.nlp4web.project.evaluation.model.intermediate.DbQuestion;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;

@RequestScoped
public class QuestionManager extends AbstractDatabaseBean {

	public DbQuestion getQuestion(int id) throws IOException {
		
		try (PreparedStatement stmt = getDbConnection().prepareStatement(
				"SELECT q.questiontext, q.comment, q.questionset_id, a.id, a.answertext, a.type, a.correct FROM question q "
				+ "LEFT JOIN answer a ON (a.question_id=q.id) WHERE q.id=?")) {
			
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				DbQuestion question = null;
				while(rs.next()) {
					if (question == null) {
						question = new DbQuestion();
						question.setId(id);
						question.setQuestiontext(rs.getString(1));
						question.setComment(rs.getString(2));
						question.setQuestionSetID(rs.getInt(3));
					}
					String answertext = rs.getString(5);
					if (answertext != null) {
						DbAnswer answer = new DbAnswer();
						answer.setAnswertext(answertext);
						answer.setCorrect(rs.getBoolean(7));
						answer.setId(rs.getInt(4));
						try {
							QuestionSetType type = QuestionSetType.valueOf(rs.getString(6));
							question.getAnswers().get(type).add(answer);
						} catch (IllegalArgumentException ex) {
							throw new IOException("Unknown question set type for answer " + answer.getId() + ": " + rs.getString(5));
						}
					}
				}
				return question;
			}
			
		} catch (SQLException ex) {
			throw new IOException("Could not fetch question with id " + id, ex);
		}
		
	}
	
	public int addQuestion(int questionSetId) throws IOException {
		try (PreparedStatement stmt = getDbConnection().prepareStatement("INSERT INTO question(questiontext, comment, questionset_id)"
				+ " VALUES('New question', '', ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
			
			stmt.setInt(1, questionSetId);
			stmt.executeUpdate();
			
			try (ResultSet genKeyRs = stmt.getGeneratedKeys()) {
				if (genKeyRs.next()) {
					return genKeyRs.getInt(1);
				} else {
					throw new IOException("Got no identifier for the new question");
				}
			}
			
		} catch (SQLException ex) {
			throw new IOException("Could not create question for question set with id " + questionSetId, ex);
		}
	}
	
	public void updateMetadata(int questionId, String text, String comment) throws IOException {
		try (PreparedStatement stmt = getDbConnection().prepareStatement("UPDATE question SET questiontext = ?, comment = ? WHERE ID = ?")) {
			
			stmt.setString(1, text != null ? text : "");
			stmt.setString(2, comment != null ? comment : "");
			stmt.setInt(3, questionId);
			int updateCount = stmt.executeUpdate();
			if (updateCount != 1) {
				throw new IOException("Got wrong update count when updating question metadata. Expected 1, got " + updateCount + " for id " + questionId);
			}
			
		} catch (SQLException ex) {
			throw new IOException("Could not update question metadata for id " + questionId, ex);
		}
	}
	
}
