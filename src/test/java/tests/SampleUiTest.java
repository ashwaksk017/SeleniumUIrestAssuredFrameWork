package tests;


import java.lang.reflect.Method;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.reporter.extent.ExtentManager;

public class SampleUiTest {

    private WebDriver driver;
    private ExtentReports extent;

    @BeforeSuite
    public void beforeSuite() {
        extent = ExtentManager.getExtentReports();
    }

    @BeforeMethod
    public void setup(Method method) {
        // Start a new Extent test for this method
        ExtentTest test = extent.createTest("UI Test: " + method.getName());
        ExtentManager.setTest(test);

        // Initialize WebDriver (make sure chromedriver is in PATH or specify its location)
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        ExtentManager.getTest().info("Chrome browser launched");
    }

    @Test
    public void testGoogleSearch() {
        ExtentTest test = ExtentManager.getTest();

        driver.get("https://www.google.com");
        test.info("Navigated to https://www.google.com");

        driver.findElement(By.name("q")).sendKeys("ExtentReports Selenium integration");
        test.info("Entered search text");

        driver.findElement(By.name("btnK")).submit();
        test.info("Submitted search form");

        String title = driver.getTitle();
        test.info("Page title is: " + title);

        try {
            // Simple assertion example
            if (title.toLowerCase().contains("extentreports")) {
                test.pass("Title contains 'extentreports'");
            } else {
                test.fail("Title does not contain 'extentreports'");
                assert false;
            }
        } catch (AssertionError e) {
            test.fail("Assertion failed: " + e.getMessage());
            throw e;
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
            ExtentManager.getTest().info("Browser closed");
        }
    }

    @AfterSuite
    public void afterSuite() {
        ExtentManager.flushReports();
    }
}
