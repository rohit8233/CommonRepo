package com.reports.Runner;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import com.packages.common.Config;
import com.packages.common.Constants;
import com.packages.common.TestResults.MYSQLHelper;


/**
 * 
 * @author Amarendra
 *
 */
public class ReportMailer {

	private String testUrl;
	private String failedScreenShotsFolder;
	private String productName;
	String testcaseId;
	String  executionLink ;
	String feature_id;

	private MYSQLHelper mysql;

	public ReportMailer(String testUrl, String failedScreenShotsFolder, String productName) {
		this.testUrl = testUrl;
		this.failedScreenShotsFolder = failedScreenShotsFolder;
		this.productName = productName;
		this.mysql = new MYSQLHelper();
	}

	public void emailReport(String recipients, String subject) {
		mysql.connectToMYSQL();
		List<String[]> listOfTestSet = mysql.getTestSetName(productName);
		pichartReport(listOfTestSet);
		String emailBody = formatSummary(listOfTestSet);
		emailBody += formatReportBody(listOfTestSet);

		sendMail(recipients, subject, emailBody);
	}

	public void pichartReport(List<String[]> listOfTestSet) {
		long totalPassedCount = 0;
		long totalFailedCount = 0;
		long totalBlockedCount = 0;
		for (String[] testsetList : listOfTestSet) {
			long passedCount = mysql.getStatusCount("Passed", testsetList[0].toString(), productName);
			long failedCount = mysql.getStatusCount("Failed", testsetList[0].toString(), productName);
			long blockedCount = mysql.getStatusCount("Blocked", testsetList[0].toString(), productName);
			totalPassedCount += passedCount;
			totalFailedCount += failedCount;
			totalBlockedCount += blockedCount;
		}
		// Load google charts
		DefaultPieDataset dataset = new DefaultPieDataset( );
		dataset.setValue("Failed("+ totalFailedCount +")", totalFailedCount);
		//dataset.setValue("Hold("+ 2 +")", 2 );
		dataset.setValue("Passed(" + totalPassedCount +")", totalPassedCount );
		dataset.setValue("Blocked("+ totalBlockedCount +")", totalBlockedCount );


		JFreeChart chart = ChartFactory.createPieChart3D(
				"",   // chart title
				dataset,          // data
				true,             // include legend
				true,
				false);
		PiePlot3D plot = ( PiePlot3D ) chart.getPlot( );             
		plot.setStartAngle( 270 );             
		plot.setForegroundAlpha( 0.60f );             
		plot.setInteriorGap( 0.02 );
		plot.setSectionPaint("Failed("+ totalFailedCount +")", new Color(246,206,206));
		//plot.setSectionPaint("Hold("+ 2 +")", Color.MAGENTA);
		plot.setSectionPaint("Passed(" + totalPassedCount +")", new Color(169,245,169));
		plot.setSectionPaint("Blocked("+ totalBlockedCount +")", new Color(242,245,169));
		chart.setBorderVisible(false);
		chart.getPlot().setOutlinePaint(Color.WHITE);
		chart.getPlot().setBackgroundPaint(Color.WHITE);
		int width = 500;   /* Width of the image */
		int height = 250;  /* Height of the image */ 
		File pieChart = new File( "PieChart.jpeg" ); 
		try {
			if (pieChart.exists()) {
				pieChart.delete();
				pieChart.createNewFile();
			}

			ChartUtilities.saveChartAsJPEG(pieChart , chart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendMail(String recipients, String subject, String emailBody) {
		final String username = Config.getGMAILUser();
		final String password = Config.getGMAILPassword();
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", Config.getGAMILSentHost());
		props.put("mail.smtp.port", Config.getGMAILSentPort());
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.user", username);
		props.put("mail.password", password);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			BodyPart messageBodyPartSummery = new MimeBodyPart();
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));

			message.setSubject(subject);
			messageBodyPartSummery.setContent(emailBody, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPartSummery);
			// second part (the image)
			messageBodyPartSummery = new MimeBodyPart();
			DataSource fds = new FileDataSource(
					"PieChart.jpeg");

			messageBodyPartSummery.setDataHandler(new DataHandler(fds));
			messageBodyPartSummery.setHeader("Content-ID", "<image>");

			multipart.addBodyPart(messageBodyPartSummery);

			message.setContent(multipart);
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			System.out.println(e);
		}
	}

	private String formatSummary(List<String[]> listOfTestSet) {
		String failedScreenShotsURL = null;
		if (failedScreenShotsFolder != null && !failedScreenShotsFolder.trim().isEmpty()) {
			failedScreenShotsURL = Config.getScreenShotsRootLocation();
			if (failedScreenShotsURL.endsWith("\\")) {
				failedScreenShotsURL += "\\";
			}
			failedScreenShotsURL += failedScreenShotsFolder;
		}
		long totalPassedCount = 0;
		long totalFailedCount = 0;
		long totalBlockedCount = 0;
		String reportSummary = "";

		reportSummary = "<font size='4'><b>Execution Status On :</b> " + testUrl + " </font><br>";
		if (failedScreenShotsURL != null) {
			reportSummary +=
					"<font size='2'><b>Failed Screen Shots Location : </b><code>" + failedScreenShotsURL + "</code></font><br>";
		}
		/*reportSummary += "<p> <hr /> <p>"
				+ "<tr><td align=bottom><img  src=\"cid:image\" width='500' height='500'></td></tr>";*/
		reportSummary +=
				"<br><table><tr><td align='top'><table><tr><td><b>Feature</b></td><td><b>PASS</b></td><td><b>FAIL</b></td><td><b>BLOCKED</b></td></tr>";
		for (String[] testsetList : listOfTestSet) {
			long passedCount = mysql.getStatusCount("Passed", testsetList[0].toString(), productName);
			long failedCount = mysql.getStatusCount("Failed", testsetList[0].toString(), productName);
			long blockedCount = mysql.getStatusCount("Blocked", testsetList[0].toString(), productName);

			reportSummary += "<tr><td><b>" + testsetList[1] + "</b></td><td style=\"background-color:#A9F5A9\">"
					+ passedCount + "</td><td style=\"background-color:#F6CECE\">" + failedCount
					+ "</td><td style=\"background-color:#F2F5A9\">" + blockedCount + "</td></tr>";
			totalPassedCount += passedCount;
			totalFailedCount += failedCount;
			totalBlockedCount += blockedCount;
		}		
		reportSummary += "<tr><td><b>Total</b></td><td style=\"background-color:#A9F5A9\"><font size='6'><b>"
				+ totalPassedCount + "</b></font></td>";
		reportSummary += "<td style=\"background-color:#F6CECE\"><font size='6'><b>" + totalFailedCount
				+ "</b></font></td>";
		reportSummary += "<td style=\"background-color:#F2F5A9\"><font size='6'><b>" + totalBlockedCount
				+ "</b></font></td></tr></table></td><td align=bottom><img  src=\"cid:image\"></td></tr></table><p>";

		return reportSummary;
	}

