package utils;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil
extends BaseTest
{
	private WebDriver driver;

	public ScreenshotUtil(WebDriver driver) {
		this.driver = driver;
	}


	public void captureScreenshot(String step, String stepDescription) {
		File screenshot = (File)((TakesScreenshot)this.driver).getScreenshotAs(OutputType.FILE);
		String screenshotPath = prop.getProperty("ExecutionReportPath") + "/screenshots/" + step + ".png";
		File reportDir = new File(prop.getProperty("ExecutionReportPath") + "/screenshots");
		if (!reportDir.exists()) {
			reportDir.mkdirs();
		}
		try {
			Files.copy(screenshot.toPath(), Path.of(screenshotPath, new String[0]), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });

			((ExtentTest)CustomListener.test.get()).info(step+": "+ stepDescription  , MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}