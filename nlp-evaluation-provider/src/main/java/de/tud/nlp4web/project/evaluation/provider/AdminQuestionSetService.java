package de.tud.nlp4web.project.evaluation.provider;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.tud.nlp4web.project.evaluation.app.security.Roles;
import de.tud.nlp4web.project.evaluation.provider.api.AdminQuestionSetServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import de.tud.nlp4web.project.evaluation.provider.impl.AnswerManager;
import de.tud.nlp4web.project.evaluation.provider.impl.QuestionManager;
import de.tud.nlp4web.project.evaluation.provider.impl.QuestionSetManager;
import lombok.extern.log4j.Log4j;

@Stateless
@Log4j
@DeclareRoles({Roles.ADMIN})
public class AdminQuestionSetService implements AdminQuestionSetServiceLocal {

	@Inject
	private QuestionSetManager qsMngr;

	@Inject
	private QuestionManager qMngr;

	@Inject
	private AnswerManager aMngr;
	
	@RolesAllowed(Roles.ADMIN) 
	@Override
	public List<QuestionSet> getQuestionSetOverview() throws ServiceException {
		List<QuestionSet> questionSets = new LinkedList<>();
		try {
			for(QuestionSetType type : QuestionSetType.values()) {
				questionSets.addAll(qsMngr.getAllQuestionSets(type));
			}
		} catch (IOException ex) {
			String msg = "Error while fetching all question sets.";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
		return questionSets;
	}

	@RolesAllowed(Roles.ADMIN) 
	@Override
	public List<QuestionSet> getQuestionSet(int id) throws ServiceException {
		List<QuestionSet> questionSets = new LinkedList<>();
		for(QuestionSetType type : QuestionSetType.values()) {
			try {
				QuestionSet qs = qsMngr.getQuestionSet(id, type);
				if (qs != null) {
					questionSets.add(qs);
				}
			} catch (IllegalArgumentException ex) {
				// Not a problem here. It is caused by calling the underlying methods with an id and a non-matching question set type
			} catch (IOException ex) {
				String msg = "Could not load question set data for id " + id;
				log.error(msg, ex);
				throw new ServiceException(msg);
			}
		}
		return questionSets;
	}

	@RolesAllowed(Roles.ADMIN) 
	@Override
	public void saveMetadata(QuestionSet questionSet) throws ServiceException {
		try {
			qsMngr.changeMetadata(questionSet);
		} catch (IOException ex) {
			String msg = "Updating metadata for question set " + questionSet.getId() + " failed.";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}

	@RolesAllowed(Roles.ADMIN) 
	@Override
	public List<QuestionSet> createQuestionSet() throws ServiceException {
		try {
			return qsMngr.createQuestionSet();
		} catch (IOException ex) {
			String msg = "Error creating question set";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}

	@Override
	@RolesAllowed(Roles.ADMIN) 
	public void lock(int id, QuestionSetType type) throws ServiceException {
		try {
			QuestionSet qs = qsMngr.getQuestionSet(id, type);
			if (qs == null) {
				throw new ServiceException("Unknown question set: " + type + "[" + id + "]");
			}
			qsMngr.lock(qs);
		} catch (IOException ex) {
			String msg = "Error locking question set " + type + "[" + id + "]";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}
	
	@Override
	@RolesAllowed(Roles.ADMIN)
	public int addQuestionTo(int id) throws ServiceException {
		try {
			List<QuestionSet> questionSets = getQuestionSet(id);
			if (questionSets == null || questionSets.isEmpty()) {
				throw new ServiceException("Could not add question. Question set with id " + id + " does not exist");
			}
			int questionId = qMngr.addQuestion(id);
			
			for(QuestionSet qs : questionSets) {
				QuestionSetType type = qs.getType();
				Answer answer = new Answer();
				answer.setId(-1);
				answer.setText("Correct Answer");
				aMngr.addAnswer(answer, questionId, type, true);
			}
			
			log.info("Added question " + questionId + " to question set " + id);
			
			return questionId;
			
		} catch (IOException ex) {
			String msg = "Could not add question for question set with id " + id;
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}
}