	private String formatReportBody(List<String[]> listOfTestSet) {
		// width = 650px, height= 400px 
		String reportBody = "<tabel><tr><td style=\"overflow: scroll\"><table border='0' bgcolor='#545b62'>";
		reportBody += "<tr><td><b><center><font color='white'>Verdict</font></center></b></td><td>"
				+ "<b><center><font color='white'>TCID</font></center></b></td>"
				+ "<b><center><font color='white'>TSID</font></center></b></td>";
		reportBody += "<td><b><center><font color='white'>TC Name</font></center></b></td>";
		reportBody += "<td><b><center><font color='white'>Build</font></center></b></td>";
		reportBody += "<td><b><center><font color='white'>ProductName</font></center></b></td>";
		reportBody += "<td><b><center><font color='white'>Notes</font></center></b></td>";
		reportBody += "<td><b><center><font color='white'>ExecutionTime</font></center></b></td>";
		reportBody += "<td><b><center><font color='white'>ExecutionDate</font></center></b></td></tr>";

		String grandTotalHTML = "";
		String displayVerdict = "";
		String rowBgColor = "";
		for (String[] testsetList : listOfTestSet) {
			List<String[]> testDetailsResult = mysql.getDetailedStatus(testsetList[0].toString(), productName);
			for (String[] row : testDetailsResult) {
				String tcid = row[0];
				String tsid = row[1];
				String verdict = row[2];
				String build = row[3];
				String notes = row[4];
				notes = notes.replace("\n", "<br />");
				String executionTime = row[5];
				String tcName = row[6];
				String productName = row[7];
				String executionDate = row[8];
				feature_id = row[9];
				switch (verdict) {
				case "Passed":
					displayVerdict = "<font color='green'><b>PASS</b></font>";
					rowBgColor = Constants.BACKGROUND_COLOR_PASS;
					break;
				case "Failed":
					displayVerdict = "<font color='red'><b>FAIL</b></font>";
					rowBgColor = Constants.BACKGROUND_COLOR_FAIL;
					break;
				default:
					displayVerdict = "<b>BLOCKED</b>";
					rowBgColor = Constants.BACKGROUND_COLOR_BLOCKED;
				}
				//String testcaseId = tcid.substring(1);
				testcaseId = tcid;
				grandTotalHTML += "<tr style='background-color:" + rowBgColor + "'><td>" + displayVerdict + "</td><td><a href= "
						+ Constants.TEST_CASE_PARTIAL_URL + testcaseId + " >" + tcid + "</a></td><td>"
						+ tsid + "</td><td>" + tcName + "</td><td style=\"width:600px\">" + build
						+ "</td><td style=\"width:600px\">" + productName
						+ "</td><td style=\"width:600px\">" + notes
						+ "</td><td style=\"width:600px\">" + executionTime
						+ "</td><td style=\"width:600px\">" + executionDate
						+ "</td></tr>";
				executionLink = Constants.TEST_EXECUTION_LINK + feature_id + Constants.BUILD_NUMBER; 
				grandTotalHTML += getRowHTMLLast30ExecutionStatus(testcaseId);

			}
		}
		reportBody += grandTotalHTML +"</tabel></td></tr></tabel>";



		return reportBody;
	}


	private String getRowHTMLLast30ExecutionStatus(String tcid) {
		String rowHTML = "<tr><td colspan='5'><table><tr style='color:white;'><td>Last 30 executions </td>";
		List<String[]> last30Status = mysql.getLast30ExecutionStatus(tcid);

		for (String[] status : last30Status) {
			String executionStatus = status[0];
			String executionDate = status[1];
			String build = status[2];
			executionDate = executionDate.split(" ")[0];
			executionDate = executionDate.substring(executionDate.lastIndexOf("-") + 1).trim();

			String bgColor = "";
			switch (executionStatus.toUpperCase()) {
			case "PASSED":
				bgColor = Constants.BACKGROUND_COLOR_PASS;
				break;
			case "FAILED":
				bgColor = Constants.BACKGROUND_COLOR_FAIL;
				break;
			default:
				bgColor = Constants.BACKGROUND_COLOR_BLOCKED;
			}
			rowHTML += "<td style='background-color:" + bgColor + "'><b><span title='" + build + "'><a href="+ executionLink +">" + executionDate
					+ "</a></span></b></td>";
		}
		rowHTML += "<tr></table></td></tr>";
		return rowHTML;
	}
}
