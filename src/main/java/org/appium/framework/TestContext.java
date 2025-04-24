package org.appium.framework;

import io.appium.java_client.AppiumDriver;

public class TestContext {
    private static AppiumDriver driver;
    private static String platformName;

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static void setDriver(AppiumDriver d) {
        driver = d;
    }

    public static void setPlatformName(String platform) {
        platformName = platform;
    }

    public static String getPlatformName() {
        return platformName;
    }
}

