package com.framework.actions;


import org.openqa.selenium.*;

public class JSExecutorActions {
    private WebDriver driver;
    private JavascriptExecutor js;

    public JSExecutorActions(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    // ✅ Scroll to element
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // ✅ Scroll by pixel
    public void scrollBy(int x, int y) {
        js.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    // ✅ Scroll to bottom
    public void scrollToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // ✅ Scroll to top
    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    // ✅ Click element using JS
    public void clickUsingJS(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].click();", element);
    }

    // ✅ Set value of input using JS
    public void setInputValue(By locator, String value) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].value='" + value + "';", element);
    }

    // ✅ Get value of input field
    public String getInputValue(By locator) {
        WebElement element = driver.findElement(locator);
        return (String) js.executeScript("return arguments[0].value;", element);
    }

    // ✅ Highlight element
    public void highlightElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    // ✅ Get page title
    public String getPageTitleUsingJS() {
        return (String) js.executeScript("return document.title;");
    }

    // ✅ Get innerText of element
    public String getInnerText(By locator) {
        WebElement element = driver.findElement(locator);
        return (String) js.executeScript("return arguments[0].innerText;", element);
    }

    // ✅ Get page inner text
    public String getPageInnerText() {
        return (String) js.executeScript("return document.documentElement.innerText;");
    }

    // ✅ Refresh page
    public void refreshPageUsingJS() {
        js.executeScript("history.go(0);");
    }

    // ✅ Get domain
    public String getDomain() {
        return (String) js.executeScript("return document.domain;");
    }

    // ✅ Get URL
    public String getCurrentUrlUsingJS() {
        return (String) js.executeScript("return document.URL;");
    }

    // ✅ Navigate to URL using JS
    public void navigateToUrlUsingJS(String url) {
        js.executeScript("window.location='" + url + "';");
    }

    // ✅ Execute custom JS and return result
    public Object executeScript(String script, Object... args) {
        return js.executeScript(script, args);
    }

    // ✅ Disable all buttons temporarily (for UI testing)
    public void disableAllButtons() {
        js.executeScript("Array.from(document.querySelectorAll('button')).forEach(btn => btn.disabled = true);");
    }
}
