package tests;


import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.framework.actions.WebDriverFactory;
import com.framework.actions.WindowActions;

import java.util.List;

public class WindowActionsTest {

    private WebDriver driver;
    private WindowActions windowActions;
    private String mainWindowHandle;

    @BeforeClass
    public void setup() {
        driver = WebDriverFactory.getDriver();
        driver.get("https://example.com");
        windowActions = new WindowActions(driver);
        mainWindowHandle = windowActions.getCurrentWindowHandle();
    }

    @Test(priority = 1)
    public void testOpenNewTab() {
        windowActions.openNewTab();
        windowActions.waitForNewWindow(2);

        List<String> handles = windowActions.getAllWindowHandles();
        Assert.assertEquals(handles.size(), 2, "Should have 2 windows/tabs open");
    }

    @Test(priority = 2)
    public void testSwitchToWindowByIndex() {
        windowActions.switchToWindowByIndex(1);
        driver.get("https://wikipedia.org");
        Assert.assertTrue(driver.getTitle().contains("Wikipedia"));
    }

    @Test(priority = 3)
    public void testWaitForWindowWithTitle() {
        windowActions.waitForWindowWithTitle("Wikipedia");
        Assert.assertTrue(driver.getTitle().contains("Wikipedia"));
    }

    @Test(priority = 4)
    public void testSwitchToMainAndOpenNewWindow() {
        windowActions.switchToWindow(mainWindowHandle);
        windowActions.openNewWindow();
        windowActions.waitForNewWindow(3);
        driver.get("https://www.selenium.dev");

        Assert.assertTrue(driver.getTitle().contains("Selenium"));
    }

    @Test(priority = 5)
    public void testWaitForWindowWithUrl() {
        windowActions.waitForWindowWithUrl("selenium.dev");
        Assert.assertTrue(driver.getCurrentUrl().contains("selenium.dev"));
    }

    @Test(priority = 6)
    public void testWindowCount() {
        int count = windowActions.getWindowCount();
        Assert.assertEquals(count, 3, "Expected 3 open windows/tabs");
    }

    @Test(priority = 7)
    public void testDoesWindowExist() {
        boolean exists = windowActions.doesWindowExist("Wikipedia");
        Assert.assertTrue(exists, "Wikipedia window should exist");
    }

    @Test(priority = 8)
    public void testGetTitleAndUrlOfWindow() {
        List<String> handles = windowActions.getAllWindowHandles();

        for (String handle : handles) {
            String title = windowActions.getTitleOfWindow(handle);
            String url = windowActions.getUrlOfWindow(handle);
            Assert.assertNotNull(title);
            Assert.assertNotNull(url);
        }
    }

    @Test(priority = 9)
    public void testCloseCurrentWindow() {
        int before = windowActions.getWindowCount();
        windowActions.switchToWindowByIndex(2);
        windowActions.closeCurrentWindow();
        windowActions.waitForNewWindow(before - 1);

        Assert.assertEquals(windowActions.getWindowCount(), before - 1);
    }

    @Test(priority = 10)
    public void testCloseAllExceptMain() {
        windowActions.switchToWindow(mainWindowHandle);
        windowActions.closeAllExceptMain();
        Assert.assertEquals(windowActions.getWindowCount(), 1, "Only main window should remain");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
