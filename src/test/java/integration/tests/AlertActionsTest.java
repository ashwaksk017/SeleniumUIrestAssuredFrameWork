package integration.tests;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.framework.actions.AlertActions;
import com.framework.actions.WebDriverFactory;

public class AlertActionsTest {

    private WebDriver driver;
    private AlertActions alertActions;

    @BeforeClass
    public void setup() {
        driver = WebDriverFactory.getDriver();

        String html = "<html><body>" +
                "<button onclick='alert(\"Simple Alert\")' id='alertBtn'>Alert</button>" +
                "<button onclick='confirm(\"Are you sure?\")' id='confirmBtn'>Confirm</button>" +
                "<button onclick='prompt(\"Your name:\", \"Default\")' id='promptBtn'>Prompt</button>" +
                "</body></html>";

        driver.get("data:text/html;charset=utf-8," + html);
        alertActions = new AlertActions(driver);
    }

    @Test(priority = 1)
    public void testAlertAccept() {
        driver.findElement(By.id("alertBtn")).click();
        Assert.assertTrue(alertActions.isAlertPresent(), "Alert should be present");
        Assert.assertEquals(alertActions.getAlertText(), "Simple Alert");
        alertActions.acceptAlert();
    }

    @Test(priority = 2)
    public void testConfirmDismiss() {
        driver.findElement(By.id("confirmBtn")).click();
        Assert.assertTrue(alertActions.isAlertPresent(), "Confirm should be present");
        Assert.assertEquals(alertActions.getAlertText(), "Are you sure?");
        alertActions.dismissAlert();
    }

    @Test(priority = 3)
    public void testPromptSendKeysAndAccept() {
        driver.findElement(By.id("promptBtn")).click();
        Assert.assertTrue(alertActions.isAlertPresent(), "Prompt should be present");
        alertActions.sendTextToAlert("John Doe");
        alertActions.acceptAlert();
    }

    @Test(priority = 4)
    public void testSafeAlertActions() {
        Assert.assertFalse(alertActions.isAlertPresent(), "No alert should be present now");

        // Safe actions when alert is NOT present (should not throw)
        Assert.assertFalse(alertActions.safeAcceptAlert(), "Safe accept should return false");
        Assert.assertFalse(alertActions.safeDismissAlert(), "Safe dismiss should return false");
        Assert.assertFalse(alertActions.safeSendKeysToAlert("Test"), "Safe send keys should return false");
    }

    @Test(priority = 5)
    public void testWaitForAlertWithCustomTimeout() {
        driver.findElement(By.id("alertBtn")).click();
        Assert.assertNotNull(alertActions.waitForAlert(5), "Alert should appear within 5 seconds");
        alertActions.acceptAlert();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
