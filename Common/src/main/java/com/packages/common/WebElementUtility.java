package com.packages.common;

import java.io.File;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * 
 * @author Amarendra
 *
 */
public class WebElementUtility {

	/**
	 * Selects the option containing {@code partialText}. This method is
	 * case-sensitive
	 * 
	 * @param select
	 * @param partialText
	 */
	public static void selectByPartOfVisibleText(Select select, String partialText) {
		selectByPartOfVisibleText(select, partialText, false);
	}

	/**
	 * Selects the option containing {@code partialText}. This method is
	 * case-insensitive
	 * 
	 * @param select
	 * @param partialText
	 */
	public static void selectByPartOfVisibleTextIgnoreCase(Select select, String partialText) {
		selectByPartOfVisibleText(select, partialText, false);
	}

	private static void selectByPartOfVisibleText(Select select, String partialText, boolean ignoreCase) {
		List<WebElement> options = select.getOptions();
		boolean matched = false;
		for (WebElement option : options) {
			String optionText = option.getText();
			if (ignoreCase) {
				optionText = optionText.toLowerCase();
				partialText = partialText.toLowerCase();
			}
			if (optionText.contains(partialText)) {
				option.click();
				matched = true;
				break;
			}
		}
		if (!matched) {
			throw new NoSuchElementException("Cannot locate element with partial text: " + partialText);
		}
	}

