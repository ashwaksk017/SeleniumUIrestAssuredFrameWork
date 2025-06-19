package com.framework.actions;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NavigationActions {
    private WebDriver driver;
    private WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT_SECONDS = 30;

    public NavigationActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    // ----------- Basic Navigation -------------

    /** Navigate to a specified URL */
    public void goToUrl(String url) {
        driver.get(url);
    }

    /** Reload current page */
    public void reloadPage() {
        driver.navigate().refresh();
    }

    /** Navigate back */
    public void navigateBack() {
        driver.navigate().back();
    }

    /** Navigate forward */
    public void navigateForward() {
        driver.navigate().forward();
    }

    /** Get current URL */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /** Get page title */
    public String getPageTitle() {
        return driver.getTitle();
    }

    // ----------- Window and Tab Management -------------

    /** Set browser window size */
    public void setWindowSize(int width, int height) {
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
    }

    /** Get browser window size */
    public org.openqa.selenium.Dimension getWindowSize() {
        return driver.manage().window().getSize();
    }

    /** Maximize window */
    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    // ----------- Timeout Management -------------

    /** Set page load timeout in seconds */
    public void setPageLoadTimeout(long seconds) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(seconds));
    }

    // ----------- Tabs and Windows -------------

    /** Open new tab */
    public void openNewTab() {
        driver.switchTo().newWindow(org.openqa.selenium.WindowType.TAB);
    }

    /** Open new window */
    public void openNewWindow() {
        driver.switchTo().newWindow(org.openqa.selenium.WindowType.WINDOW);
    }

    /**
     * Switch to tab/window by handle
     * @param handle window handle to switch to
     * @throws NoSuchWindowException if handle not found
     */
    public void switchToWindow(String handle) {
        Set<String> handles = driver.getWindowHandles();
        if (handles.contains(handle)) {
            driver.switchTo().window(handle);
        } else {
            throw new NoSuchWindowException("Window handle not found: " + handle);
        }
    }

    /**
     * Switch to tab/window by index
     * @param index zero-based index of tab/window
     * @throws NoSuchWindowException if index out of bounds
     */
    public void switchToWindowByIndex(int index) {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        if (index < 0 || index >= handles.size()) {
            throw new NoSuchWindowException("Invalid window index: " + index);
        }
        driver.switchTo().window(handles.get(index));
    }

    /** List all open tabs/windows handles */
    public List<String> listAllWindowHandles() {
        return new ArrayList<>(driver.getWindowHandles());
    }

    /** Get current tab/window handle */
    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    /** Close current tab/window */
    public void closeCurrentWindow() {
        driver.close();
        // After close, switch to first available window if any
        Set<String> handles = driver.getWindowHandles();
        if (!handles.isEmpty()) {
            driver.switchTo().window(handles.iterator().next());
        }
    }

    // ----------- Wait for page to load completely -------------

    /**
     * Wait until document.readyState == "complete"
     * @param timeoutSeconds timeout in seconds
     */
    public void waitForPageLoadComplete(long timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        customWait.until((ExpectedCondition<Boolean>) wd ->
                ((String)((org.openqa.selenium.JavascriptExecutor) wd)
                        .executeScript("return document.readyState")).equals("complete"));
    }

}
