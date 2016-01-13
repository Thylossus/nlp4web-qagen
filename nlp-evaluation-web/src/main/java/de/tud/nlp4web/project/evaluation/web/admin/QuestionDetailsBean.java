package de.tud.nlp4web.project.evaluation.web.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.tud.nlp4web.project.evaluation.provider.api.AdminQuestionServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import de.tud.nlp4web.project.evaluation.web.FacesContextHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@ManagedBean
@ViewScoped
@Log4j
public class QuestionDetailsBean implements Serializable {
	
	private static final long serialVersionUID = 8584632111456539305L;

	@EJB
	private AdminQuestionServiceLocal qService;
	
	@Getter @Setter
	private int urlId;
	
	@Getter @Setter
	private int questionId;
	
	private Map<QuestionSetType, Question> questions;

	@Getter @Setter
	private String questionText;

	@Getter @Setter
	private String comment;
	
	@Getter @Setter
	private String openTriviaInput;
	
	@Getter
	private List<AnswerRow> answerRows;
	
	public void preRenderView() {
		
		if (questionId == 0) {
			
			if (urlId != 0) {
				try {
					questionId = urlId;
					questions = qService.getQuestion(questionId);
					
					if (questions == null || questions.isEmpty()) {
						throw new ServiceException("No question was found for id " + questionId);
					}
					
					Question firstQuestion = questions.values().iterator().next();
					questionText = firstQuestion.getText();
					comment = firstQuestion.getComment();
					
					answerRows = new LinkedList<>();
					for(QuestionSetType questionType : questions.keySet()) {
						Question question = questions.get(questionType);
						AnswerRow answerRow = new AnswerRow();
						answerRow.setType(questionType);
						List<Answer> otherAnswers = new LinkedList<>();
						answerRow.setOtherAnswers(otherAnswers);
						for(Answer answer : question.getAnswers()) {
							if (answer.equals(question.getCorrectAnswer())) {
								answerRow.setCorrectAnswer(answer);
							} else {
								otherAnswers.add(answer);
							}
						}
						
						answerRow.fillUp();
						answerRows.add(answerRow);
					}
					
				} catch (ServiceException ex) {
					log.error("Could not load question data for question set with id " + questionId, ex);
					questionId = 0;
				}
			}
			
			if (questionId == 0) {
				FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Could not load question data");
			}
		}
	}
	
	public String cancel() {
		
		return returnToQuestionSet();
	}
	
	public String save() {
		Map<QuestionSetType, Question> updateData = new HashMap<>();
		for (QuestionSetType type : questions.keySet()) {
			Question oldQuestion = questions.get(type);
			Question newQuestion = new Question();
			newQuestion.setComment(comment);
			newQuestion.setText(questionText);
			newQuestion.setId(oldQuestion.getId());
			newQuestion.setQuestionSetID(oldQuestion.getQuestionSetID());
			for(AnswerRow row : answerRows) {
				if (row.getType() == type) {
					newQuestion.setCorrectAnswer(row.getCorrectAnswer());
					for(Answer a : row.getOtherAnswers()) {
						newQuestion.addAnswer(a);
					}
				}
			}
			updateData.put(type, newQuestion);
		}
		
		try {
			qService.saveQuestion(updateData);
		} catch (ServiceException ex) {
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Could not save question data");
			log.error("Error saving question data", ex);
			return null;
		}
		
		return returnToQuestionSet();
	}
	
	private String returnToQuestionSet() {
		Question firstQuestion = questions.values().iterator().next();
		return "/admin/questionsets/edit.xhtml?faces-redirect=true&id=" + firstQuestion.getQuestionSetID();
	}
	
	public String importOpenTrivia() {
		BufferedReader importReader = new BufferedReader(new StringReader(openTriviaInput));
		
		String line = null;
		String importQuestionText = null;
		String importCorrectAnswer = null;
		List<String> importAnswers = new LinkedList<>();
		try {
			while(null!=(line = importReader.readLine())) {
				line = line.trim();
				if (line.length() > 0) {
					if(line.startsWith("#Q ") && importQuestionText == null) {
						importQuestionText = line.substring("#Q ".length());
					} else if (line.startsWith("^ ")) {
						importCorrectAnswer = line.substring("^ ".length());
					} else if (line.matches("[A-D] .*")) {
						importAnswers.add(line.substring(2));
					}
				}
			}
			
			if (importQuestionText != null && importCorrectAnswer != null && importAnswers.size() == 4 && importAnswers.contains(importCorrectAnswer)) {
				
				importAnswers.remove(importCorrectAnswer);
				
				questionText = importQuestionText;
				for(AnswerRow row : answerRows) {
					row.getCorrectAnswer().setText(importCorrectAnswer);
					if (row.getType() == QuestionSetType.IB || row.getType() == QuestionSetType.DB) {
						for(int i = 0; i < importAnswers.size() && i < row.getOtherAnswers().size(); i++) {
							row.getOtherAnswers().get(i).setText(importAnswers.get(i));
						}
					}
				}
				
				FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_INFO, "Import successful");
			} else {
				FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_WARN, "Could not parse the import data");
			}
			
		} catch (IOException ex) {} // Won't happen
		
		return null;
	}
	
	@Getter @Setter
	public static class AnswerRow {
		private QuestionSetType type;
		private Answer correctAnswer;
		private List<Answer> otherAnswers;
		
		/** Assures that there is a correct answer and three alternatives. Newly generated answers (that are not in the DB) get the id 0 */
		void fillUp() {
			int n = -1;
			if (correctAnswer == null) {
				correctAnswer = new Answer();
				correctAnswer.setId(n--);
			}
			while(otherAnswers.size() < 3) {
				Answer answer = new Answer();
				answer.setId(n--);
				otherAnswers.add(answer);
			}
		}
	}
	
	
}
