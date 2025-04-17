package pages;

import base.BaseTest;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import utils.Reusable;



public class DxEnterpriseWindow
        extends BaseTest
{
    WebDriver driver;
    By categorypath = By.xpath("//select[@name='rc']");
    By reportsDropdown = By.xpath("//select[@name='cs_folders']");
    By identifierDropdown = By.xpath("//select[@name='sl_svalue']");
    By downloadBtn = By.xpath("//input[@name='download_button']");

    public DxEnterpriseWindow(WebDriver driver) {
        this.driver = driver;
    }


    public void navigateToDXEnterprise(String mainWindowHandle, Set<String> windowHandlesBefore) throws InterruptedException {
        Reporter.log("Navigating to DX Enterprise window",true);
        Reusable.switchToNewWindow(driver, mainWindowHandle, windowHandlesBefore);
        Reusable.switchToFrameInNewWindow(driver, "rows", "top");
        Reusable.switchToFrameInNewWindow(driver, "cols", "topleft");
    }


    public void selectDXEnterpriseCategory() {
        System.out.println("Selecting DX enterprise categoty- " + prop.getProperty("DXEnterpriseCategory"));
        WebElement category = driver.findElement(categorypath);
        Select categoryDropdown = new Select(category);
        categoryDropdown.selectByVisibleText(prop.getProperty("DXEnterpriseCategory"));
    }

    public void selectDXEnterpriseReports() {
        System.out.println("Selecting DX enterprise Reports " + prop.getProperty("DXEnterpriseReport"));
        WebElement reports = driver.findElement(reportsDropdown);
        Select reportsDropdown = new Select(reports);
        reportsDropdown.selectByVisibleText(prop.getProperty("DXEnterpriseReport"));
    }

    public void selectDXEnterpriseIdentifier() {
        System.out.println("Selecting DX enterprise Identifier " + prop.getProperty("DXEnterpriseIdentification"));
        WebElement identi = this.driver.findElement(this.identifierDropdown);
        Select identiDropdown = new Select(identi);
        identiDropdown.selectByVisibleText(prop.getProperty("DXEnterpriseIdentification"));
    }


    public void clickOnDownloadBtn(String mainWindowHandle, Set<String> windowHandlesBefore) throws InterruptedException {
        Reporter.log("Click on the Download button.",true);
        windowHandlesBefore = this.driver.getWindowHandles();
        String mainWindowHandle1 = this.driver.getWindowHandle();
        this.driver.findElement(this.downloadBtn).click();
        Thread.sleep(2000L);
        Reusable.switchToNewWindow(this.driver, mainWindowHandle1, windowHandlesBefore);
    }
}
