package de.tud.nlp4web.project.evaluation.web.admin;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.tud.nlp4web.project.evaluation.provider.api.AdminStatisticalServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@ManagedBean
@ViewScoped
@Log4j
public class StatisticsBean implements Serializable {
	
	private static final long serialVersionUID = 2603932297720818116L;

	@EJB
	private AdminStatisticalServiceLocal statService; 
	
	@Getter
	private List<EvaluationSession> sessions;
	
	@Getter
	private List<QuestionSetInformation> questionSetInformation;
	
	@PostConstruct
	public void postConstruct() {
		try {
			sessions = statService.getAllSessions();
		} catch (ServiceException ex) {
			log.error("Cannot load statistics", ex);
			throw new RuntimeException(ex);
		}
		
		questionSetInformation = new LinkedList<>();
		for(EvaluationSession session : sessions) {
			for(QuestionSet questionSet : session.getQuestionSets()) {
				
				// Find or create matching row
				QuestionSetInformation qsInfo = null;
				for(QuestionSetInformation i : questionSetInformation) {
					if (i.getId() == questionSet.getId() && i.getType() == questionSet.getType()) {
						qsInfo = i;
						break;
					}
				}
				if (qsInfo == null) {
					qsInfo = new QuestionSetInformation();
					qsInfo.setId(questionSet.getId());
					qsInfo.setType(questionSet.getType());
					questionSetInformation.add(qsInfo);
				}
				
				if (session.isClosed()) {
					qsInfo.setCompletedEvaluationCount(qsInfo.getCompletedEvaluationCount() + 1);
				} else {
					qsInfo.setPendingEvaluationCount(qsInfo.getPendingEvaluationCount() + 1);
				}
			}
		}
	}
	
	@Getter @Setter
	public static class QuestionSetInformation {
		private QuestionSetType type;
		private int id;
		private int completedEvaluationCount;
		private int pendingEvaluationCount;
		
		public String getName() {
			if (type == QuestionSetType.IB) {
				return QuestionSetType.IB.toString();
			} else {
				return type.toString() + "[" + id + "]";
			}
		}
		
		public int getOverallEvaluationCount() {
			return completedEvaluationCount + pendingEvaluationCount;
		}
	}
}
