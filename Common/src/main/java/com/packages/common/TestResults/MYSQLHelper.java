package com.packages.common.TestResults;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.packages.common.Config;
import com.packages.common.Constants;

/**
 * 
 * @author Amarendra
 * @version 1.0
 * 
 */

public class MYSQLHelper {

	private Connection con;

	private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final Logger logger = Logger.getLogger(MYSQLHelper.class);
    
    /**
     * This method connects to mysql Server
     * it reads information from properties file
     */
    public void connectToMYSQL() {
        String mysqlServer = Config.getMYSQLServer();
        String userMYSQL = Config.getMYSQLUser();
        String passwordMYSQL = Config.getMYSQLPassword();
        String portMYSQL = Config.getMYSQLPort();
        String databaseMYSQL = Config.getMYSQLDB();
        try {  
            Class.forName(DRIVER_CLASS);
            String connectionUrl = "jdbc:mysql://"+mysqlServer +":" + portMYSQL + "/" + databaseMYSQL +"?" + 
                                   "user=" + userMYSQL + "&password=" + passwordMYSQL;
            System.out.println(connectionUrl);
            con = (Connection) DriverManager.getConnection(connectionUrl);
        } catch (Exception ex) {
            logger.fatal("It was not possible to connect to MYSQL database, error: " + ex.toString());
        }
        logger.info("Connected to database");
    }
    
    /**
     * This method inserts test case result to mysql
     * @param productName
     * @param TCID
     * @param TSID
     * @param TCNAME
     * @param status
     * @param date
     * @param build
     * @param description
     * @param timeExecution
     * @param feature_id 
     */
	public void insertTC(String productName, String TCID, String TSID, String TCNAME, String status,
			String build, String description, String timeExecution, String executionDate, String feature_id) {
        Statement stmt = null;
        String sqlQuery = "INSERT INTO execution_status (testcase_id, testsuite_id,"
                + "testcase_status, build, description, timeexecution,"
                + "testcase_name,prod_name,execution_date,feature_id) "
                + "VALUES ('"
                + TCID
                + "', '"
                + TSID
                + "', '"
                + status
                + "', '"
                + build
                + "', '"
                + description.replace("'", "''")
                + "', '"
                + timeExecution
                + "', '"
                + TCNAME.replace("'", "''")
                + "', '"
                + productName
                + "', '"
                + executionDate
                +"','"
                + feature_id
                + "')";
        try{
            stmt = (Statement) con.prepareStatement(sqlQuery);
            stmt.executeUpdate(sqlQuery);
            stmt.close();
        } catch (SQLException ex) {
            logger.fatal("It was not possible to insert, error: " + ex.toString());
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                logger.fatal("It was not possible to close the connection, error: " + ex.toString());
            }
        }
        logger.info("The following query was inserted: " + sqlQuery);
    }

	/**
	 * function to connect testlinkDb
	 */
	public void connectToMYSQLTestLinkDB() {
		String mysqlServer = Config.getMYSQLServer();
        String userMYSQL = Config.getMYSQLUser();
        String passwordMYSQL = Config.getMYSQLPassword();
        String portMYSQL = Config.getMYSQLPort();
        String databaseMYSQL = Config.getMYSQLDBTestLink();
        try {  
            Class.forName(DRIVER_CLASS);
            String connectionUrl = "jdbc:mysql://"+mysqlServer +":" + portMYSQL + "/" + databaseMYSQL +"?" + 
                                   "user=" + userMYSQL + "&password=" + passwordMYSQL;
            System.out.println(connectionUrl);
            con = (Connection) DriverManager.getConnection(connectionUrl);
        } catch (Exception ex) {
            logger.fatal("It was not possible to connect to MYSQL database, error: " + ex.toString());
        }
        logger.info("Connected to database");
		
	}
	
	/**
	 * 
	 * @return Status Count of Test Cases after Execution.
	 */
	public long getStatusCount(String status, String testsuite_Id, String prod_Name) {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		long statusCount = 0;
		try {
			pStmt = con.prepareStatement(Constants.Query.STATUS_COUNT);
			pStmt.setString(1, status);
			pStmt.setString(2, testsuite_Id);
			pStmt.setString(3, prod_Name);

			rs = pStmt.executeQuery();
			rs.next();
			statusCount = rs.getLong(1);
		} catch (SQLException ex) {
			logger.fatal("It was not possible to get, error: " + ex.toString());
		} finally {
			close(rs);
			close(pStmt);
		}
		return statusCount;
	}

	public List<String[]> getTestSetName(String prodName) {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		List<String[]> listOfTestSet = new ArrayList<String[]>();
		try {
			pStmt = con.prepareStatement(Constants.Query.SELECT_TEST_SUITES_FOR_PRODUCT);
			pStmt.setString(1, prodName);

			rs = pStmt.executeQuery();
			int nCol = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					row[iCol - 1] = rs.getObject(iCol).toString();
				}
				listOfTestSet.add(row);
			}
		} catch (SQLException ex) {
			logger.fatal("It was not possible to get, error: " + ex.toString());
		} finally {
			close(rs);
			close(pStmt);
		}
		return listOfTestSet;
	}

	public List<String[]> getDetailedStatus(String testSetId, String prod_Name) {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		List<String[]> detailedStatus = new ArrayList<String[]>();
		try {
			pStmt = con.prepareStatement(Constants.Query.SELECT_ALL_FROM_EXECUTION_STATUS_FOR_PRODUCT);
			pStmt.setString(1, testSetId);
			pStmt.setString(2, prod_Name);

			rs = pStmt.executeQuery();
			int nCol = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				String[] row = new String[nCol];
				for (int iCol = 1; iCol <= nCol; iCol++) {
					row[iCol - 1] = rs.getObject(iCol).toString();
				}
				detailedStatus.add(row);
			}
		} catch (SQLException ex) {
			logger.fatal("It was not possible to get, error: " + ex.toString());
		} finally {
			close(rs);
			close(pStmt);
		}
		return detailedStatus;
	}

	/**
	 * Returns last 30 execution status for test case id specified by {@code tcid}
	 * @param tcid
	 * @return
	 */
	public List<String[]> getLast30ExecutionStatus(String tcid) {
		return getLastNExecutionStatus(tcid, 30);
	}

	/**
	 * Returns last N execution status for test case id specified by {@code tcid}
	 * @param tcid
	 * @return
	 */
	public List<String[]> getLastNExecutionStatus(String tcid, int n) {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		List<String[]> statuses = new ArrayList<String[]>();
		try {
			pStmt = con.prepareStatement(Constants.Query.SELECT_LAST_N_STATUS_FOR_TCID);
			pStmt.setString(1, tcid);
			pStmt.setInt(2, n);

			rs = pStmt.executeQuery();
			while (rs.next()) {
				statuses.add(new String[] { rs.getString("testcase_status"), rs.getString("execution_date"),
						rs.getString("build") });
			}
		} catch (SQLException ex) {
			logger.fatal("It was not possible to get, error: " + ex.toString());
		} finally {
			close(rs);
			close(pStmt);
		}
		return statuses;
	}

	private void close(AutoCloseable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (Exception sqle) {
			}
		}
	}
}
