package de.tud.nlp4web.project.evaluation.provider.api;

import java.util.List;

import javax.ejb.Local;

import de.tud.nlp4web.project.evaluation.provider.api.model.settings.Setting;

@Local
public interface AdminSettingsServiceLocal {

	/**
	 * Reads all Settings from the database 
	 */
	public List<Setting> getAllSettings() throws ServiceException;
	
	/**
	 * Writes a new value for the Setting to the database
	 */
	public void changeSetting(Setting setting) throws ServiceException;
}
