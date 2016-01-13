package de.tud.nlp4web.project.evaluation.provider.api;

import javax.ejb.ApplicationException;

/**
 *  This exception is thrown whenever a service fails
 */
@ApplicationException(rollback = true)
public class ServiceException extends Exception {

	private static final long serialVersionUID = 60911278756570594L;

	public ServiceException(String message) {
		super(message);
	}
}
