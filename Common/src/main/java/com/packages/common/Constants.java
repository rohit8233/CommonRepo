package com.packages.common;

/**
 * 
 * @author Amarendra
 *
 */
public final class Constants {

	private Constants() {}

	public static final String TEST_PROJECT_NAME = "MedRev_PORTAL";
	public static final String TEST_TESTPLAN = "MedRev_Test_Plan";
	public static final String TEST_BUILD = "MedRev_Build_1";
	public static final String SCREENSHOT_FOLDER_NAME_EADMIN = "MedRev";
	public static final String MAIL_SUBJECT_MEDREV = "MedRev - BVT";

	public static final String BACKGROUND_COLOR_PASS = "#A9F5A9";
	public static final String BACKGROUND_COLOR_FAIL = "#F6CECE";
	public static final String BACKGROUND_COLOR_BLOCKED = "#F2F5A9";
	public static final String TEST_CASE_PARTIAL_URL = "https://jhmedia.atlassian.net/browse/";
	public static final String TEST_EXECUTION_LINK = "https://jhmedia.atlassian.net/browse/MED-698";
	public static final String BUILD_NUMBER = "MedRev_Build_1";
	public static final String SURVEY_COMMENTS = "It was very good Experience";
	public static final String SURVEY_COMMENTS_NEGATIVE = "Nightmare";
	public static enum Rating {
		FIVESTAR,
		FOURSTAR,
		THREESTAR,
		TWOSTAR,
		ONESTAR
		}
	
	
	public final class Query {

		private Query() {}

		public static final String SELECT_TEST_SUITES_FOR_PRODUCT = "SELECT testsuite_id, testsuite_name "
				+ "FROM test_set WHERE prod_name=?";

		public static final String STATUS_COUNT = "SELECT count(*) FROM execution_status WHERE testcase_status=? "
				+ "AND testsuite_id=? AND DATE(execution_date) = DATE(NOW()) AND prod_name=?";

		public static final String SELECT_ALL_FROM_EXECUTION_STATUS_FOR_PRODUCT =
				"SELECT * FROM execution_status WHERE testsuite_id=? AND DATE(execution_date) = DATE(NOW()) AND prod_name=?";

		public static final String SELECT_LAST_N_STATUS_FOR_TCID =
				"SELECT testcase_status, execution_date, build FROM execution_status WHERE testcase_id = ? ORDER BY execution_date DESC LIMIT ?";
	}

}
