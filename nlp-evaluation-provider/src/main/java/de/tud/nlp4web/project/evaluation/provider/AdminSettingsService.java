package de.tud.nlp4web.project.evaluation.provider;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.tud.nlp4web.project.evaluation.app.security.Roles;
import de.tud.nlp4web.project.evaluation.provider.api.AdminSettingsServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.settings.Setting;
import de.tud.nlp4web.project.evaluation.provider.api.model.settings.SettingsKey;
import de.tud.nlp4web.project.evaluation.provider.impl.SettingsManager;
import lombok.extern.log4j.Log4j;

@Stateless
@DeclareRoles({Roles.ADMIN})
@Log4j
public class AdminSettingsService implements AdminSettingsServiceLocal {

	@Inject
	private SettingsManager settingsMngr;
	
	@Override
	@RolesAllowed(Roles.ADMIN)
	public List<Setting> getAllSettings() throws ServiceException {
		List<Setting> settings = new LinkedList<>();
		for(SettingsKey key : SettingsKey.values()) {
			Setting setting = settingsMngr.getSetting(key);
			if (setting != null) {
				settings.add(setting);
			}
		}
		return settings;
	}

	@Override
	@RolesAllowed(Roles.ADMIN)
	public void changeSetting(Setting setting) throws ServiceException {
		try {
			settingsMngr.setSetting(setting);
		} catch (IOException ex) {
			String msg = "Error saving setting value " + setting.getKey() + "=" + setting.getRawValue();
			log.error(msg, ex);
			throw new ServiceException(msg);
		}
		
	}

}
