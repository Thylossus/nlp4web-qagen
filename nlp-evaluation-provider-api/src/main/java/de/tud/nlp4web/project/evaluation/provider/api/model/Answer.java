package de.tud.nlp4web.project.evaluation.provider.api.model;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines a possible answer for a Question
 *
 */
@EqualsAndHashCode(of = "id")
public class Answer implements Serializable {

	private static final long serialVersionUID = -4068188112756781033L;

	/** The text for this answer */
	@Getter @Setter
	private String text;
	
	/** The internal id for this answer */
	@Getter @Setter
	private int id;
	
}
