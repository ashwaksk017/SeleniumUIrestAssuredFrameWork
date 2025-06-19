package com.api.framework.pojos;


import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlertActions {

    private WebDriver driver;
    private WebDriverWait wait;

    public AlertActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ==================== Basic Alert Operations ====================

    public void acceptAlert() {
        Alert alert = waitForAlert();
        if (alert != null) {
            alert.accept();
        }
    }

    public void dismissAlert() {
        Alert alert = waitForAlert();
        if (alert != null) {
            alert.dismiss();
        }
    }

    public String getAlertText() {
        Alert alert = waitForAlert();
        return (alert != null) ? alert.getText() : null;
    }

    public void sendTextToAlert(String text) {
        Alert alert = waitForAlert();
        if (alert != null) {
            alert.sendKeys(text);
        }
    }

    // ==================== Presence Checks ====================

    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException ex) {
            return false;
        }
    }

    public Alert waitForAlert() {
        try {
            return wait.until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            return null;
        }
    }

    public Alert waitForAlert(int seconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            return customWait.until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            return null;
        }
    }

    // ==================== Safe Actions ====================

    public boolean safeAcceptAlert() {
        if (isAlertPresent()) {
            driver.switchTo().alert().accept();
            return true;
        }
        return false;
    }

    public boolean safeDismissAlert() {
        if (isAlertPresent()) {
            driver.switchTo().alert().dismiss();
            return true;
        }
        return false;
    }

    public boolean safeSendKeysToAlert(String text) {
        if (isAlertPresent()) {
            driver.switchTo().alert().sendKeys(text);
            return true;
        }
        return false;
    }
}
