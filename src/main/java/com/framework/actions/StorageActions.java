package com.framework.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class StorageActions {
    private WebDriver driver;
    private JavascriptExecutor jsExecutor;

    public StorageActions(WebDriver driver) {
        this.driver = driver;
        if (driver instanceof JavascriptExecutor) {
            this.jsExecutor = (JavascriptExecutor) driver;
        } else {
            throw new IllegalArgumentException("Driver does not support JavaScript execution");
        }
    }

    // ===================== LOCAL STORAGE =====================

    /**
     * Get the number of items in local storage
     */
    public Long getLocalStorageSize() {
        return (Long) jsExecutor.executeScript("return window.localStorage.length;");
    }

    /**
     * Get an item from local storage by key
     */
    public String getLocalStorageItem(String key) {
        return (String) jsExecutor.executeScript("return window.localStorage.getItem(arguments[0]);", key);
    }

    /**
     * Set an item in local storage
     */
    public void setLocalStorageItem(String key, String value) {
        jsExecutor.executeScript("window.localStorage.setItem(arguments[0], arguments[1]);", key, value);
    }

    /**
     * Remove an item from local storage by key
     */
    public void removeLocalStorageItem(String key) {
        jsExecutor.executeScript("window.localStorage.removeItem(arguments[0]);", key);
    }

    /**
     * Clear all items from local storage
     */
    public void clearLocalStorage() {
        jsExecutor.executeScript("window.localStorage.clear();");
    }

    /**
     * Get all keys from local storage
     */
    @SuppressWarnings("unchecked")
    public Set<String> getLocalStorageKeys() {
        return (Set<String>) jsExecutor.executeScript(
                "var keys = []; " +
                "for(var i=0; i<window.localStorage.length; i++) { keys.push(window.localStorage.key(i)); } " +
                "return keys;");
    }

    /**
     * Get all key-value pairs from local storage
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getAllLocalStorageItems() {
        Map<String, String> map = new HashMap<>();
        Long length = getLocalStorageSize();
        for (int i = 0; i < length; i++) {
            String key = (String) jsExecutor.executeScript("return window.localStorage.key(arguments[0]);", i);
            String value = getLocalStorageItem(key);
            map.put(key, value);
        }
        return map;
    }

    // ===================== SESSION STORAGE =====================

    /**
     * Get the number of items in session storage
     */
    public Long getSessionStorageSize() {
        return (Long) jsExecutor.executeScript("return window.sessionStorage.length;");
    }

    /**
     * Get an item from session storage by key
     */
    public String getSessionStorageItem(String key) {
        return (String) jsExecutor.executeScript("return window.sessionStorage.getItem(arguments[0]);", key);
    }

    /**
     * Set an item in session storage
     */
    public void setSessionStorageItem(String key, String value) {
        jsExecutor.executeScript("window.sessionStorage.setItem(arguments[0], arguments[1]);", key, value);
    }

    /**
     * Remove an item from session storage by key
     */
    public void removeSessionStorageItem(String key) {
        jsExecutor.executeScript("window.sessionStorage.removeItem(arguments[0]);", key);
    }

    /**
     * Clear all items from session storage
     */
    public void clearSessionStorage() {
        jsExecutor.executeScript("window.sessionStorage.clear();");
    }

    /**
     * Get all keys from session storage
     */
    @SuppressWarnings("unchecked")
    public Set<String> getSessionStorageKeys() {
        return (Set<String>) jsExecutor.executeScript(
                "var keys = []; " +
                "for(var i=0; i<window.sessionStorage.length; i++) { keys.push(window.sessionStorage.key(i)); } " +
                "return keys;");
    }

    /**
     * Get all key-value pairs from session storage
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getAllSessionStorageItems() {
        Map<String, String> map = new HashMap<>();
        Long length = getSessionStorageSize();
        for (int i = 0; i < length; i++) {
            String key = (String) jsExecutor.executeScript("return window.sessionStorage.key(arguments[0]);", i);
            String value = getSessionStorageItem(key);
            map.put(key, value);
        }
        return map;
    }

    // ===================== ADDITIONAL UTILITIES =====================

    /**
     * Check if a key exists in local storage
     */
    public boolean isLocalStorageKeyPresent(String key) {
        return getLocalStorageItem(key) != null;
    }

    /**
     * Check if a key exists in session storage
     */
    public boolean isSessionStorageKeyPresent(String key) {
        return getSessionStorageItem(key) != null;
    }
}
