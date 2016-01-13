package de.tud.nlp4web.project.evaluation.web;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public final class FacesContextHelper {

	private FacesContextHelper() {}
	
	public static void addFacesMessage(Severity severity, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(severity, message, (String)null));
	}
}
