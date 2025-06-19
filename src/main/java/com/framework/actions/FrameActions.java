package com.framework.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FrameActions {
    private WebDriver driver;

    public FrameActions(WebDriver driver) {
        this.driver = driver;
    }

    // ✅ Switch to frame by index
    public void switchToFrame(int index) {
        driver.switchTo().frame(index);
    }

    // ✅ Switch to frame by name or ID
    public void switchToFrame(String nameOrId) {
        driver.switchTo().frame(nameOrId);
    }

    // ✅ Switch to frame using WebElement
    public void switchToFrame(WebElement frameElement) {
        driver.switchTo().frame(frameElement);
    }

    // ✅ Switch to frame using locator (recommended)
    public void switchToFrame(By frameLocator) {
        WebElement frame = driver.findElement(frameLocator);
        driver.switchTo().frame(frame);
    }

    // ✅ Switch to parent frame (one level up)
    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    // ✅ Switch to top-level (default) content
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // ✅ Check if in a frame (compare with default content)
    public boolean isInFrame() {
        try {
            driver.switchTo().parentFrame();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ Get current frame element via JS (if needed for logging/debugging)
    public String getCurrentFrameName() {
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return self.name;");
    }

    // ✅ Count number of frames on page
    public int getTotalFrames() {
        return driver.findElements(By.tagName("iframe")).size();
    }
}
