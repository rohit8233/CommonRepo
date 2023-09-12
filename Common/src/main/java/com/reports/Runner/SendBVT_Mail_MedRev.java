package com.reports.Runner;

import java.io.IOException;

import com.packages.common.Config;
import com.packages.common.Constants;

public final class SendBVT_Mail_MedRev {
	public static void main(String[] args) throws IOException {
		new ReportMailer(Config.getMedRevURL(), Constants.SCREENSHOT_FOLDER_NAME_EADMIN,
				Config.getMedRevProdName()).emailReport(
						Config.getmailRecipentMedRev(), Constants.MAIL_SUBJECT_MEDREV);
	}
}
