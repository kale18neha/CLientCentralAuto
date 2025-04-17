package pages;

import base.BaseTest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import utils.ScreenshotUtil;

public class DownloadOptions extends BaseTest
{
	WebDriver driver;
	By viewDate = By.xpath("//select[@name='view_date']");
	By reportsDropdown = By.xpath("//select[@name='cs_folders']");
	By identifierDropdown = By.xpath("//select[@name='sl_svalue']");
	By downloadBtn = By.xpath("//input[@value='Download']");
	By profile= By.id("reportProfiles");

	public DownloadOptions(WebDriver driver) {
		this.driver = driver;
	}


	public String selectValuesAndDownloadReport(ScreenshotUtil screenshotUtil) throws InterruptedException {
		WebElement profileSelected = null;
		String step="step6";
		try {
			Reporter.log("Downloading the report with profile Name: " + prop.getProperty("profileName") + ", Date of report generated " + prop.getProperty("SeletedReportDate"),true);
			WebElement selectProfile=driver.findElement(profile);
			Select profile=new Select(selectProfile);
			//profile.selectByValue(prop.getProperty("profileName"));
			List<WebElement> options = profile.getOptions();

	        for (WebElement option : options) {
	        	System.out.println(option.getText());
	            if (option.getText().contains(prop.getProperty("profileName"))) {
	                option.click();
	                break;
	            }
	        }
		}
		catch (Exception e) {
			screenshotUtil.captureScreenshot(step, "Expected profile "+prop.getProperty("profileName")+" is not present in the profile dropdown");
			e.printStackTrace();

		}
		if(!(prop.getProperty("SeletedReportDate")).isBlank())
		{
			WebElement date = this.driver.findElement(viewDate);
			Select DateDropdown = new Select(date);
			DateDropdown.deselectAll();
			DateDropdown.selectByVisibleText(prop.getProperty("SeletedReportDate"));
		}
		else
			Reporter.log("Selecting the current date ",true);
		WebElement identifier = this.driver.findElement(identifierDropdown);
		Select identifierDropdown = new Select(identifier);
		profileSelected = identifierDropdown.getFirstSelectedOption();
		screenshotUtil.captureScreenshot(step, "Selecting the profile from option window and clicking on download button.");
		this.driver.findElement(this.downloadBtn).click();
		Thread.sleep(1000L);
		return profileSelected.getText();
	}
}

