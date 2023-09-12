package com.packages.common.TestResults;

import static com.packages.common.TestResults.MessageConstants.ERROR_MESSAGE_TC;
import static com.packages.common.TestResults.MessageConstants.PASSED_MESSAGE_TC;
import static com.packages.common.TestResults.MessageConstants.WARNING_MESSAGE;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.packages.common.Config;
import com.packages.common.Constants;

/**
 * 
 * @author Amarendra
 * @version 1.0
 *
 */
public class TestReport implements ITestListener {

	private String productName = "MedRev";
	private String filePath = Config.getScreenShotsRootLocation() + Constants.SCREENSHOT_FOLDER_NAME_EADMIN ;
	private List<String> logReport;

	private static final String LOG4J_PROPERTIES = "..//log4j.properties";
	private static final Logger logger = Logger.getLogger(TestReport.class);
	long time;

	/**
	 * Constants used to specify the expected execution result
	 */
	public enum status {
		PASS, FAIL, SKIPPED, PERCENTFAIL, ERROR
	};

	static {
		PropertyConfigurator.configure(LOG4J_PROPERTIES);
	}

	@Override
	public void onStart(ITestContext result) {
		logger.info("-----------------------------------");
		System.out.println("Start of Execution(TEST SUITE)-> " + result.getName());
		productName = result.getSuite().getParameter("productName");
		System.out.println("Product Name-> " + productName);
		logger.info("Start of Execution(TEST SUITE)-> " + result.getName());
	}

