package de.tud.nlp4web.project.evaluation.provider;

import java.io.IOException;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import de.tud.nlp4web.project.evaluation.provider.api.EvaluationServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionEvaluationResult;
import de.tud.nlp4web.project.evaluation.provider.impl.EvaluationSessionManager;
import lombok.extern.log4j.Log4j;

@Stateless
@Log4j
public class EvaluationService implements EvaluationServiceLocal {

	@Inject
	private EvaluationSessionManager evalSessionMngr;
	
	@Override
	public EvaluationSession createNewSession() throws ServiceException {
		try {
			return evalSessionMngr.createNewSession();
		} catch (IOException ex) {
			String msg = "Could not create a new evaluation session";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}

	@Override
	public EvaluationSession recreateSession(String resumeKey) throws ServiceException {
		try {
			return evalSessionMngr.getSessionByKey(resumeKey);
		} catch (IOException ex) {
			String msg = "Could not recreate a new evaluation session";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}

	@Override
	public EvaluationSession answerQuestion(String resumeKey, Question question, QuestionEvaluationResult result)
			throws ServiceException {
		try {
			EvaluationSession session = evalSessionMngr.getSessionByKey(resumeKey);
			
			if (session == null) {
				throw new ServiceException("Could not find resumeKey " + resumeKey);
			}
			if (!session.isClosed()) {
				
				if (session.getUnansweredQuestions().contains(question)) {
					
					// Store the evaluation result
					session.answerQuestion(question, result);
					evalSessionMngr.storeEvaluationResult(session, question, result);
					
					if (session.getRemainingQuestionCount() == 0) {
						// Close the session
						Date endDate = new Date();
						session.setEndDate(endDate);
						evalSessionMngr.closeSession(session);
					}
					
				} else {
					throw new ServiceException("This question has already been answered");
				}
				
			} else {
				throw new ServiceException("The session is already closed");
			}
			
			return session;
		} catch (IOException ex) {
			String msg = "Could not recreate a new evaluation session";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}
}
