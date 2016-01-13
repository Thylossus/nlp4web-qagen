package de.tud.nlp4web.project.evaluation.provider.api.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Contains the measurements of the evaluation of a specific question
 */
public class QuestionEvaluationResult implements Serializable {

	private static final long serialVersionUID = -998473169001581749L;

	/** The answer chosen by the participant */
	@Getter @Setter
	private Answer chosenAnswer;
	
	/** Time the participant needed to answer this question */
	@Getter @Setter
	private int timeToAnswer;
	
}
