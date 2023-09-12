package com.packages.common.TestResults;

import org.apache.log4j.Logger;

import com.packages.common.Constants;

/**
 * 
 * @author Amarendra
 * @version 1.1
 * 
 */
public class SaveResults {
    
	private static Logger log = Logger.getLogger(SaveResults.class);
    
    /**
     * Save the results on mysql and TestRail
     * @param tcID
     * @param tsID
     * @param status
     * @param errorMessage
     * @param timeExecution 
     */
	public static void saveResults(String productName, String tcID, String tsID, String tcName, String status,
			String errorMessage, String timeExecution, String executionDate, String feature_id) {
        String buildNumber = Constants.TEST_BUILD;
        int testLinkStatus = 0;
        log.info(testLinkStatus);
        MYSQLHelper  mysqlUpdate = new MYSQLHelper();
        mysqlUpdate.connectToMYSQL();
		mysqlUpdate.insertTC(productName, tcID, tsID, tcName, status, buildNumber,
				errorMessage, timeExecution, executionDate, feature_id);
       
    }
    
}
