package de.tud.nlp4web.project.evaluation.provider.api.model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * An Evaluation Session contains a collection of questions that is presented to a specific user.
 * <br />
 * This Session also stores the measured data, e.g. the answers chosen by the user, the time it took to choose them etc. 
 */
@EqualsAndHashCode(of = "id")
public class EvaluationSession {

	/** The internal id of this session that identifies it in the database */
	@Getter @Setter
	private int id;
	
	/** The question sets selected for this session */
	@Getter @Setter
	private List<QuestionSet> questionSets;
	
	/** This key can be used to restore an unfinished EvaluationSession from the database */
	@Getter @Setter
	private String resumeKey;
	
	@Getter @Setter
	private Date startDate;
	
	@Getter @Setter
	private Date endDate;
	
	/** Contains all questions that already have been answered */
	@Getter
	private Map<Question, QuestionEvaluationResult> answeredQuestions;
	
	public EvaluationSession() {
		answeredQuestions = new HashMap<>();
		questionSets = new LinkedList<>();
	}
	
	/**
	 * Returns all questions in this session, regardless whether they have been answered or not
	 */
	public List<Question> getAllQuestions() {
		List<Question> allQuestions = new LinkedList<>();
		for(QuestionSet qs : questionSets) {
			allQuestions.addAll(qs.getQuestions());
		}
		return allQuestions;
	}

	/**
	 * Returns all questions that aren't answered yet 
	 */
	public List<Question> getUnansweredQuestions() {
		List<Question> allQuestions = getAllQuestions();
		allQuestions.removeAll(answeredQuestions.keySet());
		return allQuestions;
	}
	
	/**
	 * Returns the total amount of questions in this session
	 */
	public int getQuestionCount() {
		return getAllQuestions().size();
	}
	
	/**
	 * Returns the amount of questions remaining in this session
     */
	public int getRemainingQuestionCount() {
		return getQuestionCount() - answeredQuestions.size();
	}
	
	/**
	 * Marks a question answered by passing the evaluation results for this specific question
	 * @param question the question that will be marked as answered
	 * @param evalResult the evaluation result for this question
	 */
	public void answerQuestion(Question question, QuestionEvaluationResult evalResult) {
		answeredQuestions.put(question, evalResult);
	}
	
	/**
	 * Returns the approximate time for the remaining question based on the time needed to answer the questions the user has seen already
	 * @return time in ms. if there are no answered questions, 30sec/question will be used to calculate this value
	 */
	public int getEstimatedRemainingTime() {
		int totalTime = 0;
		int totalAnsweredQuestions = 0;
		for(QuestionEvaluationResult evRes : answeredQuestions.values()) {
			totalTime += evRes.getTimeToAnswer();
			totalAnsweredQuestions += 1;
		}
		if (totalAnsweredQuestions == 0) {
			return getQuestionCount() * 30 *  1000;
		}
		// average time to answer * amount of questions remaining
		return (totalTime / totalAnsweredQuestions) * (getQuestionCount() - totalAnsweredQuestions);
	}
	
	public QuestionSet getQuestionSet(int id) {
		for(QuestionSet qs : questionSets) {
			if (qs.getId() == id) {
				return qs;
			}
		}
		return null;
	}
	
	public boolean isClosed() {
		return endDate != null;
	}
	
	public String getQuestionSetString() {
		StringBuilder builder = new StringBuilder();
		for(QuestionSet qs : questionSets) {
			if (builder.length() > 0) {
				builder.append(", ");
			}
			if (qs.getType() == QuestionSetType.IB) {
				builder.append(QuestionSetType.IB.toString());
			} else {
				builder.append(qs.getType().toString()).append("[").append(qs.getId()).append("]");
			}
		}
		return builder.toString();
	}
	
}
