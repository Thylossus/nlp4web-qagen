package de.tud.nlp4web.project.evaluation.web.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.tud.nlp4web.project.evaluation.provider.api.AdminQuestionSetServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import de.tud.nlp4web.project.evaluation.web.FacesContextHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@ManagedBean
@ViewScoped
@Log4j
public class QuestionSetDetailsBean implements Serializable {
	
	private static final long serialVersionUID = 2603932297720818116L;

	@EJB
	private AdminQuestionSetServiceLocal qsService; 
	
	/** The backing Question sets. Can be more than one due to the fact that DB and ES share their ID */
	private List<QuestionSet> questionSets;

	@Getter @Setter
	private int urlId;
	
	@Getter @Setter
	private int qsId;
	
	@Getter @Setter
	private String qsName;
	
	@Getter @Setter
	private String qsComment;
	
	@Getter @Setter
	private List<QuestionMetadata> questions;
	
	public void preRenderView() {
		
		if (qsId == 0) {
			
			if (urlId != 0) {
				try {
					qsId = urlId;
					
					questionSets = qsService.getQuestionSet(qsId);
					if (questionSets != null && questionSets.size() > 0) { 
						
						QuestionSet qs = questionSets.get(0);
						qsName = qs.getName();
						qsComment = qs.getComment();
						questions = convertQuestions(questionSets);
						
						boolean allLocked = true;
						boolean someLocked = false;
						
						for(QuestionSet lqs : questionSets) {
							allLocked &= lqs.isLocked();
							someLocked |= lqs.isLocked();
						}
						
						if (allLocked) {
							FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_WARN, "All of these question sets are locked. "
									+ "You probably don't want to edit them.");
						} else if (someLocked) {
							FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_WARN, "At least some of these question sets are locked. "
									+ "You probably don't want to edit them.");
						}
						
					} else {
						throw new ServiceException("No question set was found for id " + qsId);
					}
					
				} catch (ServiceException ex) {
					log.error("Could not load question set data for question set with id " + qsId, ex);
					qsId = 0;
				}
			}
			
			if (qsId == 0) {
				FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Could not load question set data");
			}
		}
	}
	
	private List<QuestionMetadata> convertQuestions(List<QuestionSet> questionSets) {
		List<QuestionMetadata> questionMetadata = new ArrayList<>(questionSets.get(0).getQuestions().size());
		for(QuestionSet questionSet : questionSets) {
			QuestionSetType type = questionSet.getType();
			for(Question question : questionSet.getQuestions()) {
				// count up i to find the metadata element with the same or a higher id in the session
				int i = 0;
				while(i < questionMetadata.size() && questionMetadata.get(i).getId() < question.getId()) i++;
				QuestionMetadata metadata = null;
				if(i >= questionMetadata.size() || questionMetadata.get(i).getId() > question.getId()) {
					// Create a new metadata element
					metadata = new QuestionMetadata();
					metadata.setId(question.getId());
					metadata.setText(question.getText());
					questionMetadata.add(metadata);
					
				} else {
					// There is already a metadata element
					metadata = questionMetadata.get(i);
				}
				int answerCount = question.getAnswers().size();
				switch(type) {
				case IB:
					metadata.setIbAnswerCount(answerCount);
					break;
				case ES:
					metadata.setEsAnswerCount(answerCount);
					break;
				case DB:
					metadata.setDbAnswerCount(answerCount);
					break;
				}
			}
		}
		return questionMetadata;
	}

	public QuestionSet getIbQuestionSet() {
		return getQuestionSet(QuestionSetType.IB);
	}

	public QuestionSet getDbQuestionSet() {
		return getQuestionSet(QuestionSetType.DB);
	}

	public QuestionSet getEsQuestionSet() {
		return getQuestionSet(QuestionSetType.ES);
	}
	
	public String getIdString() {
		if (getIbQuestionSet() != null) {
			return "IB";
		} else if (getDbQuestionSet() != null) {
			return "DB[" + qsId + "] / ES[" + qsId + "]";
		} else {
			return "";
		}
	}
	
	private QuestionSet getQuestionSet(QuestionSetType type) {
		if (questionSets != null) {
			for(QuestionSet qs : questionSets) {
				if (qs.getType() == type) 
					return qs;
			}
		}
		return null;
	}
	
	public String saveMetadata() {
		
		QuestionSet dataQs = new QuestionSet();
		dataQs.setComment(qsComment);
		dataQs.setName(qsName);
		dataQs.setId(qsId);
		try {
			
			qsService.saveMetadata(dataQs);
			for(QuestionSet qs : questionSets) {
				qs.setName(dataQs.getName());
				qs.setComment(dataQs.getComment());
			}
			
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_INFO, "Updating metadata was successful");
		} catch (ServiceException ex) {
			log.error("Error saving metadata", ex);
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving metadata");
		}
		
		return null;
	}
	
	public String addQuestion() {
		try {
			int newQuestionId = qsService.addQuestionTo(qsId);
			// ugly, but it works...
			return "/admin/question/edit.xhtml?faces-redirect=true&id=" + newQuestionId;
		} catch (ServiceException ex) {
			log.error("Error creating a new question", ex);
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured while adding a new question");
			return null;
		}
	}
	
	

	@Getter @Setter
	public class QuestionMetadata {
		
		private int id;
		
		private int ibAnswerCount;
		
		private int esAnswerCount;
		
		private int dbAnswerCount;
		
		private String text;
	}
}
