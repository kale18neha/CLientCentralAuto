package pages;

import java.awt.AWTException;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import base.BaseTest;
import desktop.TokenGenerator;
import utils.Reusable;
import utils.ScreenshotUtil;



public class ClientHomePage extends BaseTest{
	WebDriver driver;
	By username = By.xpath("//input[@id='Ecom_User_ID']");
	By password = By.xpath("//input[@id='Ecom_Token']");
	By loginBtn = By.xpath("//*[@id='loginButton2']");
	By clientViewReport = By.xpath("//ul[@class='collapse show']");

	public ClientHomePage(WebDriver driver) {
		this.driver = driver;
	}

	public void loginwithToken(ScreenshotUtil screenshotUtil) throws InterruptedException, IOException, AWTException {
		TokenGenerator tk = new TokenGenerator();
		this.driver.get(prop.getProperty("Websiteurl"));
		this.driver.manage().window().maximize();
		Thread.sleep(4000L);
		screenshotUtil.captureScreenshot("step1", "Logging into the client Central Application");
		this.driver.findElement(this.username).sendKeys(new CharSequence[] { prop.getProperty("username") });
		Thread.sleep(2000L);
		String token = tk.getApproveToken(screenshotUtil);
		this.driver.findElement(this.password).sendKeys(new CharSequence[] { token });
		screenshotUtil.captureScreenshot("step3", "Logged in by entering token as a password.");
		this.driver.findElement(this.loginBtn).click();
		Thread.sleep(4000L);
		Reusable.switchToFrameInNewWindow(this.driver, "rows", "main");
	}


	public void clickOnClientCentralReport() {
		if (this.driver.findElement(clientViewReport).isDisplayed()) {
			WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(30));
			this.driver.findElement(clientViewReport).click();
			//wait.until(ExpectedConditions.invisibilityOfElementLocated(clientViewReport));
		}
		else {

			Reporter.log("Unable to find window.",true);
		}
	}
}
