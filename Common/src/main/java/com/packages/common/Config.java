package com.packages.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.Reporter;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author Amarendra
 */
public class Config {

	private static Properties props;
	public static WebDriver defaultDriver;



	public final static String configFile = "../Properties.xml";
	public static final int DEFAULT_IMPLICIT_WAIT_TIMEOUT = 20;

	private static final Logger logger = Logger.getLogger(Config.class);

	static {
		try {
			init();
		} catch (IOException e) {
			logger.error("Unable to load configuration",  e);
			//System.exit(1);
		}
	}

	private static void init() throws IOException {
		props = new Properties();
		InputStream is = new FileInputStream(configFile);
		props.loadFromXML(is);
	}

	/**
	 * Returns configuration value corresponding to specified key
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public static String getMYSQLServer() {
		return getValue("mysql.server");
	}

	public static String getMYSQLUser() {
		return getValue("mysql.user");
	}

	public static String getMYSQLPassword() {
		return getValue("mysql.password");
	}

	public static String getMYSQLPort() {
		return getValue("mysql.port");
	}

	public static String getMYSQLDB() {
		return getValue("mysql.database");
	}
	
	public static String getMYSQLDBTestLink() {
		return getValue("mysql.database.testlink");
	}

	public static String getMedrevUserId() {
		return getValue("MedRev.test.email");
	}

	public static String getMedrevPassword() {
		return getValue("MedRev.test.pass");
	}
	
	public static String getTestRailUser() {
		return getValue("testRail.user");
	}

	public static String getTestRailPassword() {
		return getValue("testRail.password");
	}

	public static String getTestRailUrl() {
		return getValue("testRail.url");
	}


	public static String getGAMILHost() {
		return getValue("MedRev.test.gmail.host");
	}
	
	public static String getGAMILSentHost() {
		return getValue("MedRev.test.gmail.sent.host");
	}

	public static String getGMAILPort() {
		return getValue("MedRev.test.gmail.port");
	}
	
	public static String getGMAILSentPort() {
		return getValue("MedRev.test.gmail.sent.port");
	}
	
	public static String getGMAILUser() {
		return getValue("MedRev.test.gmail.username");
	}
	
	public static String getGMAILPassword() {
		return getValue("MedRev.test.gmail.password");
	}
	
	public static String getMedRevProdName() {
		return getValue("product.MedRev");
	}

	public static String getmailRecipentMedRev() {
		return getValue("MedRev.mail.recipient");
	}

	/**
	 * Returns web driver for firefox.
	 * 
	 * @return
	 */
	public static WebDriver getDriver() {
		return getDriver(null);
	}

	/**
	 * Returns web driver for specified <code>browser</code>. In case of invalid
	 * <code>browser</code>, returns web driver for firefox.
	 * 
	 * @param browser
	 *            browser name
	 * @return
	 */
	public static WebDriver getDriver(String browser) {
		WebDriver driver = null;

		if (browser == null) {
			browser = "";
		}

		File driverFilePath = null;
		switch (browser.toLowerCase()) {
		case "chrome":
			// set path to chromedriver.exe You may need to download it from
			// http://code.google.com/p/selenium/wiki/ChromeDriver

			driverFilePath = new File("..\\chromedriver_win32\\chromedriver.exe");
			try {
				System.setProperty("webdriver.chrome.driver", driverFilePath.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	        //ChromeOptions chromeOptions = new ChromeOptions();
	       // chromeOptions.setAcceptInsecureCerts(true);

			Reporter.log("Opening Chrome browser");
			// create ChromeDriver instance
	        driver = new ChromeDriver();
			break;
		case "ie":
			// set path to IEdriver.exe You may need to download it from
			// 32 bRPG
			// http://selenium-release.storage.googleapis.com/3.4.0/IEDriverServer_Win32_2.42.0.zip
			// 64 bRPG
			// http://selenium-release.storage.googleapis.com/3.4.0/IEDriverServer_x64_2.42.0.zip
			driverFilePath = new File(".\\IEDriverServer_Win32_3.4.0\\IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", driverFilePath.getAbsolutePath());

			InternetExplorerOptions ieOptions = new InternetExplorerOptions();
			ieOptions.setAcceptInsecureCerts(true);
			// Hoping this might fix frequently occurring StaleElementReferenceException in IE
			ieOptions.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
			ieOptions.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);

			Reporter.log("Opening Internet Explorer");
			driver = new InternetExplorerDriver(ieOptions);
			break;
		default:
			//DesiredCapabilities dCapFF = DesiredCapabilities.firefox();
			//driverFilePath = new File("..\\GeekoDriver\\geckodriver.exe");
			/*
			 * try { System.setProperty("webdriver.gecko.driver",
			 * driverFilePath.getCanonicalPath()); } catch (IOException e) {
			 * e.printStackTrace(); }
			 */
			//dCapFF.setCapability("marionette", true);
			//dCapFF.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
			WebDriverManager.firefoxdriver().setup();
			//Reporter.log("Opening Firefox browser");
			driver = new FirefoxDriver();
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_IMPLICIT_WAIT_TIMEOUT));
		driver.manage().window().maximize();
		defaultDriver = driver;
		return driver;
	}

	public static WebDriver getDefaultDriver() {
		return defaultDriver;
	}

	public static void setDefaultDriver(WebDriver driver) {
		defaultDriver = driver;
	}
	

	public static WebDriver getDriverMedRev(String browser) {
		WebDriver driver = getDriver(browser);
		String url = Config.getMedRevURL();
		Reporter.log("Navigating to URL: " + url);
		driver.get(url);
		return driver;
	}
	
	public static WebDriver getDriverCrossScriptingTest(String browser, String url) {
		WebDriver driver = getDriver(browser);
		Reporter.log("Navigating to URL: " + url);
		driver.get(url);
		return driver;
	}

	public static String getMedRevURL() {
		return getValue("MedRev.test.server.url");
	}
	

	public static String getSqlProfile() {
		return getValue("medrev.test.profile");
	}

	public static String getMSITargetPath() {
		return getValue("medrev.test.exe.location");
	}
	
	public static String getScreenShotsRootLocation() {
		return getValue("screenshots.rootLocation");
	}

}
