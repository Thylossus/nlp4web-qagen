package de.tud.nlp4web.project.evaluation.provider.api.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = {"id", "type"})
public class QuestionSet implements Serializable {

	private static final long serialVersionUID = -3519531665154479432L;

	@Getter @Setter
	private int id;
	
	@Getter @Setter
	private List<Question> questions;
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String comment;
	
	@Getter @Setter
	private QuestionSetType type;
	
	@Getter @Setter
	private boolean locked;
	
	public QuestionSet() {
		questions = new LinkedList<>();
	}
	
	public static enum QuestionSetType {
		/** Individual Baseline */ IB,
		/** Difficulty Baseline */ DB,
		/** Evaluation Set */      ES;
	}
	
	/**
	 * Returns the amount of questions defined in this set (see also {@link #getAnsweredQuestionCount()}) 
	 */
	public int getQuestionCount() {
		return questions.size();
	}
	
	/**
	 * Returns the amount of questions with at least to answers of which one is marked as being the correct one
	 */
	public int getAnsweredQuestionCount() {
		int count = 0;
		for(Question question : questions) {
			count += (question.getAnswers().size() > 1 && question.getCorrectAnswer() != null) ? 1 : 0;
		}
		return count;
	}
	
	public Question getQuestion(int id) {
		for(Question q : questions) {
			if(q.getId() == id) {
				return q;
			}
		}
		return null;
	}
}
