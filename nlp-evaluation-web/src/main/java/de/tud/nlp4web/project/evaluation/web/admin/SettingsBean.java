package de.tud.nlp4web.project.evaluation.web.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.tud.nlp4web.project.evaluation.provider.api.AdminSettingsServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.settings.Setting;
import de.tud.nlp4web.project.evaluation.web.FacesContextHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@ViewScoped
@ManagedBean
@Log4j
public class SettingsBean implements Serializable {

	private static final long serialVersionUID = -281364160762666604L;

	@Getter @Setter
	private List<Setting> settings;
	
	@EJB
	private AdminSettingsServiceLocal settingsService;
	
	@PostConstruct
	public void postConstruct() {
		try {
			settings = settingsService.getAllSettings();
		} catch (ServiceException ex) {
			log.error("Could not load all settings", ex);
			throw new RuntimeException(ex);
		}
	}
	
	public String update(String key) {
		Setting setting = null;
		for(Setting s : settings) {
			if (s.getKey().name().equals(key)) {
				setting=s;
				break;
			}
		}
		if (setting != null) {
			try {
				settingsService.changeSetting(setting);
				
				FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_INFO, "The value of " + key.replace("_", " ") + " has been changed.");
			} catch (ServiceException ex) {
				log.error("Error saving new value for setting " + key + ": " + setting.getRawValue(), ex);
			}
		}
		return null;
	}
}
