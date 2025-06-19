package com.framework.actions;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class WindowActions {
    private WebDriver driver;
    private WebDriverWait wait;

    public WindowActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===================== Basic Window Handling =====================

    public void openNewTab() {
        driver.switchTo().newWindow(WindowType.TAB);
    }

    public void openNewWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    public List<String> getAllWindowHandles() {
        return new ArrayList<>(driver.getWindowHandles());
    }

    public int getWindowCount() {
        return driver.getWindowHandles().size();
    }

    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    public void switchToWindowByIndex(int index) {
        List<String> handles = getAllWindowHandles();
        if (index < 0 || index >= handles.size()) {
            throw new NoSuchWindowException("Invalid window index: " + index);
        }
        driver.switchTo().window(handles.get(index));
    }

    public void closeCurrentWindow() {
        driver.close();
        Set<String> handles = driver.getWindowHandles();
        if (!handles.isEmpty()) {
            driver.switchTo().window(handles.iterator().next());
        }
    }

    public void closeAllExceptMain() {
        String mainHandle = getCurrentWindowHandle();
        for (String handle : getAllWindowHandles()) {
            if (!handle.equals(mainHandle)) {
                driver.switchTo().window(handle);
                driver.close();
            }
        }
        driver.switchTo().window(mainHandle);
    }

    // ===================== Waiting =====================

    public void waitForNewWindow(int expectedCount) {
        wait.until((ExpectedCondition<Boolean>) wd -> wd.getWindowHandles().size() == expectedCount);
    }

    public void waitForWindowWithTitle(String title) {
        wait.until((ExpectedCondition<Boolean>) wd -> {
            for (String handle : wd.getWindowHandles()) {
                wd.switchTo().window(handle);
                if (wd.getTitle().equalsIgnoreCase(title)) return true;
            }
            return false;
        });
    }

    public void waitForWindowWithUrl(String partialUrl) {
        wait.until((ExpectedCondition<Boolean>) wd -> {
            for (String handle : wd.getWindowHandles()) {
                wd.switchTo().window(handle);
                if (wd.getCurrentUrl().contains(partialUrl)) return true;
            }
            return false;
        });
    }

    // ===================== Utility =====================

    public String getTitleOfWindow(String handle) {
        driver.switchTo().window(handle);
        return driver.getTitle();
    }

    public String getUrlOfWindow(String handle) {
        driver.switchTo().window(handle);
        return driver.getCurrentUrl();
    }

    public boolean doesWindowExist(String titleOrUrlPartial) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(titleOrUrlPartial) || driver.getCurrentUrl().contains(titleOrUrlPartial)) {
                return true;
            }
        }
        return false;
    }

    public void printAllWindowTitles() {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            System.out.println("Window Title: " + driver.getTitle());
        }
    }
}
