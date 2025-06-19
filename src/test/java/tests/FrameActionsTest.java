package tests;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.framework.actions.FrameActions;
import com.framework.actions.WebDriverFactory;
import com.reporter.extent.ExtentManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class FrameActionsTest {
    private WebDriver driver;
    private FrameActions frameActions;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentManager.getExtentReports();
    }

    @BeforeMethod
    public void setup() {
        driver = WebDriverFactory.getDriver();  // Ensure Chrome is default
        test = extent.createTest("FrameActions Functional Test");
        frameActions = new FrameActions(driver);
    }

    @Test(priority = 1)
    public void testSwitchToFrameById() {
        driver.get("https://the-internet.herokuapp.com/iframe");
        test.info("Navigated to iframe test page");

        frameActions.switchToFrame("mce_0_ifr");
        test.info("Switched to frame by ID");

        boolean inFrame = frameActions.isInFrame();
        Assert.assertTrue(inFrame, "Not inside frame");

        frameActions.switchToDefaultContent();
        test.info("Returned to default content");
    }

    @Test(priority = 2)
    public void testSwitchToFrameByIndex() {
        driver.get("https://the-internet.herokuapp.com/nested_frames");
        test.info("Navigated to nested frames page");

        frameActions.switchToFrame(0); // top frame
        test.info("Switched to top frame (index 0)");

        frameActions.switchToParentFrame();
        test.info("Returned to parent frame");

        Assert.assertEquals(frameActions.getTotalFrames(), 3);
    }

    @Test(priority = 3)
    public void testSwitchToFrameByLocator() {
        driver.get("https://the-internet.herokuapp.com/iframe");
        frameActions.switchToFrame(By.id("mce_0_ifr"));
        test.info("Switched using locator");

        driver.findElement(By.id("tinymce")).clear();
        driver.findElement(By.id("tinymce")).sendKeys("Frame test successful");
        test.pass("Typed into iframe");

        frameActions.switchToDefaultContent();
    }

    @Test(priority = 4)
    public void testGetCurrentFrameName() {
        driver.get("https://the-internet.herokuapp.com/iframe");

        frameActions.switchToFrame(By.id("mce_0_ifr"));
        String frameName = frameActions.getCurrentFrameName();
        test.info("Current frame name: " + frameName);

        Assert.assertTrue(frameName != null);
    }

    @Test(priority = 5)
    public void testGetTotalFrames() {
        driver.get("https://the-internet.herokuapp.com/nested_frames");

        int totalFrames = frameActions.getTotalFrames();
        test.info("Total frames found: " + totalFrames);

        Assert.assertTrue(totalFrames >= 1);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
