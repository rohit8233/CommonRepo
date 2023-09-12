package com.packages.common;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

/**
 * 
 * @author Amarendra
 * 
 */
public class WebDriverUtility {

	/**
	 * Method to switch to window with specified {@code title}
	 * @param title
	 * @param driver
	 * @return
	 */
	public static WebDriver switchToWindow(String title, WebDriver driver) {
		Reporter.log("Switching to new window");
		waitForNewWindowToOpen();
		WebDriverWait wait = getWaitFor1Mins(driver);
		return wait.until(CustomExpectedConditions.windowTitleIs(title));
	}

	public static WebDriver switchToWindowTitleContains(String title, WebDriver driver) {
		waitForNewWindowToOpen();
		WebDriverWait wait = getWaitFor1Mins(driver);
		return wait.until(CustomExpectedConditions.windowTitleContains(title));
	}

	/**
	 * WaRPG for a frame to be available and switches to it.
	 * @param id frame id
	 * @param driver
	 * @return webdriver after switching to iframe
	 */
	public static WebDriver switchToIFrame(String id, WebDriver driver) {
		WebDriverWait wait = getWaitFor30Secs(driver);
		return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(id)));
	}

	/**
	 * WaRPG for a frame at specified index to be available and switches to it.
	 * @param idx frame index
	 * @param driver
	 * @return webdriver after switching to iframe
	 */
	public static WebDriver switchToIFrameByIndex(int idx, WebDriver driver) {
		WebDriverWait wait = getWaitFor30Secs(driver);
		return wait.until(CustomExpectedConditions.frameToBeAvailableAndSwitchToIt(idx));
	}

	/**
	 * WaRPG for a frame to be available and switches to it.
	 * @param id frame id
	 * @param driver
	 * @return webdriver after switching to iframe
	 */
	public static WebDriver switchToIFrameByClass(String className, WebDriver driver) {
		WebDriverWait wait = getWaitFor30Secs(driver);
		Reporter.log("Switching to an iframe by classname");
		return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.className(className)));
	}

	/**
	 * WaRPG for an alert to present and switches to it
	 * @param driver
	 * @return alert after switching to it
	 */
	public static Alert switchToAlert(WebDriver driver) {
		wait(3);
		WebDriverWait wait = getWaitFor30Secs(driver);
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	/**
	 * Method to close alert by dismissing it. This method doesn't guarantee
	 * closing of alert as alert handling is driver specific.
	 * 
	 * @param driver
	 * @return true if alert was closed, false otherwise
	 */
	public static boolean closeAlert(WebDriver driver) {
		boolean alertClosed = false;
		try {
			Alert alert = switchToAlert(driver);
			alert.dismiss();
			alertClosed = true;
		} catch(Exception ex) {}
		return alertClosed;
	}

	/**
	 * Method to close alert by accepting it. This method doesn't guarantee
	 * closing of alert as alert handling is driver specific.
	 * 
	 * @param driver
	 * @return true if alert was closed, false otherwise
	 */
	public static boolean acceptAlert(WebDriver driver) {
		boolean alertClosed = false;
		try {
			Alert alert = switchToAlert(driver);
			alert.accept();
			alertClosed = true;
		} catch(Exception ex) {}
		return alertClosed;
	}

	public static void wait(int timeInSecs) {
		try {
			Thread.sleep(timeInSecs * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to Switch to New Window
	 * 
	 * @param driver
	 */
	public static String switchToNewWindow(WebDriver driver) {
		waitForNewWindowToOpen();
		// Store the current window handle
		String winHandleBefore = driver.getWindowHandle();

		// Switch to new window opened
		Set<String> winHandles = driver.getWindowHandles();
		driver.switchTo().window((String) winHandles.toArray()[winHandles.size() - 1]);
		//driver.manage().window().maximize();
		return winHandleBefore;
	}

	/**
	 * Function to Switch to New Window
	 * <p>
	 * <b>NOTE: </b>Usually window handle for newly opened window in
	 * {@link WebDriver#getWindowHandles()} appears at last, but due to some
	 * unexpected behavior of Internet Explorer, sometimes it appears at
	 * beginning
	 * </p>
	 * 
	 * @param driver
	 * @param winHandlesBefore
	 *            set of window handles before new window is opened
	 */
	public static void switchToNewWindow(WebDriver driver, Set<String> winHandlesBefore) {
		waitForNewWindowToOpen();
		// Switch to new window opened
		Set<String> winHandlesAfter = driver.getWindowHandles();
		winHandlesAfter.removeAll(winHandlesBefore);
		driver.switchTo().window((String) winHandlesAfter.toArray()[0]);
		driver.manage().window().maximize();
	}

	/**
	 * Function to Switch to Parent Window
	 * 
	 * @param winHandleBefore
	 */
	public static void switchtoParentWindow(WebDriver driver,
			String winHandleBefore) {
		// Close the new window, if that window no more required
		driver.close();

		// Switch back to original browser (first window) and Continue with
		// original browser (first window)
		Reporter.log("Switching to new window");
		driver.switchTo().window(winHandleBefore);
	}

	/**
	 * Method to return wait object with 15 Seconds timeout.
	 * 
	 * @param driver
	 * @return
	 */
	public static WebDriverWait getWaitFor15Secs(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		return wait;
	}

	/**
	 * Method to return wait object with 30 Seconds timeout.
	 * 
	 * @param driver
	 * @return
	 */
	public static WebDriverWait getWaitFor30Secs(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(30));
		return wait;
	}

	/**
	 * Method to return wait object with 1 Minutes timeout.
	 * 
	 * @param driver
	 * @return
	 */
	public static WebDriverWait getWaitFor1Mins(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		return wait;
	}

	/**
	 * Method to return wait object with 2 Minutes timeout.
	 * 
	 * @param driver
	 * @return
	 */
	public static WebDriverWait getWaitFor2Mins(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(2 * 60));
		return wait;
	}

	/**
	 * Method to return wait object with 5 Minutes timeout.
	 * 
	 * @param driver
	 * @return
	 */
	public static WebDriverWait getWaitFor5Mins(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(3 * 60));
		return wait;
	}

	/**
	 * Method to return wait object with 10 Minutes timeout.
	 * 
	 * @param driver
	 * @return
	 */
	public static WebDriverWait getWaitFor10Mins(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(10 * 60));
		return wait;
	}

	/**
	 * When a new window/tab is opened, it is possible that handle is not yet
	 * added to driver instance. This method is to give new window/tab some time
	 * to attach to driver instance.
	 */
	public static void waitForNewWindowToOpen() {
		wait(5);
	}

	/**
	 * 
	 * @param d
	 * @param days
	 * @return
	 */

	public static Date addDays(Date d, long days)
	{
		Reporter.log("Adding Dayes to Current day");
		d.setTime(d.getTime() + days * 1000 * 60 * 60 * 24);
		return d;
	}
	/**
	 * 
	 * @param noOfDaysDifference
	 * @return
	 */
	public static int[] getDateAndMonth(long noOfDaysDifference){
		Reporter.log("Gettign Day and Month from Calender");
		int[] arrayOfMonthAndDate = new int[2];
		Date date = new Date();
		Reporter.log("Setting Start Time");
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(date);
		Calendar endCalendar = new GregorianCalendar();
		Date now1 = new Date();
		Reporter.log("Setting End Time");
		Date enddate = addDays(now1, noOfDaysDifference);
		endCalendar.setTime(enddate);
		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		arrayOfMonthAndDate[0] = diffMonth;
		arrayOfMonthAndDate[1] = endCalendar.get(Calendar.DATE);
		return arrayOfMonthAndDate;
	}
	
	public static void refresh(WebDriver driver) {
		driver.navigate().refresh();
	}

	/**
	 * Closes all windows except window with {@code windowHandle} and switches to it
	 * @param driver
	 * @param windowHandle
	 * @return
	 */
	public static WebDriver closeAllButThis(WebDriver driver, String windowHandle) {
		WebDriverWait wait = WebDriverUtility.getWaitFor30Secs(driver);
		driver = wait.until(CustomExpectedConditions.closeWindowExcept(windowHandle));
		// A kind of hack for IE. After switching back to window, click doesn't work sometimes.
		WebElement body = driver.findElement(By.tagName("body"));
		WebDriverUtility.getWaitFor30Secs(driver).until(ExpectedConditions.elementToBeClickable(body));
		body.click();
		return driver;
	}

	/**
	 * Method to refresh current document only, and not the whole window. This
	 * can be used to refresh a frame. To do so, first switch to that frame and
	 * then call this method
	 * 
	 * @param driver
	 */
	public static void refreshCurrentDocument(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.location.reload(true);");
	}
	
}
