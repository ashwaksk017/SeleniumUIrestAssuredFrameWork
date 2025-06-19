package tests;


import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.framework.actions.StorageActions;
import com.framework.actions.WebDriverFactory;

import java.util.Map;
import java.util.Set;

public class StorageActionsTest {

    private WebDriver driver;
    private StorageActions storageActions;

    @BeforeClass
    public void setUp() {
        driver = WebDriverFactory.getDriver();
        storageActions = new StorageActions(driver);
        // Load a blank page with storage support
        driver.get("about:blank");
    }

    @Test(priority = 1)
    public void testLocalStorageSetAndGetItem() {
        storageActions.setLocalStorageItem("key1", "value1");
        String value = storageActions.getLocalStorageItem("key1");
        Assert.assertEquals(value, "value1", "LocalStorage item value should match");
    }

    @Test(priority = 2)
    public void testLocalStorageHasKey() {
        Assert.assertTrue(storageActions.isLocalStorageKeyPresent("key1"), "LocalStorage should contain key1");
        Assert.assertFalse(storageActions.isLocalStorageKeyPresent("nonexistent"), "LocalStorage should not contain nonexistent key");
    }

    @Test(priority = 3)
    public void testLocalStorageRemoveItem() {
        storageActions.setLocalStorageItem("removeKey", "toBeRemoved");
        Assert.assertTrue(storageActions.isLocalStorageKeyPresent("removeKey"));
        storageActions.removeLocalStorageItem("removeKey");
        Assert.assertFalse(storageActions.isLocalStorageKeyPresent("removeKey"), "Key should be removed from LocalStorage");
    }

    @Test(priority = 4)
    public void testLocalStorageSizeAndClear() {
        storageActions.setLocalStorageItem("key2", "value2");
        storageActions.setLocalStorageItem("key3", "value3");

        Long size = storageActions.getLocalStorageSize();
        Assert.assertTrue(size >= 2, "LocalStorage size should be at least 2");

        storageActions.clearLocalStorage();
        Long clearedSize = storageActions.getLocalStorageSize();
        Assert.assertEquals(clearedSize.longValue(), 0L, "LocalStorage should be empty after clear");
    }

    @Test(priority = 5)
    public void testLocalStorageGetKeysAndAllItems() {
        storageActions.clearLocalStorage();
        storageActions.setLocalStorageItem("alpha", "a");
        storageActions.setLocalStorageItem("beta", "b");

        Set<String> keys = storageActions.getLocalStorageKeys();
        Assert.assertTrue(keys.contains("alpha"));
        Assert.assertTrue(keys.contains("beta"));

        Map<String, String> allItems = storageActions.getAllLocalStorageItems();
        Assert.assertEquals(allItems.get("alpha"), "a");
        Assert.assertEquals(allItems.get("beta"), "b");
    }

    @Test(priority = 6)
    public void testSessionStorageSetAndGetItem() {
        storageActions.setSessionStorageItem("sKey1", "sValue1");
        String value = storageActions.getSessionStorageItem("sKey1");
        Assert.assertEquals(value, "sValue1", "SessionStorage item value should match");
    }

    @Test(priority = 7)
    public void testSessionStorageHasKey() {
        Assert.assertTrue(storageActions.isSessionStorageKeyPresent("sKey1"), "SessionStorage should contain sKey1");
        Assert.assertFalse(storageActions.isSessionStorageKeyPresent("noKey"), "SessionStorage should not contain noKey");
    }

    @Test(priority = 8)
    public void testSessionStorageRemoveItem() {
        storageActions.setSessionStorageItem("sRemove", "toRemove");
        Assert.assertTrue(storageActions.isSessionStorageKeyPresent("sRemove"));
        storageActions.removeSessionStorageItem("sRemove");
        Assert.assertFalse(storageActions.isSessionStorageKeyPresent("sRemove"), "Key should be removed from SessionStorage");
    }

    @Test(priority = 9)
    public void testSessionStorageSizeAndClear() {
        storageActions.setSessionStorageItem("sKey2", "val2");
        storageActions.setSessionStorageItem("sKey3", "val3");

        Long size = storageActions.getSessionStorageSize();
        Assert.assertTrue(size >= 2, "SessionStorage size should be at least 2");

        storageActions.clearSessionStorage();
        Long clearedSize = storageActions.getSessionStorageSize();
        Assert.assertEquals(clearedSize.longValue(), 0L, "SessionStorage should be empty after clear");
    }

    @Test(priority = 10)
    public void testSessionStorageGetKeysAndAllItems() {
        storageActions.clearSessionStorage();
        storageActions.setSessionStorageItem("sessAlpha", "sa");
        storageActions.setSessionStorageItem("sessBeta", "sb");

        Set<String> keys = storageActions.getSessionStorageKeys();
        Assert.assertTrue(keys.contains("sessAlpha"));
        Assert.assertTrue(keys.contains("sessBeta"));

        Map<String, String> allItems = storageActions.getAllSessionStorageItems();
        Assert.assertEquals(allItems.get("sessAlpha"), "sa");
        Assert.assertEquals(allItems.get("sessBeta"), "sb");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
