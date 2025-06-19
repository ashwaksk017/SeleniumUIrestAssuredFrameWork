package com.framework.actions;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.function.Function;

public class WaitActions {
    private WebDriver driver;
    private WebDriverWait wait;

    // Default timeout and polling interval
    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private static final int DEFAULT_POLLING_MILLIS = 500;

    public WaitActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        this.wait.pollingEvery(Duration.ofMillis(DEFAULT_POLLING_MILLIS));
        this.wait.ignoring(NoSuchElementException.class);
    }

    // ===================== IMPLICIT WAIT =====================
    public void setImplicitWait(long seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    public void resetImplicitWait() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    // ===================== EXPLICIT WAIT =====================
    public void setExplicitWaitTimeout(long seconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        this.wait.pollingEvery(Duration.ofMillis(DEFAULT_POLLING_MILLIS));
        this.wait.ignoring(NoSuchElementException.class);
    }

    public void setPollingInterval(long millis) {
        this.wait.pollingEvery(Duration.ofMillis(millis));
    }

    // ===================== WAIT FOR ELEMENT TO BE VISIBLE =====================
    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // ===================== WAIT FOR ELEMENT TO BE HIDDEN/INVISIBLE =====================
    public boolean waitForElementInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForElementInvisible(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    // ===================== WAIT FOR TEXT TO APPEAR IN ELEMENT =====================
    public boolean waitForTextToBePresentInElement(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public boolean waitForTextToBePresentInElement(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // ===================== WAIT FOR ATTRIBUTE VALUE =====================
    public boolean waitForAttributeToBe(By locator, String attribute, String value) {
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    public boolean waitForAttributeToContain(By locator, String attribute, String valueSubstring) {
        return wait.until(ExpectedConditions.attributeContains(locator, attribute, valueSubstring));
    }

    // ===================== WAIT FOR ELEMENT TO BE ENABLED/DISABLED =====================
    // Enabled means clickable and enabled attribute true
    public boolean waitForElementToBeEnabled(By locator) {
        return wait.until(driver -> {
            WebElement el = driver.findElement(locator);
            return el.isEnabled() && el.isDisplayed();
        });
    }

    public boolean waitForElementToBeDisabled(By locator) {
        return wait.until(driver -> {
            WebElement el = driver.findElement(locator);
            return !el.isEnabled() || !el.isDisplayed();
        });
    }

    // ===================== EXPLICIT WAIT WITH CUSTOM TIME =====================
    public WebElement waitForElementVisible(By locator, long timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public boolean waitForCondition(Function<WebDriver, Boolean> condition, long timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(condition);
    }

    // ===================== RETRY FAILED ASSERTIONS =====================
    /**
     * Retry a boolean assertion until timeout or success
     * @param assertionFunction a function that returns boolean
     * @param timeoutSeconds max wait seconds
     * @param pollMillis polling interval millis
     * @return true if assertion passed within time; false otherwise
     */
    public boolean retryUntilTrue(Function<WebDriver, Boolean> assertionFunction, long timeoutSeconds, long pollMillis) {
        WebDriverWait retryWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        retryWait.pollingEvery(Duration.ofMillis(pollMillis));
        try {
            return retryWait.until(assertionFunction);
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ===================== ASSERTION CHAINING (basic example) =====================
    // Allows chaining multiple assertions to wait for all of them to pass
    public boolean waitForAllConditions(Function<WebDriver, Boolean>... conditions) {
        for (Function<WebDriver, Boolean> condition : conditions) {
            try {
                wait.until(condition);
            } catch (TimeoutException e) {
                return false;
            }
        }
        return true;
    }

    // ===================== AUTO-WAIT FOR ELEMENTS (generic) =====================
    /**
     * Generic wait for element, retries until element is found or timeout
     * @param locator element locator
     * @param timeoutSeconds timeout duration
     * @return found WebElement
     */
    public WebElement autoWaitForElement(By locator, long timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

}
