package com.packages.common;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

/**
 * 
 * @author Amarendra
 *
 */
public class ListUtils {

	/**
	 * Checks if the list contains specified string, ignoring the case.
	 * @param list
	 * @param str
	 * @return
	 */
	public static boolean checkIfListContainsIgnoreCase(List<String> list, String str) {
		boolean found = false;
		if (indexOfIgnoreCase(list, str) > -1) {
			found = true;
		}
		return found;
	}

	public static int indexOfIgnoreCase(List<String> list, String str) {
		int idx = -1;
		int i = 0;
		for (; i < list.size(); i++) {
			if (list.get(i).equalsIgnoreCase(str)) {
				idx = i;
				break;
			}
		}
		return idx;
	}

	/**
	 * Checks if the list contains specified sub-string
	 * @param list
	 * @param subString
	 * @return
	 */
	public static boolean checkIfListContainsSubString(List<String> list, String subString) {
		return (firstIndexOfSubString(list, subString) > -1 ? true : false);
	}

	/**
	 * Returns index of first occurrence of string containing specified
	 * {@code subString}
	 * 
	 * @param list
	 * @param subString
	 * @return
	 */
	public static int firstIndexOfSubString(List<String> list, String subString) {
		int idx = -1;
		int i = 0;
		for (; i < list.size(); i++) {
			if (list.get(i).contains(subString)) {
				idx = i;
				break;
			}
		}
		return idx;
	}

	/**
	 * Checks if the list contains specified sub-string, ignoring the case
	 * @param list
	 * @param str
	 * @return
	 */
	public static boolean checkIfListContainsSubStringIgnoreCase(List<String> list, String subString) {
		boolean found = false;
		for (String str : list) {
			found = str.toLowerCase().contains(subString.toLowerCase());
			if (found) {
				break;
			}
		}
		return found;
	}

	/**
	 * Checks if each item in list contains specified sub-string, ignoring the case
	 * @param list
	 * @param str
	 * @return
	 */
	public static boolean checkIfEachListItemContainsSubStringIgnoreCase(List<String> list, String subString) {
		boolean found = false;
		for (String str : list) {
			found = str.toLowerCase().contains(subString.toLowerCase());
			if (!found) {
				break;
			}
		}
		return found;
	}

	public static List<String> getListOfTexts(List<WebElement> eleList) {
		List<String> texts = new ArrayList<String>();
		for (WebElement ele : eleList) {
			texts.add(ele.getText().trim());
		}
		return texts;
	}
}
