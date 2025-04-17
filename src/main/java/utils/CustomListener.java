package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import listeners.ExtentManager;

public class CustomListener implements ITestListener {
	private static ExtentReports extent;
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	public void onTestStart(ITestResult result) {
		System.out.println("In ON TestStart");
		ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
		test.set(extentTest);
		((ExtentTest)test.get()).log(Status.INFO, " Starting test execution");
	}


	public void onTestSuccess(ITestResult result) {
		((ExtentTest)test.get()).log(Status.PASS, "Test Passed " + result.getMethod().getMethodName());
		Reporter.log("Test Passed: " + result.getMethod().getMethodName(),true);
	}

	public void onTestFailure(ITestResult result) {
		((ExtentTest)test.get()).log(Status.FAIL, "Test Failed " + result.getThrowable());
		Reporter.log("Test Failed: " + result.getMethod().getMethodName(),true);
	}

	public void onTestSkipped(ITestResult result) {
		Reporter.log("Test Skipped: " + result.getMethod().getMethodName(),true);
	}


	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

	public void onStart(ITestContext context) {
		extent = ExtentManager.getExtentReports();
	}


	public void onFinish(ITestContext context) {
		Reporter.log("Test Suite Finished: " + context.getName(),true);
		if (extent != null)
			extent.flush();
	}
}