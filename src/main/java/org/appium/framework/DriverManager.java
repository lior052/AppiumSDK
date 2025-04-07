package org.appium.framework;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.appium.utils.AppiumUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class DriverManager extends AppiumUtils {

    private AppiumDriverLocalService driver;


    //public AppiumDriver initializeDriver(String platformName, Properties properties) throws IOException {
    public AppiumDriver initializeDriver(String platformName, int port, Properties properties) throws IOException {
        driver = startAppiumServer(properties.getProperty("ipAddress"), port);
        //driver = startAppiumServer(properties.getProperty("ipAddress"), Integer.parseInt(properties.getProperty("port")));

        if (platformName.equalsIgnoreCase("iOS")) {
            return setupIOSDriver(properties);
        } else if (platformName.equalsIgnoreCase("Android")) {
            return setupAndroidDriver(properties);
        } else {
            throw new IllegalArgumentException("Invalid platform: " + platformName);
        }
    }

    private IOSDriver setupIOSDriver(Properties properties) {
        XCUITestOptions options = new XCUITestOptions();
        options.setApp(properties.getProperty("iosAppPath"));
        options.setDeviceName(properties.getProperty("iosSimulatorName"));
        options.setPlatformVersion(properties.getProperty("iosPlatformVersion"));
        options.setWdaLaunchTimeout(Duration.ofSeconds(20));

        return new IOSDriver(driver.getUrl(), options);
    }

    private AndroidDriver setupAndroidDriver(Properties properties) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName(properties.getProperty("androidEmulatorName"));
        options.setApp(properties.getProperty("androidAppPath"));
        options.setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(20));

        return new AndroidDriver(driver.getUrl(), options);
    }

    public void stopAppiumServer() {
        if (driver != null) {
            driver.stop();
        }
    }
}
