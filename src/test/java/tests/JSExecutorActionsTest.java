package tests;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.framework.actions.JSExecutorActions;
import com.framework.actions.WebDriverFactory;
import com.reporter.extent.ExtentManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class JSExecutorActionsTest {
    private WebDriver driver;
    private JSExecutorActions jsActions;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentManager.getExtentReports();
    }

    @BeforeMethod
    public void setup() {
        driver = WebDriverFactory.getDriver();
        jsActions = new JSExecutorActions(driver);
        test = extent.createTest("JSExecutorActions Test");
    }

    @Test(priority = 1)
    public void testScrollToElementAndClickUsingJS() {
        driver.get("https://the-internet.herokuapp.com/large");
        By footerLocator = By.id("page-footer");

        jsActions.scrollToElement(footerLocator);
        test.info("Scrolled to footer element");

        Assert.assertTrue(driver.findElement(footerLocator).isDisplayed());
    }

    @Test(priority = 2)
    public void testScrollByPixelAndScrollTopBottom() {
        driver.get("https://the-internet.herokuapp.com/large");

        jsActions.scrollBy(0, 400);
        test.info("Scrolled down by 400px");

        jsActions.scrollToBottom();
        test.info("Scrolled to bottom");

        jsActions.scrollToTop();
        test.info("Scrolled to top");

        Assert.assertTrue(driver.getTitle().contains("The Internet"));
    }

    @Test(priority = 3)
    public void testClickUsingJS() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");
        By addButton = By.xpath("//button[text()='Add Element']");
        By deleteButton = By.className("added-manually");

        jsActions.clickUsingJS(addButton);
        test.info("Clicked Add Element via JS");

        Assert.assertTrue(driver.findElement(deleteButton).isDisplayed());
    }

    @Test(priority = 4)
    public void testSetInputValueAndGetValue() {
        driver.get("https://the-internet.herokuapp.com/inputs");
        By inputField = By.tagName("input");

        jsActions.setInputValue(inputField, "12345");
        String value = jsActions.getInputValue(inputField);

        test.info("Input value set to: " + value);
        Assert.assertEquals(value, "12345");
    }

    @Test(priority = 5)
    public void testHighlightElement() {
        driver.get("https://the-internet.herokuapp.com/inputs");
        By inputField = By.tagName("input");

        jsActions.highlightElement(inputField);
        test.info("Highlighted input element");
        Assert.assertTrue(driver.findElement(inputField).isDisplayed());
    }

    @Test(priority = 6)
    public void testGetPageInfoUsingJS() {
        driver.get("https://the-internet.herokuapp.com");

        String title = jsActions.getPageTitleUsingJS();
        String domain = jsActions.getDomain();
        String url = jsActions.getCurrentUrlUsingJS();
        String text = jsActions.getPageInnerText();

        test.info("Title: " + title);
        test.info("Domain: " + domain);
        test.info("URL: " + url);
        test.info("Text contains 'Welcome': " + text.contains("Welcome"));

        Assert.assertTrue(title.contains("The Internet"));
        Assert.assertTrue(domain.contains("herokuapp"));
        Assert.assertTrue(url.contains("https://"));
        Assert.assertTrue(text.contains("Welcome"));
    }

    @Test(priority = 7)
    public void testRefreshAndNavigateUsingJS() {
        driver.get("https://the-internet.herokuapp.com/status_codes");

        jsActions.refreshPageUsingJS();
        test.info("Page refreshed using JS");

        jsActions.navigateToUrlUsingJS("https://the-internet.herokuapp.com/checkboxes");
        test.info("Navigated to checkboxes using JS");

        Assert.assertTrue(driver.getCurrentUrl().contains("checkboxes"));
    }

    @Test(priority = 8)
    public void testDisableAllButtons() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");
        jsActions.disableAllButtons();
        test.info("All buttons disabled using JS");

        String disabled = driver.findElement(By.xpath("//button[text()='Add Element']")).getAttribute("disabled");
        Assert.assertNotNull(disabled, "Button is not disabled");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        extent.flush();
    }
}
