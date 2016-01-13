package de.tud.nlp4web.project.evaluation.model.intermediate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import lombok.Getter;
import lombok.Setter;

/**
 *  represents a question that is stored in the database
 */
public class DbQuestion {

	@Getter @Setter
	private int id;

	@Getter @Setter
	private String questiontext;
	
	@Getter @Setter
	private String comment;

	@Getter @Setter
	private Map<QuestionSetType, List<DbAnswer>> answers;
	
	@Getter @Setter
	private int questionSetID;
	
	public DbQuestion() {
		answers = new HashMap<>();
		answers.put(QuestionSetType.DB, new LinkedList<DbAnswer>());
		answers.put(QuestionSetType.IB, new LinkedList<DbAnswer>());
		answers.put(QuestionSetType.ES, new LinkedList<DbAnswer>());
	}
	
	/** Returns all QuestionSetTypes for that at least one answer is defined */
	public Set<QuestionSetType> getTypes() {
		Set<QuestionSetType> types = new HashSet<>();
		for(QuestionSetType type : QuestionSetType.values()) {
			if (!answers.get(type).isEmpty()) {
				types.add(type);
			}
		}
		return types;
	}
	
	public Question convertToModel(QuestionSetType answerSet) {
		Question question = new Question();
		question.setId(id);
		question.setText(questiontext);
		question.setComment(comment);
		question.setQuestionSetID(questionSetID);
		LinkedList<DbAnswer> setAnswers = new LinkedList<>(answers.get(answerSet));
		for(DbAnswer answer : setAnswers) {
			if (answer.isCorrect()) {
				question.setCorrectAnswer(answer.convertToModel());
			} else {
				question.addAnswer(answer.convertToModel());
			}
		}
		return question;
	}
	
	public Answer getAnswer(int id) {
		for(QuestionSetType type : answers.keySet()) {
			List<DbAnswer> typeAnswers = answers.get(type);
			for(DbAnswer dbAnswer : typeAnswers) {
				if (dbAnswer.getId() == id) {
					return dbAnswer.convertToModel();
				}
			}
		}
		return null;
	}
}