package tests;



import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;

import com.framework.actions.WaitActions;
import com.framework.actions.WebDriverFactory;

import java.time.Duration;

public class WaitActionsTest {

    private WebDriver driver;
    private WaitActions waitActions;

    @BeforeClass
    public void setUp() {
        driver = WebDriverFactory.getDriver();
        waitActions = new WaitActions(driver);

        // Load a test page with elements we can manipulate
        String html = "<html><head><title>WaitActions Test</title></head><body>" +
                "<button id='btn' style='display:none;' disabled>Click Me</button>" +
                "<div id='text'>Initial</div>" +
                "<div id='attr' data-status='old'></div>" +
                "<div id='enable-toggle' disabled>Toggle Me</div>" +
                "</body></html>";
        driver.get("data:text/html;charset=utf-8," + html);
    }

    @Test
    public void testWaitForElementVisible() {
        // Make the button visible after 1 second
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                ((JavascriptExecutor) driver).executeScript("document.getElementById('btn').style.display = 'block';");
            } catch (InterruptedException ignored) {}
        }).start();

        WebElement btn = waitActions.waitForElementVisible(By.id("btn"));
        Assert.assertTrue(btn.isDisplayed());
    }

    @Test
    public void testWaitForElementInvisible() {
        // Hide button after 1 second
        ((JavascriptExecutor) driver).executeScript("document.getElementById('btn').style.display = 'block';");
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                ((JavascriptExecutor) driver).executeScript("document.getElementById('btn').style.display = 'none';");
            } catch (InterruptedException ignored) {}
        }).start();

        boolean hidden = waitActions.waitForElementInvisible(By.id("btn"));
        Assert.assertTrue(hidden);
    }

    @Test
    public void testWaitForTextToBePresentInElement() {
        // Change text after 1 second
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                ((JavascriptExecutor) driver).executeScript("document.getElementById('text').textContent = 'Hello World';");
            } catch (InterruptedException ignored) {}
        }).start();

        boolean textAppeared = waitActions.waitForTextToBePresentInElement(By.id("text"), "Hello World");
        Assert.assertTrue(textAppeared);
    }

    @Test
    public void testWaitForAttributeToBe() {
        // Change attribute after 1 second
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                ((JavascriptExecutor) driver).executeScript("document.getElementById('attr').setAttribute('data-status', 'new');");
            } catch (InterruptedException ignored) {}
        }).start();

        boolean attrChanged = waitActions.waitForAttributeToBe(By.id("attr"), "data-status", "new");
        Assert.assertTrue(attrChanged);
    }

    @Test
    public void testWaitForElementToBeEnabledAndDisabled() {
        WebElement toggle = driver.findElement(By.id("enable-toggle"));

        // Enable after 1 second
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                ((JavascriptExecutor) driver).executeScript("document.getElementById('enable-toggle').removeAttribute('disabled');");
            } catch (InterruptedException ignored) {}
        }).start();

        boolean enabled = waitActions.waitForElementToBeEnabled(By.id("enable-toggle"));
        Assert.assertTrue(enabled);

        // Disable after 1 second
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                ((JavascriptExecutor) driver).executeScript("document.getElementById('enable-toggle').setAttribute('disabled', '');");
            } catch (InterruptedException ignored) {}
        }).start();

        boolean disabled = waitActions.waitForElementToBeDisabled(By.id("enable-toggle"));
        Assert.assertTrue(disabled);
    }

    @Test
    public void testExplicitWaitWithCustomTimeout() {
        // Change text after 2 seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                ((JavascriptExecutor) driver).executeScript("document.getElementById('text').textContent = 'Custom Timeout';");
            } catch (InterruptedException ignored) {}
        }).start();

        // Wait with 5 sec timeout, should succeed
        boolean textAppeared = waitActions.waitForCondition(
                drv -> drv.findElement(By.id("text")).getText().equals("Custom Timeout"), 5);
        Assert.assertTrue(textAppeared);

        // Wait with 1 sec timeout, should fail and return false
        boolean failedWait = waitActions.waitForCondition(
                drv -> drv.findElement(By.id("text")).getText().equals("Never Happens"), 1);
        Assert.assertFalse(failedWait);
    }

    @Test
    public void testRetryUntilTrue() {
        // Text changes to "Ready" after 2 seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                ((JavascriptExecutor) driver).executeScript("document.getElementById('text').textContent = 'Ready';");
            } catch (InterruptedException ignored) {}
        }).start();

        boolean success = waitActions.retryUntilTrue(
                drv -> drv.findElement(By.id("text")).getText().equals("Ready"), 5, 500);
        Assert.assertTrue(success);

        boolean fail = waitActions.retryUntilTrue(
                drv -> drv.findElement(By.id("text")).getText().equals("Nope"), 3, 500);
        Assert.assertFalse(fail);
    }

    @Test
    public void testWaitForAllConditions() {
        // Prepare elements
        ((JavascriptExecutor) driver).executeScript("document.getElementById('btn').style.display = 'block';");
        ((JavascriptExecutor) driver).executeScript("document.getElementById('enable-toggle').removeAttribute('disabled');");

        boolean allPassed = waitActions.waitForAllConditions(
                drv -> drv.findElement(By.id("btn")).isDisplayed(),
                drv -> drv.findElement(By.id("enable-toggle")).isEnabled());
        Assert.assertTrue(allPassed);
    }

    @Test
    public void testAutoWaitForElement() {
        // Show element after 1 second
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                ((JavascriptExecutor) driver).executeScript("var el=document.createElement('div'); el.id='newElem'; document.body.appendChild(el);");
            } catch (InterruptedException ignored) {}
        }).start();

        WebElement newElem = waitActions.autoWaitForElement(By.id("newElem"), 5);
        Assert.assertNotNull(newElem);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
