package com.packages.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Class To Connect SQL Database and Execute SQL Query.
 * @author Amarendra
 * 02/18/2015
 *
 */
public class SQLDBConnectHelper {
	
	public static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static Logger logger = Logger.getRootLogger();
	
	/**
	 * Create a database connection
	 * 
	 * @param databaseName the database to connect to.
	 * @param sqlServerURL
	 * @param username
	 * @param password
	 * @return returns the connection
	 */
	public static Connection createConnection(String databaseName, String sqlServerURL, String username, String password) {
		
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			if (!sqlServerURL.startsWith("jdbc:")) {
				sqlServerURL = "jdbc:sqlserver://" + sqlServerURL;
			}
			String connectionUrl = sqlServerURL + ";databaseName=" + databaseName +";user=" + username +";password="+ password;
			logger.debug("SQL server connection: " + connectionUrl);
			System.out.println("SQL server connection: " + connectionUrl);
			conn = DriverManager.getConnection(connectionUrl);
		}
		catch (SQLException e) {	
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {	
			e.printStackTrace();
		} 
		return conn;
	}
		
	/**
	 * Closes the database connection
	 * 
	 * @param conn The connection to close
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {	
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Run a query
	 * 
	 * @param conn the database connection.
	 * @param query the query to run
	 * @return returns the result set
	 */
	public static ResultSet runQuery(Connection conn, String query) {
		ResultSet resultSet = null;
		if (conn != null) {
			try {
				logger.debug("SQL query to execute: " + query);
				Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				resultSet = st.executeQuery(query);
			}
			catch (SQLException e) {	
				e.printStackTrace();
			} 
		}
		return resultSet;
	}
	
	/**
	 * Executes the passed query against the passed connection parameters. The
	 * result is a single value; first element of the first row of the result
	 * set. Use this method to retrieve counts and other single value results.
	 * 
	 * @param databaseName
	 * @param sqlServerURL
	 * @param username
	 * @param password
	 * @param query
	 * @return
	 */
	public static String executeScalar(String databaseName,
			String sqlServerURL, String username, String password, String query) {

		String result = null;

		Connection conn = createConnection(databaseName, sqlServerURL,
				username, password);
		ResultSet resultSet = SQLDBConnectHelper.runQuery(conn, query);

		try {
			resultSet.beforeFirst();
			while (resultSet.next()) {
				result = resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLDBConnectHelper.closeConnection(conn);
		}

		return result;
	}

	/**
	 * Executes the passed query against the passed connection object. The
	 * result is a single value; first element of the first row of the result
	 * set. Use this method to retrieve counts and other single value results.
	 * 
	 * @param conn the database connection.
	 * @param query the query to run
	 * @return returns the scalar result as string
	 */
	public static String executeScalar(Connection conn, String query) {
		String result = null;
		ResultSet resultSet = null;
		if (conn != null) {
			try {
				resultSet = SQLDBConnectHelper.runQuery(conn, query);
				if (resultSet != null) {
					resultSet.beforeFirst();
					while (resultSet.next()) {
						result = resultSet.getString(1);
					}
				}
			} catch (SQLException e) {	
				e.printStackTrace();
			} finally {
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch(SQLException ex) {}
				}
			}
		}
		return result;
	}

	/**
	 * Executes the passed query against the passed connection parameters. The
	 * result is a list of values for provided column.
	 * @param databaseName
	 * @param sqlServerURL
	 * @param username
	 * @param password
	 * @param query
	 * @param column
	 * @return
	 */
	public static List<String> getListByQuery(String databaseName,	String sqlServerURL,
			String username, String password, String query, String column) {

		List<String> result = new ArrayList<>();

		Connection conn = null;
		ResultSet resultSet = null;

		try {
			conn = createConnection(databaseName, sqlServerURL,	username, password);
			resultSet = SQLDBConnectHelper.runQuery(conn, query);
			resultSet.beforeFirst();
			while (resultSet.next()) {
				result.add(resultSet.getString(column));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLDBConnectHelper.closeConnection(conn);
		}

		return result;
	}

	/**
	 * Method to run update insert queries.
	 * @param conn
	 * @param query
	 */
	public static void runUpdateQuery(Connection conn, String query) {

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query );
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		
}