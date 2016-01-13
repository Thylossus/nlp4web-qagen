package de.tud.nlp4web.project.evaluation.provider.api.model.settings;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Setting implements Serializable {

	private static final long serialVersionUID = -1626890029447058262L;

	@Getter
	private SettingsKey key;
	
	@Getter @Setter
	private String rawValue;
	
	@Getter
	private String description;
	
	public Setting(SettingsKey key, String rawValue, String description) {
		this.key = key;
		this.rawValue = rawValue;
		this.description = description;
	}
	
	public Setting() {
		this(null, null, null);
	}
	
	public String getStringValue() {
		return rawValue;
	}
	
	public int getIntValue() {
		try {
			return Integer.parseInt(rawValue);
		} catch (NumberFormatException ex) {
			throw new IllegalStateException("Not a number: " + key + "=" + rawValue, ex);
		}
	}
}
