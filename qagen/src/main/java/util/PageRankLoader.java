package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.DBConfig;

/**
 * A helper class to read the page rank of a Wikipedia article from the database.
 */
public class PageRankLoader {

	private Connection conn;
	
	/**
	 * Initialize database connection.
	 */
	public PageRankLoader() {
		DBConfig dbconfig = DBConfig.getInstance();
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + dbconfig.host + "/" + dbconfig.database + "?user=" + dbconfig.user + "&password=" + dbconfig.password);
		} catch (SQLException e) {
			this.conn = null;
			// TODO Add logger
			e.printStackTrace();
		}
	}
	
	/**
	 * Get page rank for the provided article.
	 * @param articleId The article's id.
	 * @return The article's page rank or 0.0 if no page rank was found.
	 */
	public double getPageRank(int articleId) {
		try {
			PreparedStatement stmt = this.conn.prepareStatement("SELECT score FROM pagerank WHERE pageid = ?");
			stmt.setInt(1, articleId);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				return rs.getDouble("score");
			}
			
		} catch (SQLException e) {
			// TODO Add logging
			e.printStackTrace();
		}
		return 0.0;
	}
	
	/**
	 * Close the database connection.
	 */
	public void closeConnection() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Add logger
			e.printStackTrace();
		}
	}
	
}
