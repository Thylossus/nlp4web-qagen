package de.tud.nlp4web.project.evaluation.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.tud.nlp4web.project.evaluation.app.security.Roles;
import de.tud.nlp4web.project.evaluation.model.intermediate.DbQuestion;
import de.tud.nlp4web.project.evaluation.provider.api.AdminQuestionServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import de.tud.nlp4web.project.evaluation.provider.impl.AnswerManager;
import de.tud.nlp4web.project.evaluation.provider.impl.QuestionManager;
import lombok.extern.log4j.Log4j;

@Stateless
@DeclareRoles({Roles.ADMIN})
@Log4j
public class AdminQuestionService implements AdminQuestionServiceLocal {

	@Inject
	private QuestionManager questionMngr;

	@Inject
	private AnswerManager answerMngr;
	
	@Override
	@RolesAllowed({Roles.ADMIN})
	public Map<QuestionSetType, Question> getQuestion(int id) throws ServiceException {
		try {
			DbQuestion dbQuestion = questionMngr.getQuestion(id);
			if (dbQuestion == null) {
				return new HashMap<>();
			} else {
				Map<QuestionSetType, Question> questions = new HashMap<>();
				for(QuestionSetType type : QuestionSetType.values()) {
					Question question = dbQuestion.convertToModel(type);
					if (question.getAnswers().size() > 0) {
						questions.put(type, question);
					}
				}
				return questions;
			}
			
		} catch (IOException ex) {
			String msg = "Could not fetch question(s) for id " + id;
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}

	@Override
	public void saveQuestion(Map<QuestionSetType, Question> data) throws ServiceException {
		try {
			// Check constraints
			int questionId = -1;
			for(Question question : data.values()) {
				if (questionId == -1) {
					questionId = question.getId();
				} else {
					if (question.getId() != questionId) {
						throw new ServiceException("The question ids differ: " + questionId + " and " + question.getId() + ". This is not allowed.");
					}
				}
			}
			if (questionId == -1) {
				throw new ServiceException("An empty question map has been passed");
			}
			
			// Load model from backend
			DbQuestion dbQuestion = questionMngr.getQuestion(questionId);
			if (dbQuestion == null) {
				throw new ServiceException("The question does not exist in the database");
			}
			// Validate types
			Set<QuestionSetType> backendTypes = dbQuestion.getTypes();
			if (backendTypes.size() == data.keySet().size()) {
				backendTypes.removeAll(data.keySet());
				if (backendTypes.size() == 0) {
					
					// Update metadata (text + comment)
					Question firstQuestion = data.values().iterator().next();
					updateMetadata(firstQuestion);
					
					// Update answers
					for(QuestionSetType type : data.keySet()) {
						Question question = data.get(type);
						for(Answer answer : question.getAnswers()) {
							boolean correct = answer.equals(question.getCorrectAnswer());
							if (answer.getId() <= 0) {
								// Add answer
								if (answer.getText() != null && answer.getText().trim().length() > 0) {
									answerMngr.addAnswer(answer, question.getId(), type, correct);
								}
								
							} else {
								// Update answer
								Answer dbAnswer = dbQuestion.getAnswer(answer.getId());
								if (dbAnswer == null) {
									throw new ServiceException("The answer with id " + answer.getId() + " does not belong to question with id " + question.getId());
								}
								
								answerMngr.updateAnswer(answer);
							}
						}
					}
					return;
					
				}
			}
			throw new ServiceException("The question set types of the parameter data did not match the database data");
			
		} catch (IOException ex) {
			String msg = "Error saving new question data";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}
	
	/** Updated the questions metadata */
	private void updateMetadata(Question data) throws ServiceException {
		try {
			questionMngr.updateMetadata(data.getId(), data.getText(), data.getComment());
		} catch (IOException ex) {
			String msg = "Error saving question metadata";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}

}
