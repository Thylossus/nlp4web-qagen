package de.tud.nlp4web.project.evaluation.web.admin;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.tud.nlp4web.project.evaluation.provider.api.AdminQuestionSetServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import de.tud.nlp4web.project.evaluation.web.FacesContextHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@ManagedBean
@ViewScoped
@Log4j
public class QuestionSetBean implements Serializable {
	
	private static final long serialVersionUID = 2603932297720818116L;

	@EJB
	private AdminQuestionSetServiceLocal qsService; 
	
	@Getter @Setter
	private List<QuestionSet> questionSets;
	
	
	@PostConstruct
	public void postConstruct() {
		try {
			questionSets = qsService.getQuestionSetOverview();
		} catch (ServiceException ex) {
			log.error("Cannot load question set overview", ex);
			throw new RuntimeException(ex);
		}
		Collections.sort(questionSets, QUESTION_SET_COMPARATOR);
	}
	
	public String lock(int id, String type) {
		try {
			QuestionSetType enumType = QuestionSetType.valueOf(type);
			qsService.lock(id, enumType);
			for(QuestionSet qs : questionSets) {
				if (qs.getId() == id && qs.getType() == enumType) {
					qs.setLocked(true);
				}
			}
		} catch (Exception ex) {
			log.error("Error creating question set", ex);
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error while locking the question set");
		}
		return null;
	}
	
	public String addQuestionset() {

		try {
			List<QuestionSet> newQuestionSets = qsService.createQuestionSet();
			
			questionSets.addAll(newQuestionSets);
			Collections.sort(questionSets, QUESTION_SET_COMPARATOR);
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_INFO, "Question set created");
		} catch (ServiceException ex) {
			log.error("Error creating question set", ex);
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating question set");
		}
		return null;
	}
	

	private static final Comparator<QuestionSet> QUESTION_SET_COMPARATOR = new Comparator<QuestionSet>() {
		@Override
		public int compare(QuestionSet qs1, QuestionSet qs2) {
			if (qs1.getId() == qs2.getId()) {
				return qs1.getType().compareTo(qs2.getType());
			}
			return Integer.compare(qs1.getId(), qs2.getId());
		}
	};
}
