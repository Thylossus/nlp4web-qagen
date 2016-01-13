package de.tud.nlp4web.project.evaluation.common.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.sql.DataSource;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
public abstract class AbstractDatabaseBean {
	
	@Resource(lookup = "jdbc/nlpweb")
	private DataSource dbDataSource;
	
	@Getter(AccessLevel.PROTECTED)
	private Connection dbConnection;

	@Getter(AccessLevel.PROTECTED)
	private final static Calendar dbCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+01:00"), Locale.GERMAN);
	
	@PostConstruct
	private void postConstruct() {
		try {
			dbConnection = dbDataSource.getConnection();
		} catch (SQLException ex) {
			log.error("Failed to connect to DB: ", ex);
			throw new RuntimeException("Error during establishing a database connection", ex);
		}
	}
	
	@PreDestroy
	private void preDestroy() {
		try {
			if (dbConnection != null && !dbConnection.isClosed()) {
				dbConnection.close();
			}
		} catch (SQLException ex) {
			log.error("Could not close DB Connection: ", ex);
		}
	}
}
