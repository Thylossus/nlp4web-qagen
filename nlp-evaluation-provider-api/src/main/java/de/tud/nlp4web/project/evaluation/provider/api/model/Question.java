package de.tud.nlp4web.project.evaluation.provider.api.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines a question that should be evaluated
 *
 */
@EqualsAndHashCode(of = "id")
public class Question implements Serializable {

	private static final long serialVersionUID = 6699517437709485184L;

	/** Text of the question */
	@Getter @Setter
	private String text;
	
	/** The comment */
	@Getter @Setter
	private String comment;
	
	/** Internal ID of the question */
	@Getter @Setter
	private int id;
	
	/** The list of possible answers to this question */
	private List<Answer> answers;
	
	/** Correct answer for this question. Has to be an element of {@link #answers}. */
	@Getter
	private Answer correctAnswer;
	
	/** ID of the question set */
	@Getter @Setter
	private int questionSetID;
	
	public Question() {
		super();
		answers = new LinkedList<>();
	}
	
	public void addAnswer(Answer answer) {
		if(!answers.contains(answer)) {
			answers.add(answer);
		}
	}
	
	public void setCorrectAnswer(Answer answer) {
		if (!answers.contains(answer)) {
			answers.add(answer);
		}
		correctAnswer = answer;
	}
	
	public List<Answer> getAnswers() {
		return Collections.unmodifiableList(answers);
	}
	
	public void shuffleAnswers() {
		Collections.shuffle(answers);
	}
	
}
