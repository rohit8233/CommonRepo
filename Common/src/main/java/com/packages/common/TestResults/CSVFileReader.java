package com.packages.common.TestResults;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

import com.packages.common.TestResults.CSVFileReader;


/**
 * 
 * @author Amarendra
 * @version 1.0
 * 
 */

public class CSVFileReader {
    private static final Logger logger = Logger.getLogger(CSVFileReader.class.getName());
    
    static String csvFile = "test_execution.csv";
    static BufferedReader br = null;
    static String line = "";
    static String cvsSplitBy = ",";
 
    public static String getTCTitle(String testcaseID){
        String testcaseTitle = null;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                
                // use comma as separator
                String[] testcaseInfo = line.split(cvsSplitBy);
                if (testcaseInfo[0].compareTo(testcaseID)==0){
                    testcaseTitle = testcaseInfo[1];
                }
            }
        } catch (FileNotFoundException ex) {
            logger.fatal("Cannot read the file, error: " + ex);
        } catch (IOException ex) {
            logger.fatal("Cannot read the file, error: " + ex);
        }
        return testcaseTitle;
    }
    
    public static String getTCSuite(String testcaseID){
        String testsuiteID= null;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                
                // use comma as separator
                String[] testcaseInfo = line.split(cvsSplitBy);
                if (testcaseInfo[0].compareTo(testcaseID)==0 & testcaseInfo[3].compareTo("defects")!=0){
                    testsuiteID =  testcaseInfo[2];
                }
            }
        } catch (FileNotFoundException ex) {
            logger.fatal("Cannot read the file, error: " + ex);
        } catch (IOException ex) {
            logger.fatal("Cannot read the file, error: " + ex);
        }
        return testsuiteID;
    }
    
    public static String getTSIDDefect(String testcaseID){
        String testsuiteID= "";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                
                // use comma as separator
                String[] testcaseInfo = line.split(cvsSplitBy);
                if (testcaseInfo[0].compareTo(testcaseID)==0 & testcaseInfo[3].compareTo("defects")==0){
                    testsuiteID = testcaseInfo[2];
                }
            }
        } catch (FileNotFoundException ex) {
            logger.fatal("Cannot read the file, error: " + ex);
        } catch (IOException ex) {
            logger.fatal("Cannot read the file, error: " + ex);
        }
        return testsuiteID;
    }
}