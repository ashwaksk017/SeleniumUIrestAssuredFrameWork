package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.framework.actions.FileTransferActions;
import com.framework.actions.WebDriverFactory;
import com.reporter.extent.ExtentManager;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.Arrays;

public class FileTransferTest {
    private WebDriver driver;
    private FileTransferActions fileActions;
    private ExtentReports extent;
    private ExtentTest test;

    private final String DOWNLOAD_DIR = System.getProperty("user.dir") + "/downloads";
    private final String FILE_NAME = "sample.csv";
    private final String RENAMED_FILE = "renamed_sample.csv";
    private final String FILE_URL = "https://people.sc.fsu.edu/~jburkardt/data/csv/hw_200.csv";

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentManager.getExtentReports();
        File dir = new File(DOWNLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @BeforeMethod
    public void setup() {
        driver = WebDriverFactory.getDriver();
        test = extent.createTest("Remote File Download and Rename Test");
        fileActions = new FileTransferActions(driver, test);
    }

    @Test
    public void testDownloadAndRenameFile() {
        // Clean the download directory before test
        fileActions.cleanDownloadDirectory(DOWNLOAD_DIR);

        String targetFilePath = DOWNLOAD_DIR + "/" + FILE_NAME;

        // Download file
        boolean isDownloaded = fileActions.downloadFileFromUrl(FILE_URL, targetFilePath, 3);
        Assert.assertTrue(isDownloaded, "File download failed");

        // Rename the downloaded file
        String renamedFilePath = DOWNLOAD_DIR + "/" + RENAMED_FILE;
        boolean isRenamed = fileActions.renameFile(targetFilePath, renamedFilePath);
        Assert.assertTrue(isRenamed, "File rename failed");

        // Verify file exists
        File file = new File(renamedFilePath);
        Assert.assertTrue(file.exists(), "Renamed file does not exist");

        test.pass("Remote file downloaded and renamed successfully.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
