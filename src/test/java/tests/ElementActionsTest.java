package tests;



import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;

import com.framework.actions.ElementActions;
import com.framework.actions.WebDriverFactory;

public class ElementActionsTest {

    private WebDriver driver;
    private ElementActions actions;

    @BeforeClass
    public void setup() {
        driver = WebDriverFactory.getDriver();
        actions = new ElementActions(driver);

        // Load a test page
        String testPage = "<html><body>" +
                "<input type='text' id='input' value='default'>" +
                "<input type='checkbox' id='checkbox'>" +
                "<select id='dropdown'>" +
                "<option value='1'>One</option><option value='2'>Two</option>" +
                "</select>" +
                "<div id='text'>Hello</div>" +
                "<div id='hover'>Hover Here</div>" +
                "<button id='clickBtn'>Click Me</button>" +
                "<div id='source' draggable='true'>DragMe</div>" +
                "<div id='target'>DropHere</div>" +
                "</body></html>";

        driver.get("data:text/html;charset=utf-8," + testPage);
    }

    @Test
    public void testClick() {
        actions.click(By.id("clickBtn"));
        Assert.assertTrue(actions.isElementVisible(By.id("clickBtn")));
    }

    @Test
    public void testSendKeysAndClearInput() {
        actions.sendKeys(By.id("input"), "TestInput");
        Assert.assertEquals(actions.getAttribute(By.id("input"), "value"), "TestInput");

        actions.clearInput(By.id("input"));
        Assert.assertEquals(actions.getAttribute(By.id("input"), "value"), "");
    }

    @Test
    public void testSlowTyping() throws InterruptedException {
        actions.typeSlowly(By.id("input"), "abc", 100);
        Assert.assertEquals(actions.getAttribute(By.id("input"), "value"), "abc");
    }

    @Test
    public void testTextAndAttributes() {
        String text = actions.getText(By.id("text"));
        Assert.assertEquals(text, "Hello");

        String attr = actions.getAttribute(By.id("input"), "id");
        Assert.assertEquals(attr, "input");
    }

    @Test
    public void testCssValue() {
        String cssValue = actions.getCssValue(By.id("input"), "display");
        Assert.assertTrue(cssValue != null && !cssValue.isEmpty());
    }

    @Test
    public void testCheckboxCheckUncheck() {
        actions.checkCheckbox(By.id("checkbox"));
        Assert.assertTrue(actions.isCheckboxChecked(By.id("checkbox")));

        actions.uncheckCheckbox(By.id("checkbox"));
        Assert.assertFalse(actions.isCheckboxChecked(By.id("checkbox")));
    }

    @Test
    public void testDropdownSelections() {
        actions.selectDropdownByVisibleText(By.id("dropdown"), "Two");
        Assert.assertEquals(actions.getAttribute(By.id("dropdown"), "value"), "2");

        actions.selectDropdownByValue(By.id("dropdown"), "1");
        Assert.assertEquals(actions.getAttribute(By.id("dropdown"), "value"), "1");

        actions.selectDropdownByIndex(By.id("dropdown"), 1);
        Assert.assertEquals(actions.getAttribute(By.id("dropdown"), "value"), "2");
    }

    @Test
    public void testHover() {
        actions.hoverOverElement(By.id("hover"));
        Assert.assertTrue(actions.isElementVisible(By.id("hover")));
    }

    @Test
    public void testSafeClickFallback() {
        boolean clicked = actions.safeClick(By.id("clickBtn"));
        Assert.assertTrue(clicked);
    }

    @Test
    public void testIsElementPresentAndEnabled() {
        Assert.assertTrue(actions.isElementPresent(By.id("input")));
        Assert.assertTrue(actions.isElementEnabled(By.id("input")));
    }

    @Test
    public void testKeyboardKeyPress() {
        actions.sendKeys(By.id("input"), "Test");
        actions.pressKey(By.id("input"), Keys.BACK_SPACE);  // Remove last character
        Assert.assertEquals(actions.getAttribute(By.id("input"), "value"), "Tes");
    }

    @Test
    public void testDragAndDropOffset() {
        try {
            actions.dragAndDropOffset(By.id("source"), 50, 0);  // won't validate but should not throw
        } catch (Exception e) {
            Assert.fail("Drag and drop by offset failed");
        }
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
