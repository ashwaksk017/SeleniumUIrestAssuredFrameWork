
package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.framework.actions.ScreenshotActions;
import com.framework.actions.WebDriverFactory;
import com.reporter.extent.ExtentManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

public class ScreenshotActionsTest {

    private WebDriver driver;
    private ScreenshotActions screenshotActions;
    private ExtentReports extent;
    private ExtentTest test;

    private final String screenshotsDir = "screenshots";

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentManager.getExtentReports();
    }

    @BeforeMethod
    public void setup() {
        driver = WebDriverFactory.getDriver();
        screenshotActions = new ScreenshotActions(driver);
        test = extent.createTest("ScreenshotActions Tests");
    }

    @Test(priority = 1)
    public void testCaptureFullPageScreenshot() throws IOException {
        driver.get("https://the-internet.herokuapp.com/");
        File screenshot = screenshotActions.captureFullPageScreenshot(screenshotsDir);
        test.info("Captured full page screenshot: " + screenshot.getAbsolutePath());

        Assert.assertTrue(screenshot.exists(), "Full page screenshot file should exist");
        Assert.assertTrue(screenshot.length() > 0, "Full page screenshot file should not be empty");
    }

    @Test(priority = 2)
    public void testCaptureElementScreenshot() throws IOException {
        driver.get("https://the-internet.herokuapp.com/");
        By footerLocator = By.id("page-footer");
        File screenshot = screenshotActions.captureElementScreenshot(footerLocator, screenshotsDir);
        test.info("Captured element screenshot: " + screenshot.getAbsolutePath());

        Assert.assertTrue(screenshot.exists(), "Element screenshot file should exist");
        Assert.assertTrue(screenshot.length() > 0, "Element screenshot file should not be empty");
    }

    @Test(priority = 3)
    public void testCaptureScreenshotAsBase64() {
        driver.get("https://the-internet.herokuapp.com/");
        String base64 = screenshotActions.captureScreenshotAsBase64();
        test.info("Captured screenshot as Base64 string");

        Assert.assertNotNull(base64);
        Assert.assertTrue(base64.length() > 100, "Base64 screenshot string length should be > 100");
    }

    @Test(priority = 4)
    public void testCaptureElementScreenshotAsBase64() {
        driver.get("https://the-internet.herokuapp.com/");
        By footerLocator = By.id("page-footer");
        String base64 = screenshotActions.captureElementScreenshotAsBase64(footerLocator);
        test.info("Captured element screenshot as Base64 string");

        Assert.assertNotNull(base64);
        Assert.assertTrue(base64.length() > 100, "Base64 element screenshot string length should be > 100");
    }

    @Test(priority = 5)
    public void testCompareScreenshotsWithTolerance() throws IOException {
        driver.get("https://the-internet.herokuapp.com/");
        File screenshot1 = screenshotActions.captureFullPageScreenshot(screenshotsDir);
        driver.navigate().refresh();
        File screenshot2 = screenshotActions.captureFullPageScreenshot(screenshotsDir);

        test.info("Comparing two screenshots with tolerance of 10 pixels");
        boolean areSimilar = screenshotActions.compareScreenshotsWithTolerance(screenshot1, screenshot2, 10);

        test.info("Screenshots similarity: " + areSimilar);
        Assert.assertTrue(areSimilar, "Screenshots should be similar within 10 pixels tolerance");
    }

    @Test(priority = 6)
    public void testCaptureAndAttachScreenshot() {
        driver.get("https://the-internet.herokuapp.com/");
        screenshotActions.captureAndAttachScreenshot(test, "Attached Screenshot");
        test.info("Screenshot attached to ExtentReports");
        // No assertion here since this writes to report — verify manually
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
