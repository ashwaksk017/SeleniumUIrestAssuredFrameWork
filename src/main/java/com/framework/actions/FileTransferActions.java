package com.framework.actions;
	import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;

public class FileTransferActions {
    private WebDriver driver;
    private ExtentTest test;

    public FileTransferActions(WebDriver driver, ExtentTest test) {
        this.driver = driver;
        this.test = test;
    }

    
    public FileTransferActions(WebDriver driver) {
        this.driver = driver;
    }
    
    // Upload a file via <input type="file">
    public void uploadFile(By locator, String absoluteFilePath) {
        driver.findElement(locator).sendKeys(absoluteFilePath);
        test.info("Uploaded file: " + absoluteFilePath);
    }

    // Wait for file download with optional file type filtering
    public boolean isFileDownloaded(String downloadDir, String expectedFileName, int timeoutSeconds, List<String> allowedExtensions) {
        File dir = new File(downloadDir);
        Instant start = Instant.now();

        while (Duration.between(start, Instant.now()).getSeconds() < timeoutSeconds) {
            File[] files = dir.listFiles((d, name) -> {
                boolean matches = name.equalsIgnoreCase(expectedFileName);
                boolean allowed = allowedExtensions == null || allowedExtensions.stream().anyMatch(name::endsWith);
                return matches && allowed;
            });
            if (files != null && files.length > 0) return true;
            sleep(1000);
        }

        attachScreenshot("File download failed: " + expectedFileName);
        return false;
    }

    // Clean directory
    public void cleanDownloadDirectory(String downloadDirPath) {
        try {
            FileUtils.cleanDirectory(new File(downloadDirPath));
        } catch (IOException e) {
            test.warning("Could not clean directory: " + e.getMessage());
        }
    }

    // Return most recent file
    public File getLatestFileFromDir(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles(File::isFile);
        if (files == null || files.length == 0) return null;

        return Arrays.stream(files)
                .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()))
                .orElse(null);
    }

    // Download a remote file with retry logic + MIME detection + rename
    public boolean downloadFileFromUrl(String fileUrl, String destPath, int maxRetries) {
        int attempt = 0;
        while (attempt < maxRetries) {
            attempt++;
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.connect();

                if (conn.getResponseCode() != 200) {
                    test.warning("Attempt " + attempt + " failed: HTTP " + conn.getResponseCode());
                    continue;
                }

                String contentType = conn.getContentType();
                test.info("Detected MIME type: " + contentType);

                try (InputStream in = conn.getInputStream()) {
                    Files.copy(in, Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
                }

                test.pass("Downloaded file to: " + destPath);
                return true;

            } catch (IOException e) {
                test.warning("Retry " + attempt + ": " + e.getMessage());
                sleep(1000);
            }
        }

        attachScreenshot("Remote file download failed after " + maxRetries + " attempts");
        return false;
    }

    // Rename file
    public boolean renameFile(String oldPath, String newPath) {
        try {
            Files.move(Paths.get(oldPath), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);
            test.info("Renamed file to: " + newPath);
            return true;
        } catch (IOException e) {
            test.fail("Rename failed: " + e.getMessage());
            return false;
        }
    }

    // Screenshot helper
    private void attachScreenshot(String message) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            String path = "reports/screenshots/" + System.currentTimeMillis() + ".png";
            FileUtils.copyFile(src, new File(path));
            test.addScreenCaptureFromPath(path, message);
        } catch (IOException e) {
            test.warning("Screenshot failed: " + e.getMessage());
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
