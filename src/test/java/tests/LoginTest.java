package tests;



import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.framework.actions.WebDriverFactory;
import com.java.pages.LoginPage;

public class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeClass
    public void setup() {
        driver = WebDriverFactory.getDriver();
        loginPage = new LoginPage(driver);
    }

    @Test
    public void validLoginTest() {
        driver.get("https://example.com/login");
        loginPage.login("admin", "admin123");
        String title = loginPage.getTitle();
        assert title.contains("Dashboard");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
