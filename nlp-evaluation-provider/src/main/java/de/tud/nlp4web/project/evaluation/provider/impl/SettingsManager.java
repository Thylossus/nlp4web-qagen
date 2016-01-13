package de.tud.nlp4web.project.evaluation.provider.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import de.tud.nlp4web.project.evaluation.common.impl.AbstractDatabaseBean;
import de.tud.nlp4web.project.evaluation.provider.api.model.settings.Setting;
import de.tud.nlp4web.project.evaluation.provider.api.model.settings.SettingsKey;
import lombok.extern.log4j.Log4j;

/**
 * This class takes care of managing the settings table in the database
 */
@RequestScoped
@Log4j
public class SettingsManager extends AbstractDatabaseBean {

	private HashMap<SettingsKey, Setting> cachedSettings;
	
	@PostConstruct
	private void postConstruct() {
		cachedSettings = new HashMap<>();
		try(
				PreparedStatement stmt = getDbConnection().prepareStatement("SELECT param, value, description FROM settings");
				ResultSet rs = stmt.executeQuery()
		) {
			while(rs.next()) {
				SettingsKey setting = SettingsKey.valueOf(rs.getString(1));
				if (setting != null) {
					cachedSettings.put(setting, new Setting(setting, rs.getString(2), rs.getString(3)));
				} else {
					log.warn("Unknown settings key in database: " + rs.getString(1));
				}
			}
		} catch (SQLException ex) {
			log.error(ex);
			throw new RuntimeException("Could not read Settings", ex);
		}
	}
	
	/**
	 * Returns the current value of a setting
	 * @param key Key for the setting to query
	 * @return Setting or null if the key was not found in the database
	 */
	public Setting getSetting(SettingsKey key) {
		return cachedSettings.get(key);
	}
	
	/**
	 * Changes a value of a setting in the database
	 * @param setting the setting to change
	 * @throws IOException Database error
	 */
	public void setSetting(Setting setting) throws IOException {
		if (!cachedSettings.containsKey(setting.getKey())) {
			throw new IOException("Setting not present in DB. Cannot change " + setting.getKey());
		}
		try(PreparedStatement stmt = getDbConnection().prepareStatement("UPDATE settings SET value = ? where param = ?")) {
			stmt.setString(1, setting.getRawValue());
			stmt.setString(2, setting.getKey().name());
			stmt.executeUpdate();
			log.info("Changed setting: " + setting.getKey() + "=" + setting.getRawValue() + " (was " + cachedSettings.get(setting.getKey()).getRawValue() + ")");
			cachedSettings.get(setting.getKey()).setRawValue(setting.getRawValue());
		} catch (SQLException ex) {
			throw new IOException("Error while changing settings", ex);
		}
	}
}
