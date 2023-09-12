package com.packages.common;

import java.sql.Connection;
import java.time.Duration;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * 
 * @author Amarendra
 *
 */
public class CustomExpectedConditions {

	/**
	 * An expectation to execute a scalar query and return the result or
	 * null if result is empty or null.
	 * 
	 * @param conn
	 * @param sqlQuery
	 * @return
	 */
	public static ExpectedCondition<String> executeScalarQuery(final Connection conn, final String sqlQuery) {
		return new ExpectedCondition<String>() {
			public String apply(WebDriver driver) {
				String result = SQLDBConnectHelper.executeScalar(conn, sqlQuery);
				if (StringUtils.isNotEmpty(result)) {
					return result;
				}
				return null;
			}
		};
	}

	/**
	 * An expectation for checking that an element is either invisible or not
	 * present on the DOM.
	 *
	 * @param element used to find the element
	 * @return true if the element is not displayed or the element doesn't exist or stale element
	 */
	public static ExpectedCondition<Boolean> invisibilityOfElement(
			final WebElement element) {
		return invisibilityOfElement(element, 3);
	}

	/**
	 * An expectation for checking that an element is either invisible or not
	 * present on the DOM.
	 *
	 * @param element used to find the element
	 * @param timeToWait time in seconds to wait before considering the element as invisible
	 * @return true if the element is not displayed or the element doesn't exist or stale element
	 */
	public static ExpectedCondition<Boolean> invisibilityOfElement(
			final WebElement element, final int timeToWait) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				try {
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeToWait));
					return !(element.isDisplayed());
				} catch (NoSuchElementException e) {
					// Returns true because the element is not present in DOM. The
					// try block checks if the element is present but is invisible.
					return true;
				} catch (StaleElementReferenceException e) {
					// Returns true because stale element reference implies that element
					// is no longer visible.
					return true;
				} finally {
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Config.DEFAULT_IMPLICIT_WAIT_TIMEOUT));
				}
			}
		};
	}

	/**
	 * An expectation for checking whether the given frame is available to
	 * switch to.
	 * <p>
	 * If the frame is available it switches the given driver to the specified
	 * frameIndex.
	 *
	 * @param frameLocator
	 *            used to find the frame (index)
	 * @return WebDriver instance after frame has been switched
	 */
	public static ExpectedCondition<WebDriver> frameToBeAvailableAndSwitchToIt(
			final int frameLocator) {
		return new ExpectedCondition<WebDriver>() {
			public WebDriver apply(WebDriver driver) {
				try {
					return driver.switchTo().frame(frameLocator);
				} catch (NoSuchFrameException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "frame to be available: " + frameLocator;
			}
		};
	}

	public static ExpectedCondition<WebDriver> windowTitleIs(
			final String winTitle) {
		return new ExpectedCondition<WebDriver>() {
			public WebDriver apply(WebDriver driver) {
				try {
					Set<String> winHandles = driver.getWindowHandles();
					for (String handle : winHandles) {
						WebDriver winHandle = driver.switchTo().window(handle);
						if (winHandle.getTitle().equals(winTitle)) {
							driver.manage().window().maximize();
							return winHandle;
						}
					}
				} catch(UnhandledAlertException uaex) {
					return null;
				}
				return null;
			}
		};
	}

	public static ExpectedCondition<WebDriver> windowTitleContains(
			final String winTitle) {
		return new ExpectedCondition<WebDriver>() {
			public WebDriver apply(WebDriver driver) {
				try {
					Set<String> winHandles = driver.getWindowHandles();
					for (String handle : winHandles) {
						WebDriver winHandle = driver.switchTo().window(handle);
						if (winHandle.getTitle().contains(winTitle)) {
							return winHandle;
						}
					}
				} catch(UnhandledAlertException uaex) {
					try {
						driver.switchTo().alert().dismiss();
					} catch (Exception ex) {}
					return null;
				}
				return null;
			}
		};
	}

	public static ExpectedCondition<WebDriver> closeWindowExcept(
			final String winHandle) {
		return new ExpectedCondition<WebDriver>() {
			public WebDriver apply(WebDriver driver) {
				try {
					Set<String> winHandles = driver.getWindowHandles();
					winHandles.remove(winHandle);
					for (String handle : winHandles) {
						driver.switchTo().window(handle);
						driver.close();
					}
					if (winHandles.isEmpty()) {
						return driver.switchTo().window(winHandle);
					}
				} catch(UnhandledAlertException uaex) {
					return null;
				}
				return null;
			}
		};
	}

	public static ExpectedCondition<Boolean> attributeContains(
			final WebElement ele, final String attrName, final String expectedAttrVal) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				boolean found = false;
				String attrVal = ele.getAttribute(attrName);
				if (StringUtils.isNotEmpty(attrVal) && attrVal.contains(expectedAttrVal)) {
					found = true;
				}
				return found;
			}
		};
	}

	public static ExpectedCondition<Boolean> isTextPresent(final WebElement ele) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return StringUtils.isNotEmpty(ele.getText());
			}
		};
	}

	public static ExpectedCondition<Boolean> isTextEqualsIgnoreCase(
			final String expectedText, final WebElement ele) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ele.getText().equalsIgnoreCase(expectedText);
			}
		};
	}
}
