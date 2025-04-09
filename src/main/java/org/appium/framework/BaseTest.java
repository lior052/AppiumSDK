package org.appium.framework;

import io.appium.java_client.AppiumDriver;
import org.appium.utils.AppiumUtils;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

//temp comment1
public abstract class BaseTest extends AppiumUtils {

    protected AppiumDriver driver;
    private DriverManager driverManager;
    private String platformName;


    //@BeforeSuite(alwaysRun = true)
    @BeforeClass(alwaysRun = true)
    @Parameters({"platformName"})
    public void setup(@Optional("iOS") String platformName) throws IOException {
        if (System.getenv("platformName") != null) {
            this.platformName = System.getenv("platformName");
        }
        else {
            this.platformName = platformName;
        }

        if (driverManager == null) {
            driverManager = new DriverManager();
        }
        if (driver == null) {
            setDriver(this.platformName);
        }
    }


    @AfterSuite(alwaysRun = true)
    public void stopAppium() {
        if (driverManager != null) {
            driverManager.stopAppiumServer();
        }
    }

    public AppiumDriver getDriver() throws IOException {
        if (driver == null) {
            System.out.println("Driver is null, initializing...");
            if (driverManager == null) {
                driverManager = new DriverManager();
            }
            setDriver(platformName);
        }
        return driver;
    }

    private void setDriver(String platformName) throws IOException {
        if (platformName == null) {
            if (System.getenv("platformName") != null) {
                platformName = System.getenv("platformName");
            } else {
                platformName = "iOS"; // Default if not set
            }
        }
        Properties properties = getPropertiesData();

        driver = driverManager.initializeDriver(platformName, getPort(), properties);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    private int getPort() {
        Properties properties = null;
        String port;
        try {
            properties = getPropertiesData();
            port = this.platformName.equalsIgnoreCase("iOS") ?
                    properties.getProperty("iosAppiumPort") :
                    properties.getProperty("androidAppiumPort");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Integer.parseInt(port);
    }

    public String getPlatformName() {
        return this.platformName;
    }
}
