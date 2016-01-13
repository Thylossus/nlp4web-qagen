package de.tud.nlp4web.project.evaluation.model.intermediate;

import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import lombok.Getter;
import lombok.Setter;

public class QuestionSetStats {

	@Getter @Setter
	private QuestionSet questionSet;
	
	@Getter @Setter
	private int pendingEvaluationCount;
	
	@Getter @Setter
	private int completeEvaluationCount;

	@Getter @Setter
	private int timeoutEvaluationCount;
	
	/** Retourns the amount of potential successful evaluations (thus, timeout evaluations aren't counted) */
	public int getEvaluationCount() {
		return pendingEvaluationCount + completeEvaluationCount;
	}
	
}
