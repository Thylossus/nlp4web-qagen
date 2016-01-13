package de.tud.nlp4web.project.evaluation.web.evaluation;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import de.tud.nlp4web.project.evaluation.provider.api.EvaluationServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.web.FacesContextHelper;
import de.tud.nlp4web.project.evaluation.web.Outcome;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@ViewScoped
@ManagedBean
@Log4j
public class ResumeBean implements Serializable {

	private static final long serialVersionUID = 7170715296815583917L;

	@Getter @Setter @ManagedProperty(value = "#{evaluationBean}")
	private EvaluationBean evaluationBean;

	@EJB
	private EvaluationServiceLocal evaluationService;
	
	@Getter
	private EvaluationSession session;
	
	public void preRenderView(String urlKey) {
		if (session == null) {
			log.debug("Incoming resume request with key " + urlKey);
			if (urlKey != null && urlKey.trim().length() == 32) {
				urlKey = urlKey.trim();
				try {
					session = evaluationService.recreateSession(urlKey);
				} catch (ServiceException ex) {
					log.warn("Could not restore session for key " + urlKey, ex);
				}
			}
		}
	}
	
	public String getRemainingTimeString() {
		int min = Math.max(session.getEstimatedRemainingTime() / 60000, 1);
		return min + " " + (min != 1 ? "minutes" : "minute");
	}
	
	public boolean isValidSession() {
		return session != null;
	}
	
	public boolean isClosed() {
		return session != null && session.isClosed();
	}
	
	public String resume() {
		if (session != null) {
			evaluationBean.initializeWithSession(session);
			if (!isClosed()) {
				return Outcome.NEXT_QUESTION;
			} else {
				return Outcome.SHOW_RESULT;
			}
		}
		FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured. Please try again.");
		return null;
	}
	
}
