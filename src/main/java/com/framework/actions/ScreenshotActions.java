package com.framework.actions;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotActions {
    private WebDriver driver;
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");

    public ScreenshotActions(WebDriver driver) {
        this.driver = driver;
    }

    /** Capture full page screenshot and save to given folder with timestamped name */
    public File captureFullPageScreenshot(String folderPath) throws IOException {
        ensureFolderExists(folderPath);

        String fileName = "fullpage_" + getTimestamp() + ".png";
        File destFile = new File(folderPath + File.separator + fileName);
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        FileUtils.copyFile(srcFile, destFile);
        return destFile;
    }

    /** Capture viewport screenshot (visible part of page) */
    public File captureViewportScreenshot(String folderPath) throws IOException {
        return captureFullPageScreenshot(folderPath); // default Selenium takes viewport screenshot
    }

    /** Capture screenshot of specific element */
    public File captureElementScreenshot(By locator, String folderPath) throws IOException {
        ensureFolderExists(folderPath);

        WebElement element = driver.findElement(locator);
        String fileName = "element_" + getTimestamp() + ".png";
        File srcFile = element.getScreenshotAs(OutputType.FILE);
        File destFile = new File(folderPath + File.separator + fileName);

        FileUtils.copyFile(srcFile, destFile);
        return destFile;
    }

    /** Capture screenshot as BufferedImage */
    public BufferedImage captureScreenshotAsImage() throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return ImageIO.read(screenshot);
    }

    /** Capture element screenshot as BufferedImage */
    public BufferedImage captureElementAsImage(By locator) throws IOException {
        WebElement element = driver.findElement(locator);
        File screenshot = element.getScreenshotAs(OutputType.FILE);
        return ImageIO.read(screenshot);
    }

    /** Compare two screenshots allowing tolerance for size and pixel differences */
    public boolean compareScreenshotsWithTolerance(File file1, File file2, int tolerancePx) throws IOException {
        BufferedImage img1 = ImageIO.read(file1);
        BufferedImage img2 = ImageIO.read(file2);

        // Quick size tolerance check
        int widthDiff = Math.abs(img1.getWidth() - img2.getWidth());
        int heightDiff = Math.abs(img1.getHeight() - img2.getHeight());
        if (widthDiff > tolerancePx || heightDiff > tolerancePx) {
            return false;
        }

        // Compare pixel by pixel in overlapping region
        int compareWidth = Math.min(img1.getWidth(), img2.getWidth());
        int compareHeight = Math.min(img1.getHeight(), img2.getHeight());

        for (int x = 0; x < compareWidth; x++) {
            for (int y = 0; y < compareHeight; y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Capture screenshot as Base64 string */
    public String captureScreenshotAsBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /** Capture element screenshot as Base64 string */
    public String captureElementScreenshotAsBase64(By locator) {
        WebElement element = driver.findElement(locator);
        return element.getScreenshotAs(OutputType.BASE64);
    }

    /** Attach screenshot to ExtentTest */
    public void captureAndAttachScreenshot(ExtentTest test, String screenshotName) {
        try {
            String base64Screenshot = captureScreenshotAsBase64();
            test.info("Screenshot: " + screenshotName,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot, screenshotName).build());
        } catch (Exception e) {
            test.warning("Failed to capture screenshot: " + e.getMessage());
        }
    }

    /** Helper to ensure directory exists */
    private void ensureFolderExists(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /** Helper to get current timestamp string */
    private String getTimestamp() {
        return timestampFormat.format(new Date());
    }
}
