package test;

import base.BaseTest;
import java.awt.AWTException;
import java.io.IOException;
import org.testng.TestNG;


public class TestMain extends BaseTest
{
	public static void main(String[] args) throws InterruptedException, IOException, AWTException {
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { ReportValidation.class });
		testng.run();
	}
}


