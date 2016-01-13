package de.tud.nlp4web.project.evaluation.provider.api;

import javax.ejb.Local;

import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionEvaluationResult;

@Local
public interface EvaluationServiceLocal {

	
	/**
	 * Creates a new {@link EvaluationSession} in the database.<br />
	 * The server will return a session that contains the question collections that are mostly needed to be answered next
	 * @return a new EvaluationSession 
	 */
	public EvaluationSession createNewSession() throws ServiceException;
	
	/**
	 * Loads the session state that fits to the given restart key
	 * @param resumeKey restart key
	 * @return session state
	 * @throws ServiceException
	 */
	public EvaluationSession recreateSession(String resumeKey) throws ServiceException;
	
	/**
	 * Stores the answer chosen by the client to the database
	 * @param resumeKey key identifying the session (so called resume key)
	 * @param question the question that has been answers 
	 * @param result the evaluation result
	 * @return the new state of the session
	 * @throws ServiceException if an exception occurs
	 */
	public EvaluationSession answerQuestion(String resumeKey, Question question, QuestionEvaluationResult result) throws ServiceException;
	
	
}