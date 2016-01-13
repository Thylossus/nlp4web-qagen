package de.tud.nlp4web.project.evaluation.provider.api;

import java.util.List;

import javax.ejb.Local;

import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;

@Local
public interface AdminQuestionSetServiceLocal {

	/**
	 * Lists all question sets
	 * @return a list of question sets
	 * @throws ServiceException if a technical error occurs
	 */
	public List<QuestionSet> getQuestionSetOverview() throws ServiceException;
	
	/**
	 * Returns the question set(s) matching the given id. If the type of question set is IB, the list will contain
	 * exactly one element, if it's DB/ES, both question set will be included in the list
	 * @param id the id used to search for question sets
	 * @return question set(s) as explained above. If the id is unknown, the list will be empty
	 * @throws ServiceException if a techincal error occurs
	 */
	public List<QuestionSet> getQuestionSet(int id) throws ServiceException;
	
	/**
	 * Stores the metadata (name and comment) for the given question set. Only name, comment and id have to be set in the object
	 * @param questionSet data to update
	 * @throws ServiceException if a technical error occurs
	 */
	public void saveMetadata(QuestionSet questionSet) throws ServiceException;
	
	/**
	 * Creates a new question set of type DB/ES
	 * @return the newly created question sets (one of type DB, one of type ES)
	 * @throws ServiceException if a technical error occurs
	 */
	public List<QuestionSet> createQuestionSet() throws ServiceException;
	
	/**
	 * Locks a question set and by this, makes it available for evaluation
	 * @param id of the question set
	 * @param type of the question set
	 * @throws ServiceException if a technical error occurs
	 */
	public void lock(int id, QuestionSetType type) throws ServiceException;
	
	/**
	 * Adds a new question to the question set(s) with the given id
	 * @param id the id to add the question to
	 * @return the id of the new question
	 * @throws ServiceException if an error occurs
	 */
	public int addQuestionTo(int id) throws ServiceException;
}
