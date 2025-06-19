package com.framework.actions;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ElementActions {

    private WebDriver driver;
    private Actions actions;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    // =================== Element Interaction ===================

    public void click(By locator) {
        driver.findElement(locator).click();
    }

    public void click(WebElement element) {
        element.click();
    }

    public void jsClick(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void doubleClick(By locator) {
        actions.doubleClick(driver.findElement(locator)).perform();
    }

    public void rightClick(By locator) {
        actions.contextClick(driver.findElement(locator)).perform();
    }

    public void hoverOverElement(By locator) {
        actions.moveToElement(driver.findElement(locator)).perform();
    }

    public void sendKeys(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void clearInput(By locator) {
        driver.findElement(locator).clear();
    }

    // =================== Element Info ===================

    public String getText(By locator) {
        return driver.findElement(locator).getText();
    }

    public String getAttribute(By locator, String attribute) {
        return driver.findElement(locator).getAttribute(attribute);
    }

    public String getCssValue(By locator, String propertyName) {
        return driver.findElement(locator).getCssValue(propertyName);
    }

    // =================== State Checks ===================

    public boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    // =================== Dropdowns ===================

    public void selectDropdownByValue(By locator, String value) {
        Select select = new Select(driver.findElement(locator));
        select.selectByValue(value);
    }

    public void selectDropdownByVisibleText(By locator, String text) {
        Select select = new Select(driver.findElement(locator));
        select.selectByVisibleText(text);
    }

    public void selectDropdownByIndex(By locator, int index) {
        Select select = new Select(driver.findElement(locator));
        select.selectByIndex(index);
    }

    // =================== Checkboxes ===================

    public void checkCheckbox(By locator) {
        WebElement checkbox = driver.findElement(locator);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void uncheckCheckbox(By locator) {
        WebElement checkbox = driver.findElement(locator);
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public boolean isCheckboxChecked(By locator) {
        return driver.findElement(locator).isSelected();
    }

    // =================== Drag and Drop ===================

    public void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = driver.findElement(sourceLocator);
        WebElement target = driver.findElement(targetLocator);
        actions.dragAndDrop(source, target).perform();
    }

    public void dragAndDropOffset(By sourceLocator, int xOffset, int yOffset) {
        WebElement source = driver.findElement(sourceLocator);
        actions.dragAndDropBy(source, xOffset, yOffset).perform();
    }

    // =================== Keyboard ===================

    public void pressKey(By locator, Keys key) {
        driver.findElement(locator).sendKeys(key);
    }

    public void pressKey(Keys key) {
        actions.sendKeys(key).perform();
    }

    public void typeSlowly(By locator, String text, long delayMillis) throws InterruptedException {
        WebElement element = driver.findElement(locator);
        element.clear();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            Thread.sleep(delayMillis);
        }
    }

    // =================== Utility ===================

    public boolean safeClick(By locator) {
        try {
            click(locator);
            return true;
        } catch (ElementClickInterceptedException | NoSuchElementException e) {
            try {
                jsClick(locator);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

}
