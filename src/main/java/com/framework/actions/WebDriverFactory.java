package com.framework.actions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.safari.*;

public class WebDriverFactory {

    public enum BrowserType {
        CHROME, FIREFOX, EDGE, SAFARI, CHROME_HEADLESS, FIREFOX_HEADLESS
    }

    public static WebDriver getDriver() {
        String browserName = ConfigReader.get("browser").toUpperCase();
        boolean headless = ConfigReader.getBoolean("headless");

        BrowserType browserType;
        switch (browserName) {
            case "CHROME":
                browserType = headless ? BrowserType.CHROME_HEADLESS : BrowserType.CHROME;
                break;
            case "FIREFOX":
                browserType = headless ? BrowserType.FIREFOX_HEADLESS : BrowserType.FIREFOX;
                break;
            case "EDGE":
                browserType = BrowserType.EDGE;
                break;
            case "SAFARI":
                browserType = BrowserType.SAFARI;
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        return getDriver(browserType);
    }

    public static WebDriver getDriver(BrowserType browserType) {
        WebDriver driver;

        switch (browserType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                break;

            case CHROME_HEADLESS:
                WebDriverManager.chromedriver().setup();
                ChromeOptions headlessOptions = new ChromeOptions();
                headlessOptions.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080");
                driver = new ChromeDriver(headlessOptions);
                break;

            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                ffOptions.addArguments("--width=1920", "--height=1080");
                driver = new FirefoxDriver(ffOptions);
                break;

            case FIREFOX_HEADLESS:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffHeadless = new FirefoxOptions();
                ffHeadless.addArguments("--headless");
                driver = new FirefoxDriver(ffHeadless);
                break;

            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            case SAFARI:
                driver = new SafariDriver();
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browserType);
        }
        return driver;
    }
}