	@Override
	public void onTestStart(ITestResult result) {
		logger.info("-----------------------------------");
		logger.info("Test Started-> " + result.getName());
		System.out.println("-----------------------------------");
		System.out.println("Test Started-> " + result.getName());

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		Object[] testParameters = (Object[]) result.getParameters();
		String tcId = (String) testParameters[0];
		String tsId = (String) testParameters[1];
		String tcName = (String) testParameters[2];
		String feature_id = (String) testParameters[3];

		time = result.getEndMillis() - result.getStartMillis();
		logger.info("Test Pass-> " + result.getName());
		logger.info("Test Case ID-> " + tcId);
		logger.info("Execution time -> " + time / 1000 + " seconds");
		logger.info("Message -> " + PASSED_MESSAGE_TC);
		System.out.println("***********************************************");
		String notes = "";
		String verdict = "";
		long time = result.getEndMillis() - result.getStartMillis();
		long milliseconds = time % 1000;
		long seconds = (time / 1000) % 60;
		long minutes = (time / (60 * 1000)) % 60;
		long hours = (time / (60 * 60 * 1000)) % 24;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date).toString());
		String executionDate = dateFormat.format(date).toString();
		String duration = hours + ":" + minutes + ":" + seconds + "." + milliseconds;
		notes = compileTestResults(status.PASS, result, duration);
		verdict = getVerdict(status.PASS);
		takeScreenShot(result.getName() + tcId);
		SaveResults.saveResults(productName, tcId, tsId, tcName, verdict, notes, duration, executionDate,feature_id);
		logger.info("-----------------------------------");
		Reporter.clear();
	}

	@Override
	public void onTestFailure(ITestResult result) {
		Object[] testParameters = (Object[]) result.getParameters();
		String tcId = (String) testParameters[0];
		String tsId = (String) testParameters[1];
		String tcName = (String) testParameters[2];
		String feature_id = (String) testParameters[3];
		time = result.getEndMillis() - result.getStartMillis();
		logger.error("Test Failed-> " + result.getName());
		logger.error("Test Case ID -> " + tcId);
		logger.info("Execution time -> " + time / 1000 + " seconds");
		logger.info("Message -> " + ERROR_MESSAGE_TC);
		System.out.println("***********************************************");
		String notes = "";
		String verdict = "";
		long time = result.getEndMillis() - result.getStartMillis();
		long milliseconds = time % 1000;
		long seconds = (time / 1000) % 60;
		long minutes = (time / (60 * 1000)) % 60;
		long hours = (time / (60 * 60 * 1000)) % 24;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date).toString());
		String executionDate = dateFormat.format(date).toString();
		String duration = hours + ":" + minutes + ":" + seconds + "." + milliseconds;
		notes = compileTestResults(status.FAIL, result, duration);
		verdict = getVerdict(status.FAIL);
		ERROR_MESSAGE_TC = notes;
		takeScreenShot(result.getName() + tcId);
		SaveResults.saveResults(productName, tcId, tsId, tcName, verdict, notes, duration, executionDate,feature_id);
		System.out.println("Message -> " + ERROR_MESSAGE_TC);
		System.out.println("***********************************************");
		logger.info("-----------------------------------");
		Reporter.clear();
	}

	public void takeScreenShot(String methodName) {
		// get the driver
		try {
			WebDriver driver = Config.getDefaultDriver();
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			// The below method will save the screen shot in d drive with test
			// method name
			FileUtils.copyFile(scrFile, new File(filePath + methodName + ".png"));
			System.out.println("***Placed screen shot in " + filePath + " ***");
		} catch (Exception e) {
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		Object[] testParameters = (Object[]) result.getParameters();
		String tcId = (String) testParameters[0];
		String tsId = (String) testParameters[1];
		String tcName = (String) testParameters[2];
		String feature_id = (String) testParameters[3];
		logger.warn("Test Skipped-> " + result.getName() + " Test Case ID -> " + tcId);
		System.out.println("Test Skipped-> " + result.getName() + " Test Case ID -> " + tcId);
		time = result.getEndMillis() - result.getStartMillis();
		// Get the duration time in Milliseconds and convert it to Hours and a
		// time format
		String notes = "";
		String verdict = "";
		long time = result.getEndMillis() - result.getStartMillis();
		long milliseconds = time % 1000;
		long seconds = (time / 1000) % 60;
		long minutes = (time / (60 * 1000)) % 60;
		long hours = (time / (60 * 60 * 1000)) % 24;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date).toString());
		String executionDate = dateFormat.format(date).toString();
		String duration = hours + ":" + minutes + ":" + seconds + "." + milliseconds;
		notes = WARNING_MESSAGE;
		verdict = getVerdict(status.SKIPPED);
		WARNING_MESSAGE = notes;

		SaveResults.saveResults(productName, tcId, tsId, tcName, verdict, notes, duration, executionDate, feature_id);
		System.out.println("Message -> " + WARNING_MESSAGE);
		System.out.println("***********************************************");
		logger.info("-----------------------------------");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		logger.info("Success percentage failure -> " + result.toString());
	}

	@Override
	public void onFinish(ITestContext context) {
		int numTCsFails = context.getFailedTests().size();
		int numTCsPassed = context.getPassedTests().size();
		int numTCsSkipped = context.getSkippedTests().size();

		logger.info("Test cases execution number: " + numTCsPassed + " PASSED");
		logger.info("Test cases execution number: " + numTCsFails + " FAILED");
		logger.info("Test cases execution number: " + numTCsSkipped + " SKIPPED");

		logger.info("End of Execution(TEST SUITE)-> " + context.getName());
		logger.info("-----------------------------------");
	}

	/*
	 * Reads the execution Log from a test result
	 */
	private String compileTestResults(status stat, ITestResult tr, String duration) {
		String methodName = tr.getMethod().getMethodName();
		int i = 1;
		Boolean testPassed = true;
		if (stat != status.PASS)
			testPassed = false;
		StringBuilder msg = new StringBuilder("Finished " + methodName + " test - ");
		if (testPassed) {
			msg.append("PASSED - Total Time: " + duration);
			/*
			 * logReport = Reporter.getOutput(); for (String log : logReport) {
			 * msg.append("\n Step "+ i+ ": " + log ); i++; }
			 */
		} else {
			msg.append("FAILED");
			logReport = Reporter.getOutput();
			for (String log : logReport) {
				if (i == logReport.size()) {
					msg.append("\n\n FAILED AT:\n Step " + i + ": " + log);
				} else {
					msg.append("\n Step " + i + ": " + log);
				}
				i++;
			}
			msg.append(getExceptionInfo(tr));
		}
		return msg.toString();
	}

	/**
	 * @param tr
	 * @param msg
	 */
	private StringBuilder getExceptionInfo(ITestResult tr) {
		StringBuilder msg = new StringBuilder();
		// If the test failed due to an exception, include the exception info in
		// the log message, unless it's a TestNG timeout exception
		Throwable t = tr.getThrowable();
		if (t != null) {
			String nl = System.getProperty("line.separator");
			msg.append(nl);
			msg.append(" ");
			msg.append(t.toString());
			// If it's not a thread timeout, include the stack trace too
			if (!(t instanceof org.testng.internal.thread.ThreadTimeoutException)) {
				for (StackTraceElement e : t.getStackTrace()) {
					msg.append(nl);
					msg.append(" ");
					msg.append(e.toString());
				}
			}
		}
		return msg;
	}

	/**
	 * Returns the current execution verdict into a string
	 * 
	 * @param stat
	 *           
	 * @return  current execution status
	 */
	private String getVerdict(status stat) {
		String verdict;
		switch (stat) {
		case PASS:
			verdict = "Passed";
			break;
		case FAIL:
			verdict = "Failed";
			break;
		case SKIPPED:
			verdict = "Blocked";
			break;
		case PERCENTFAIL:
			verdict = "Inconclusive";
			break;
		default:
			verdict = "Error";
		}
		return verdict;
	}
}
