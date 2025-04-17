package test;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.ClientHomePage;
import pages.DownloadOptions;
import pages.DxEnterpriseWindow;
import utils.CustomListener;
import utils.Reusable;
import utils.ScreenshotUtil;



@Listeners(CustomListener.class)
public class ReportValidation extends BaseTest
{
	WebDriver driver;
	DxEnterpriseWindow DXEnterpriseWindowObj;
	DownloadOptions DownloadOptionObj;
	ClientHomePage clientHomePageObj;
	ScreenshotUtil screenshotUtil;

	@BeforeClass
	public void setup() {
	WebDriverManager.chromedriver().setup();
	this.driver=(WebDriver)new ChromeDriver();
	this.clientHomePageObj=new ClientHomePage(driver);
	this.DownloadOptionObj=new DownloadOptions(driver); 
	this.DXEnterpriseWindowObj=new DxEnterpriseWindow(driver);
	this.screenshotUtil = new ScreenshotUtil(driver);}



	@Test
	public void validateReport() throws IOException, InterruptedException, AWTException {
		String downloadDir = prop.getProperty("ZipDownloadFilePath");
		this.clientHomePageObj.loginwithToken(screenshotUtil);
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> windowHandlesBefore = driver.getWindowHandles();
		clientHomePageObj.clickOnClientCentralReport();
		screenshotUtil.captureScreenshot("step4", "Clicking on view report");
		DXEnterpriseWindowObj.navigateToDXEnterprise(mainWindowHandle, windowHandlesBefore);
		DXEnterpriseWindowObj.selectDXEnterpriseCategory();
		DXEnterpriseWindowObj.selectDXEnterpriseReports();
		DXEnterpriseWindowObj.selectDXEnterpriseIdentifier();
		screenshotUtil.captureScreenshot("step5", "Selecting the values from DX enterprise window.");
		DXEnterpriseWindowObj.clickOnDownloadBtn(mainWindowHandle, windowHandlesBefore);
		String profileSelected = DownloadOptionObj.selectValuesAndDownloadReport(screenshotUtil);
		String downloadedFileName = Reusable.waitForDownloadToComplete(downloadDir);
		Reporter.log("Download fileName "+downloadedFileName,true);
		//String fileName = Reusable.getLatestDownloadedFileName(downloadDir);
		File unzippedDir = new File(downloadDir + "/unzipped/"+profileSelected);
		File folderPath=Reusable.unzipFile(downloadDir + "/" + downloadedFileName, unzippedDir.getAbsolutePath());
		Reusable.readUnzippedFiles(folderPath, profileSelected,screenshotUtil);
	}




	@AfterClass
	public void tearDown() {
		Reporter.log("Find the final exeution report in the path mentioned: " + prop.getProperty("ExecutionReportPath"),true);
		if (this.driver != null)
			this.driver.quit();
	}
}



