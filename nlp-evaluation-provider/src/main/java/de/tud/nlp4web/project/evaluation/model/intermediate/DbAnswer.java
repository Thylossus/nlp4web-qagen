package de.tud.nlp4web.project.evaluation.model.intermediate;

import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an answer that is stored in the database
 */
public class DbAnswer {

	@Getter @Setter
	private String answertext;

	@Getter @Setter
	private int id;

	@Getter @Setter
	private boolean correct;
	
	public Answer convertToModel() {
		Answer answer = new Answer();
		answer.setId(id);
		answer.setText(answertext);
		return answer;
	}
}
