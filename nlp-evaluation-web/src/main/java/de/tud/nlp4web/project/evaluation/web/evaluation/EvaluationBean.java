package de.tud.nlp4web.project.evaluation.web.evaluation;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.tud.nlp4web.project.evaluation.provider.api.EvaluationServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionEvaluationResult;
import de.tud.nlp4web.project.evaluation.web.FacesContextHelper;
import de.tud.nlp4web.project.evaluation.web.Outcome;
import lombok.Getter;
import lombok.extern.log4j.Log4j;


@ManagedBean(name = "evaluationBean")
@SessionScoped
@Log4j
public class EvaluationBean implements Serializable {

	private static final long serialVersionUID = 1765207534633061351L;

	@Getter
	private EvaluationSession evaluationSession;
	
	@EJB
	private EvaluationServiceLocal evaluationService;
	
	public void initializeWithSession(EvaluationSession session) {
		evaluationSession = session;
	}
	
	public Question getNextQuestion() {
		List<Question> unansweredQuestions = evaluationSession.getUnansweredQuestions();
		if (unansweredQuestions.isEmpty()) {
			return null;
		} else {
			Question question = unansweredQuestions.get(0);
			question.shuffleAnswers();
			return question;
		}
	}
	
	public boolean hasNextQuestion() {
		return !evaluationSession.isClosed();
	}

	public void storeAnswer(Question question, QuestionEvaluationResult evalResult) {
		
		try {
			evaluationSession = evaluationService.answerQuestion(evaluationSession.getResumeKey(), question, evalResult);
		} catch (ServiceException ex) {
			log.error(ex);
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured. Please try again.");
		}
		
	}
	
	public String startEvaluation() {
		if (evaluationSession == null) {
			log.info("Initializing new Session...");
			try {
				evaluationSession = evaluationService.createNewSession();
			} catch (ServiceException ex) {
				FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured. Please try again.");
				log.error("Could not initialize EvaluationBean: " , ex);
				return null;
			}
			
			log.info("New Session initialized. Backend ID is " + evaluationSession.getId());
		} else {
			log.info("Resuming session " + evaluationSession.getId());
		}
		
		if (hasNextQuestion()) {
			return Outcome.NEXT_QUESTION;
		} else {
			return Outcome.SHOW_RESULT;
		}
	}
	
	public String resumeLater() {
		return Outcome.RESUME_LATER;
	}
	
	public String getResumeLink() {
		return "http://nlp.fhessel.de/evaluation/resume.xhtml?key=" + evaluationSession.getResumeKey();
	}
	
	public int getTotalQuestionCount() {
		return evaluationSession.getQuestionCount();
	}
	
	public int getAnsweredQuestionCount() {
		return evaluationSession.getQuestionCount() - evaluationSession.getRemainingQuestionCount();
	}
	
	public boolean isValidSession() {
		return evaluationSession != null;
	}
}
