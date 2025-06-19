package tests;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.data.loader.CsvDataLoader;
import com.framework.actions.WebDriverFactory;
import com.java.pages.LoginPage;

public class LoginTestCSVDriven {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeClass
    public void setup() {
        driver = WebDriverFactory.getDriver();
        loginPage = new LoginPage(driver);
    }

    @Test(dataProvider = "loginData", dataProviderClass = CsvDataLoader.class)
    public void validLoginTest(Map<String, String> data) {
        driver.get("https://example.com/login");

        String username = data.get("username");
        String password = data.get("password");

        loginPage.login(username, password);
        String title = loginPage.getTitle();

        assert title.contains("Dashboard") : "Login failed for user: " + username;
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
