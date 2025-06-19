package com.java.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginBtn = By.id("login");

    public LoginPage(WebDriver driver) {
        super(driver); // initializes all action classes
    }

    public void login(String username, String password) {
        element.sendKeys(usernameField, username);
        element.sendKeys(passwordField, password);
        element.click(loginBtn);
    }

    public String getTitle() {
        return navigation.getPageTitle();
    }
}
