package tests;


import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.framework.actions.NavigationActions;
import com.framework.actions.WebDriverFactory;

import java.util.List;

public class NavigationActionsTest {

    private WebDriver driver;
    private NavigationActions nav;

    @BeforeClass
    public void setUp() {
        driver = WebDriverFactory.getDriver();
        nav = new NavigationActions(driver);
    }

    @Test(priority = 1)
    public void testGoToUrlAndGetTitle() {
        nav.goToUrl("https://example.com");
        nav.waitForPageLoadComplete(15);
        String title = nav.getPageTitle();
        String currentUrl = nav.getCurrentUrl();

        Assert.assertTrue(title.contains("Example Domain"), "Page title should contain 'Example Domain'");
        Assert.assertTrue(currentUrl.contains("example.com"), "URL should contain 'example.com'");
    }

    @Test(priority = 2)
    public void testReloadPage() {
        String urlBefore = nav.getCurrentUrl();
        nav.reloadPage();
        String urlAfter = nav.getCurrentUrl();
        Assert.assertEquals(urlBefore, urlAfter, "URL should be the same after reload");
    }

    @Test(priority = 3)
    public void testNavigateBackAndForward() {
        nav.goToUrl("https://example.com");
        nav.waitForPageLoadComplete(10);

        nav.goToUrl("https://google.com");
        nav.waitForPageLoadComplete(10);

        nav.navigateBack();
        nav.waitForPageLoadComplete(10);
        Assert.assertTrue(nav.getCurrentUrl().contains("example.com"), "Should have navigated back to example.com");

        nav.navigateForward();
        nav.waitForPageLoadComplete(10);
        Assert.assertTrue(nav.getCurrentUrl().contains("google.com"), "Should have navigated forward to google.com");
    }

    @Test(priority = 4)
    public void testSetAndGetWindowSize() {
        nav.setWindowSize(1024, 768);
        Dimension size = nav.getWindowSize();

        Assert.assertEquals(size.getWidth(), 1024);
        Assert.assertEquals(size.getHeight(), 768);
    }

    @Test(priority = 5)
    public void testMaximizeWindow() {
        nav.maximizeWindow();
        Dimension size = nav.getWindowSize();
        Assert.assertTrue(size.getWidth() > 1024, "Window width should be greater than 1024 after maximizing");
    }

    @Test(priority = 6)
    public void testOpenAndSwitchToNewTab() {
        String originalHandle = nav.getCurrentWindowHandle();

        nav.openNewTab();
        List<String> handles = nav.listAllWindowHandles();

        Assert.assertEquals(handles.size(), 2, "Should have 2 windows/tabs open");
        nav.switchToWindowByIndex(1);
        nav.goToUrl("https://wikipedia.org");

        Assert.assertTrue(nav.getCurrentUrl().contains("wikipedia"), "Should have navigated to Wikipedia");

        // Switch back and verify
        nav.switchToWindow(originalHandle);
        Assert.assertTrue(nav.getCurrentUrl().contains("google") || nav.getCurrentUrl().contains("example"),
                "Back to previous window");
    }

    @Test(priority = 7)
    public void testOpenNewWindowAndClose() {
        nav.openNewWindow();
        List<String> handlesBefore = nav.listAllWindowHandles();
        Assert.assertTrue(handlesBefore.size() >= 2, "At least 2 windows should be open");

        nav.goToUrl("https://openai.com");
        nav.waitForPageLoadComplete(10);
        Assert.assertTrue(nav.getCurrentUrl().contains("openai"));

        nav.closeCurrentWindow();

        // Ensure switch back to existing window
        String currentUrl = nav.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Should have switched to another open window");
    }

    @Test(priority = 8)
    public void testPageLoadTimeoutSetting() {
        // Should not throw exception with fast site
        nav.setPageLoadTimeout(10);
        nav.goToUrl("https://example.com");
        nav.waitForPageLoadComplete(10);
    }

    @Test(priority = 9)
    public void testListAllWindowHandlesAndCurrentHandle() {
        List<String> handles = nav.listAllWindowHandles();
        String current = nav.getCurrentWindowHandle();

        Assert.assertTrue(handles.contains(current), "Current handle should be in list of all handles");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