	public static void setValue(WebDriver driver, WebElement ele, String value) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value='" + value + "';", ele);
	}

	public static boolean isDisplayed(WebDriver driver, WebElement ele) {
		return isDisplayed(driver, ele, 10);
	}

	public static boolean isDisplayed(WebDriver driver, WebElement ele, int timeout) {
		boolean displayed = false;
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
			displayed = ele.isDisplayed();
		} catch (Exception ex) {
			displayed = false;
		} finally {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Config.DEFAULT_IMPLICIT_WAIT_TIMEOUT));
		}
		return displayed;
	}

	/**
	 * Returns parent of specified web element
	 * 
	 * @param ele
	 * @return
	 */
	public static WebElement getParent(WebElement ele) {
		return ele.findElement(By.xpath("."));
	}

	public static boolean isVerticalScrollBarPresent(WebDriver driver, WebElement ele) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (Boolean) js.executeScript("return arguments[0].scrollHeight > arguments[0].clientHeight;", ele);
	}

	public static boolean isHorizontalScrollBarPresent(WebDriver driver, WebElement ele) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (Boolean) js.executeScript("return arguments[0].scrollWidth > arguments[0].clientWidth;", ele);
	}

	public static boolean isVerticalScrollBarPresent(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (Boolean) js
				.executeScript("return document.documentElement.scrollHeight > document.documentElement.clientHeight;");
	}

	public static boolean isHorizontalScrollBarPresent(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (Boolean) js
				.executeScript("return document.documentElement.scrollWidth > document.documentElement.clientWidth;");
	}

	public static void checkCheckBox(WebElement chkbx) {
		if (!chkbx.isSelected()) {
			chkbx.click();
		}
	}

	public static void uncheckCheckBox(WebElement chkbx) {
		if (chkbx.isSelected()) {
			chkbx.click();
		}
	}

	public static boolean isAlertDisplayed(WebDriver driver) {
		boolean foundAlert = false;
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			foundAlert = true;
		} catch (Exception eTO) {
			foundAlert = false;
		}
		return foundAlert;
	}

	/**
	 * Click on Element
	 */
	public static void click(WebElement element, WebDriver driver) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	public static void selectRespondentFunctionality(WebElement element, WebDriver driver, String text) {
		/**
		 * Function to Verify Respondent Type Options
		 * 
		 * @param respondentType
		 */
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		Select select = new Select(element);
		select.selectByVisibleText(text);

	}

	public static void selectDateRangeFromCalaender(WebElement btn_TimeFrame, List<WebElement> fromCalender,
			List<WebElement> toCalender, WebDriver driver, String currentDate, String finalDate) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(btn_TimeFrame));
		WebElementUtility.click(btn_TimeFrame, driver);
		WebDriverUtility.wait(2);

		for (WebElement dateFrom : fromCalender) {
			String selectedDate = dateFrom.getText();
			if (selectedDate.equals(currentDate)) {
				dateFrom.click();
				break;
			}
		}

		for (WebElement dateTo : toCalender) {
			String selectedfinalDate = dateTo.getText();
			if (selectedfinalDate.equals(finalDate)) {
				dateTo.click();
				break;
			}

		}

	}

	public static void selectDateRangeFromCalaender1(WebElement btn_TimeFrame, String text, WebElement btn_month,
			List<WebElement> fromCalender, List<WebElement> toCalender, WebDriver driver, String currentDate,
			String finalDate) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(btn_TimeFrame));
		WebElementUtility.click(btn_TimeFrame, driver);
		WebDriverUtility.wait(2);
		Select select = new Select(btn_month);
		select.selectByVisibleText(text);
		for (WebElement dateFrom : fromCalender) {
			String selectedDate = dateFrom.getText();
			if (selectedDate.equals(currentDate)) {
				dateFrom.click();
				break;
			}
		}

		for (WebElement dateTo : toCalender) {
			String selectedfinalDate = dateTo.getText();
			if (selectedfinalDate.equals(finalDate)) {
				dateTo.click();
				break;
			}

		}
	}

	public static void selectFinalMonth(WebElement btn_TimeFrame, List<WebElement> calender, WebDriver driver,
			String finalDate) {
		// WebDriverWait wait = Utility.getWaitFor15Secs(driver);
		// wait.until(ExpectedConditions.elementToBeClickable(btn_TimeFrame));
		// WebElementUtils.click(btn_TimeFrame, driver);
		// Utility.wait(2);

		for (WebElement date : calender) {
			String selectedfinalDate = date.getText();
			if (selectedfinalDate.equals(finalDate)) {
				date.click();
			}

		}

	}

	/**
	 * Function to Enter Text in TextBox
	 * 
	 * @param driver
	 * @param txt_Name
	 * @param text
	 */
	public static void sendText(WebDriver driver, WebElement elementTxt, String text) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(elementTxt));
		elementTxt.sendKeys(text);
	}

	/**
	 * Function to Select Locations
	 * 
	 * @param driver
	 * @param btn_Locations
	 * @param list_Locations
	 * @param verificationText
	 * @param WebElement
	 */
	public static void selectLocations(WebDriver driver, WebElement btn_Locations, List<WebElement> list_Locations,
			String verificationText) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		WebElementUtility.click(btn_Locations, driver);
		wait.until(ExpectedConditions.elementToBeClickable(list_Locations.get(0)));
		WebDriverUtility.wait(3);
		for (WebElement webElement : list_Locations) {
			if (webElement.getText().equals(verificationText)) {
				WebElementUtility.click(webElement, driver);
				WebDriverUtility.wait(2);
				break;
			}

		}

	}

	public static void resetLocation(WebDriver driver, WebElement btn_Locations, List<WebElement> list_Locations,
			WebElement Def_WebElement) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		WebElementUtility.click(btn_Locations, driver);
		wait.until(ExpectedConditions.elementToBeClickable(Def_WebElement));
		WebDriverUtility.wait(1);
		WebElementUtility.click(Def_WebElement, driver);
		WebDriverUtility.wait(1);
	}

	public static void resetLocationforChangedLocation(WebDriver driver, WebElement btn_UserCompany,
			WebElement btn_CompanyChange, String VarificationText, List<WebElement> List_Company) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(btn_CompanyChange));
		WebElementUtility.click(btn_UserCompany, driver);
		WebDriverUtility.wait(2);
		WebElementUtility.click(btn_CompanyChange, driver);

		for (WebElement comp : List_Company) {
			if (VarificationText.equals(comp.getText()))
				comp.click();
		}
	}

	/**
	 * Function to SelectChanged Company
	 * 
	 * @param btn_User
	 * @param crt_Company
	 * @param text
	 * @param driver
	 * @param btn_CompanyChange
	 * @param btn_New_CompanyName
	 */
	public static void selectCompanyChangedFunctionality(WebElement btn_User, WebElement crt_Company, String text,
			WebDriver driver, WebElement btn_CompanyChange, WebElement btn_New_CompanyName) {
		WebElementUtility.click(btn_User, driver);
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(crt_Company));
		String varText[] = text.split(",");
		if (crt_Company.getText().equals(varText[0])) {
			WebDriverUtility.wait(2);
			WebElementUtility.click(btn_CompanyChange, driver);
			System.out.println("click_btn");
			WebDriverUtility.wait(2);
			if (btn_New_CompanyName.getText().equals(varText[1])) {
				WebElementUtility.click(btn_New_CompanyName, driver);
			}
			WebDriverUtility.wait(5);
			System.out.println("New Company " + btn_New_CompanyName.getText());

		}
	}

	/**
	 * Function to Select Analysis filtered By Survey
	 * 
	 * @param driver
	 * @param element
	 * @param verificationText
	 */
	public static void selectAnalysisFilterBySurvry(WebDriver driver, WebElement element, String verificationText) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		String[] respontentOptions = verificationText.split(",");
		Select select = new Select(element);
		List<WebElement> options = select.getOptions();
		int i = 0;
		for (WebElement webElement : options) {
			if (webElement.getText().equals(respontentOptions[i]))
				System.out.println(webElement.getText());
			break;
		}
		i++;

	}

	/**
	 * Function to Select Business Statistics
	 * 
	 * @param driver
	 * @param list_BusinessStatistics
	 */
	public static void selectBusinessStatisticsData(WebDriver driver, List<WebElement> list_BusinessStatistics) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.visibilityOf(list_BusinessStatistics.get(0)));
		for (WebElement element : list_BusinessStatistics) {
			System.out.println(element.getText() + " ");
		}

	}

	/**
	 * Function to Select AnalysisList Survey
	 * 
	 * @param driver
	 * @param list_element
	 * @param text
	 * @param element
	 */
	public static void selectAnalysisListSurvey(WebDriver driver, List<WebElement> list_element, String text,
			WebElement element) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.visibilityOf(list_element.get(0)));
		String[] txt = text.split(",");
		for (WebElement ele : list_element)
			if (ele.getText().equals(txt[2])) {
				System.out.println(element.getText());
				break;
			}

	}

	/**
	 * Funation to select Overview Date
	 * 
	 * @param driver
	 * @param ListData_Overview_Appearnce
	 */
	public static void selectOverviewData(WebDriver driver, List<WebElement> ListData_Overview_Appearnce) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		WebDriverUtility.wait(2);
		wait.until(ExpectedConditions.visibilityOfAllElements(ListData_Overview_Appearnce));
		for (WebElement element : ListData_Overview_Appearnce) {
			System.out.println(element.getText());
		}

	}

	public static void selectRespondentTypeFunctionalitySurveyResult(WebDriver driver, WebElement drp_AllRespondentType,
			List<WebElement> list_drpAllRespondentType, String verificationText) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.visibilityOf(drp_AllRespondentType));
		WebElementUtility.click(drp_AllRespondentType, driver);
		List<WebElement> allOptions = list_drpAllRespondentType;
		System.out.println(allOptions.size());
		for (WebElement option : allOptions) {
			if (option.getText().equals(verificationText)) {
				option.click();
				break;

			}
		}
	}

	public static int verifySurveyCount(WebDriver driver, WebElement drp_AllRespondentType,
			List<WebElement> Count_SurveyAllRespondent) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.elementToBeClickable(Count_SurveyAllRespondent.get(0)));
		return Count_SurveyAllRespondent.size();
	}

	public static void displayTextToDrpdown(WebDriver driver, WebElement drp_AllRespondentType,
			List<WebElement> list_drpAllRespondentType) {
		WebDriverWait wait = WebDriverUtility.getWaitFor15Secs(driver);
		wait.until(ExpectedConditions.visibilityOf(drp_AllRespondentType));
		WebElementUtility.click(drp_AllRespondentType, driver);
		List<WebElement> allOptions = list_drpAllRespondentType;
		for (WebElement option : allOptions) {

			Assert.assertTrue(option.isDisplayed(), option.getText() +"Element is not Displayed");
		}

	}

	/**
	 * Function to Convert RGB to HEX color Code
	 * 
	 * @param input
	 * @return
	 */
	public static String covertRGBtoHEX(String input) {
		String output = input.substring(input.indexOf('(') + 1, input.indexOf(')'));
		String[] out = output.split(",");
		String hexColor = String.format("#%02x%02x%02x", Integer.parseInt(out[0].trim()),
				Integer.parseInt(out[1].trim()), Integer.parseInt(out[2].trim()));
		return hexColor;
	}

	public static boolean isFileDownloaded(String downloadPath, String fileName) {
		File file = new File(downloadPath);
		File[] fileContents = file.listFiles();
		for (int i = 0; i < fileContents.length; i++) {
			if (fileContents[i].getName().equals(fileName)) {
				return true;
			}
		}
		return false;
	}

}
