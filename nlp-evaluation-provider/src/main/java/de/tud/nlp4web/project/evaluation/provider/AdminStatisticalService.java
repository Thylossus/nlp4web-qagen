package de.tud.nlp4web.project.evaluation.provider;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.tud.nlp4web.project.evaluation.app.security.Roles;
import de.tud.nlp4web.project.evaluation.provider.api.AdminStatisticalServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.provider.impl.EvaluationSessionManager;
import lombok.extern.log4j.Log4j;

@Stateless
@Log4j
@DeclareRoles({Roles.ADMIN})
public class AdminStatisticalService implements AdminStatisticalServiceLocal {

	@Inject
	private EvaluationSessionManager evalSessionMngr;
	
	@Override
	@RolesAllowed({Roles.ADMIN})
	public List<EvaluationSession> getAllSessions() throws ServiceException {
		List<EvaluationSession> sessions = new LinkedList<>();
		
		try {
			List<String> resumeKeys = evalSessionMngr.getAllResumeKeys();
			
			for(String key : resumeKeys) {
				sessions.add(evalSessionMngr.getSessionByKey(key));
			}
			
			return sessions;
		} catch (IOException ex) {
			String msg = "Error loading all sessions";
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
	}
	
}
