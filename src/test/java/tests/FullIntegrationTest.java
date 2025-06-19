package tests;


import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.framework.actions.AlertActions;
import com.framework.actions.ElementActions;
import com.framework.actions.FileTransferActions;
import com.framework.actions.FrameActions;
import com.framework.actions.JSExecutorActions;
import com.framework.actions.NavigationActions;
import com.framework.actions.ScreenshotActions;
import com.framework.actions.StorageActions;
import com.framework.actions.WaitActions;
import com.framework.actions.WebDriverFactory;
import com.framework.actions.WindowActions;
import com.reporter.extent.ExtentManager;

public class FullIntegrationTest {

    private WebDriver driver;
    private NavigationActions nav;
    private ElementActions elements;
    private AlertActions alerts;
    private WindowActions windows;
    private FrameActions frames;
    private WaitActions waits;
    private StorageActions storage;
    private ScreenshotActions screenshot;
    private JSExecutorActions js;
    private FileTransferActions fileTransfer;

    private static ExtentReports extent;
    private static ExtentTest test;

    @BeforeClass
    public void setup() {
        driver = WebDriverFactory.getDriver();
        nav = new NavigationActions(driver);
        elements = new ElementActions(driver);
        alerts = new AlertActions(driver);
        windows = new WindowActions(driver);
        frames = new FrameActions(driver);
        waits = new WaitActions(driver);
        storage = new StorageActions(driver);
        screenshot = new ScreenshotActions(driver);
        js = new JSExecutorActions(driver);
        fileTransfer = new FileTransferActions(driver);

        // ExtentReports setup (optional)
        extent = ExtentManager.getExtentReports();
        test = extent.createTest("Full Integration Test");

        // Load test HTML (you can use a real test site too)
        String testHTML = "<html><body>" +
                "<input type='text' id='input' />" +
                "<button onclick='alert(\"Hello!\")' id='alertBtn'>Show Alert</button>" +
                "<iframe srcdoc=\"<input id='insideFrame'/>\" id='frame1'></iframe>" +
                "</body></html>";
        driver.get("data:text/html;charset=utf-8," + testHTML);
    }

    @Test
    public void integrationScenarioTest() throws IOException {
        try {
            // 🔹 1. Navigation
            test.info("Navigating to test page");
            nav.reloadPage();
            nav.getPageTitle();

            // 🔹 2. Input actions
            test.info("Typing into input");
            elements.sendKeys(By.id("input"), "TestUser");
            elements.clearInput(By.id("input"));
            elements.sendKeys(By.id("input"), "RetryUser");

            // 🔹 3. JS Execution
            js.executeScript("document.getElementById('input').value='JSUser';");

            // 🔹 4. Alert handling
            driver.findElement(By.id("alertBtn")).click();
            alerts.acceptAlert();
            test.pass("Handled alert successfully");

            // 🔹 5. Frame interaction
            frames.switchToFrame("frame1");
            elements.sendKeys(By.id("insideFrame"), "InsideFrame");
            frames.switchToDefaultContent();

            // 🔹 6. Storage
            storage.setLocalStorageItem("username", "QAUser");
            String user = storage.getLocalStorageItem("username");
            test.info("LocalStorage username: " + user);

            // 🔹 7. Screenshots
            screenshot.captureFullPageScreenshot("TestScreenshot");
            test.addScreenCaptureFromPath("screenshots/TestScreenshot.png");

            // 🔹 8. Wait for element
            waits.waitForElementVisible(By.id("input"));

            // 🔹 9. Window Actions
            String originalHandle = driver.getWindowHandle();
            windows.openNewTab();
            driver.get("https://example.com");
            windows.switchToWindow(originalHandle);

            // 🔹 10. File Transfer (mocked)
            fileTransfer.uploadFile(By.id("input"), "path/to/mockFile.txt");

            test.pass("Full integration flow completed successfully");

        } catch (Exception e) {
            test.fail("Test failed with exception: " + e.getMessage());
            screenshot.captureFullPageScreenshot("FailureScreenshot");
            try {
                test.addScreenCaptureFromPath("screenshots/FailureScreenshot.png");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
        extent.flush();
    }
}
