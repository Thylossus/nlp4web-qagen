package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;

public class DBConfig {

	private static DBConfig instance;
	
	public final String host;
	public final String database;
	public final String user;
	public final String password;
	public final Language language = Language.english;
	
	/**
	 * Try to read the db.properties file. If this file is not available, read db.default.properties.
	 * The properties in the files are copied to the instance's attributes.
	 */
	private DBConfig() {
		InputStream inputStream = DBConfig.class.getClassLoader().getResourceAsStream("config/db.properties");
		
		if (inputStream == null) {
			inputStream = DBConfig.class.getClassLoader().getResourceAsStream("config/db.default.properties");
		}
		
		Properties props = new Properties();
		boolean loadingSuccess = false;
		
		try {
			props.load(inputStream);
			loadingSuccess = true;
		} catch (IOException e) {
			// TODO Add logging (msg: properties file for the database not found; initialized all properties with empty values)
			e.printStackTrace();
		}
		
		if (loadingSuccess) {
			this.host = props.getProperty("host", "");
			this.database = props.getProperty("database", "");
			this.user = props.getProperty("user", "");
			this.password = props.getProperty("password", "");
		} else {
			this.host = "";
			this.database = "";
			this.user = "";
			this.password = "";
		}
		
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Add logging
			e.printStackTrace();
		}
	}
	
	/**
	 * Get an instance of the DBConfig class. This instance has the values of the db.properties file as attributes.
	 * @return The instance.
	 */
	public static DBConfig getInstance() {
		if (DBConfig.instance == null) {
			DBConfig.instance = new DBConfig();
		}
		
		return DBConfig.instance;
	}
	
	/**
	 * Get a JWPL DatabaseConfiguration object from the configuration stored in the instance of this singleton.
	 * @return A JWPL DatabaseConfiguration object.
	 */
	public static DatabaseConfiguration getJwplDbConfig() {
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
		DBConfig instance = DBConfig.getInstance();
		dbConfig.setHost(instance.host);
		dbConfig.setDatabase(instance.database);
		dbConfig.setUser(instance.user);
		dbConfig.setPassword(instance.password);
		dbConfig.setLanguage(Language.english);
		
		return dbConfig;
	}
	
}
