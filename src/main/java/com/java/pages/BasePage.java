package com.java.pages;

import org.openqa.selenium.WebDriver;

import com.framework.actions.AlertActions;
import com.framework.actions.ElementActions;
import com.framework.actions.FileTransferActions;
import com.framework.actions.FrameActions;
import com.framework.actions.JSExecutorActions;
import com.framework.actions.NavigationActions;
import com.framework.actions.ScreenshotActions;
import com.framework.actions.StorageActions;
import com.framework.actions.WaitActions;
import com.framework.actions.WindowActions;

public class BasePage {
    protected WebDriver driver;
    protected NavigationActions navigation;
    protected ElementActions element;
    protected WaitActions wait;
    protected AlertActions alert;
    protected FrameActions frame;
    protected JSExecutorActions js;
    protected ScreenshotActions snap;
    protected WindowActions window;
    protected StorageActions storage;
    protected FileTransferActions file;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.navigation = new NavigationActions(driver);
        this.element = new ElementActions(driver);
        this.wait = new WaitActions(driver);
        this.alert = new AlertActions(driver);
        this.frame = new FrameActions(driver);
        this.js = new JSExecutorActions(driver);
        this.snap = new ScreenshotActions(driver);
        this.window = new WindowActions(driver);
        this.storage = new StorageActions(driver);
        this.file = new FileTransferActions(driver);
    }
}
