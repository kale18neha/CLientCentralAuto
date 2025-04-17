package listeners;

import java.io.File;

import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.observer.ExtentObserver;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.BaseTest;

public class ExtentManager extends BaseTest {
	private static ExtentReports extent;

	public static ExtentReports getExtentReports() {
		if (extent == null) {

			String reportPath = prop.getProperty("ExecutionReportPath") + "/ExtentReport.html";
			Reporter.log("REPORT file: " + reportPath,true);

			File reportDir = new File(prop.getProperty("ExecutionReportPath"));
			if (!reportDir.exists()) {
				reportDir.mkdirs();
			}
			ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

			extent = new ExtentReports();
			extent.attachReporter(new ExtentObserver[] { (ExtentObserver)reporter });

			extent.setSystemInfo("OS", "Windows");
			extent.setSystemInfo("Tester", "Automation User Neha K");
			extent.setSystemInfo("Application Name", "Client Central");
		}
		return extent;
	}
}


