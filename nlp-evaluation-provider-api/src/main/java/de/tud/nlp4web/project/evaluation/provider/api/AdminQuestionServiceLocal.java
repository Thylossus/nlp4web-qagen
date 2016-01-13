package de.tud.nlp4web.project.evaluation.provider.api;

import java.util.Map;

import javax.ejb.Local;

import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;

@Local
public interface AdminQuestionServiceLocal {

	/**
	 * Loads all questions for the given ID. There can be more than one question for an ID because the question may be
	 * part of different question sets, thus having different answers. Note that the question metadata stays the same for
	 * all questions, regardless of the question set they are in
	 * @param id the id to look for
	 * @return Map of Questions or an empty map if none could be found
	 * @throws ServiceException if an error occurs
	 */
	public Map<QuestionSetType, Question> getQuestion(int id) throws ServiceException;
	
	/**
	 * Saves changes to this question. All questions passed to this method should have the same id, text and comment. If
	 * the id differs, this method will fail. If text or comment differ, it is not specified what will happen.<br />
	 * Answers that are known not already to be stored in the database should be marked with an id of 0 or less.
	 * @param data The data to store as a map of question set type and the specific question for that type.
	 * @throws ServiceException if something fails or one of the constraints mentioned above is violated.
	 */
	public void saveQuestion(Map<QuestionSetType, Question> data) throws ServiceException;
}
