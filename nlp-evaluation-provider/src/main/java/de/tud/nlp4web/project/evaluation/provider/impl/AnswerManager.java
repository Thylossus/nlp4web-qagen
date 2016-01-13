package de.tud.nlp4web.project.evaluation.provider.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;

import de.tud.nlp4web.project.evaluation.common.impl.AbstractDatabaseBean;
import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;

@RequestScoped
public class AnswerManager extends AbstractDatabaseBean {

	public void addAnswer(Answer answer, int questionId, QuestionSetType type, boolean correct) throws IOException {
		try (PreparedStatement stmt = getDbConnection().prepareStatement("INSERT INTO answer(type,answertext,question_id,correct) VALUES(?,?,?,?)")) {
			stmt.setString(1, type.toString());
			stmt.setString(2, answer.getText());
			stmt.setInt(3, questionId);
			stmt.setBoolean(4, correct);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			throw new IOException("Could not create answer", ex);
		}
	}
	
	public void updateAnswer(Answer answer) throws IOException {
		try (PreparedStatement stmt = getDbConnection().prepareStatement("UPDATE answer SET answertext = ? WHERE id = ?")) {
			stmt.setString(1, answer.getText());
			stmt.setInt(2, answer.getId());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			throw new IOException("Could not update answer " + answer.getId(), ex);
		}
	}
}
