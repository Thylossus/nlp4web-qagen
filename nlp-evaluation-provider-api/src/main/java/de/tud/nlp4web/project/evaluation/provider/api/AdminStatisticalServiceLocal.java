package de.tud.nlp4web.project.evaluation.provider.api;

import java.util.List;

import javax.ejb.Local;

import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;

@Local
public interface AdminStatisticalServiceLocal {

	public List<EvaluationSession> getAllSessions() throws ServiceException;
	
	
}
