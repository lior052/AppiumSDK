package org.appium.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class AppiumUtils {

    private AppiumDriverLocalService service;


    protected AppiumDriverLocalService startAppiumServer(String ipAddress, int port) {
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File("//usr//local//lib//node_modules//appium//build//lib//main.js"))
                .withIPAddress(ipAddress)
                .usingPort(port).build();
        service.start();
        return service;
    }

    protected void stopAppiumServer() {
        service.stop();
    }

    protected Properties getPropertiesData() throws IOException {
        Properties properties = new Properties();
        String propertiesFilePath = System.getProperty("user.dir") + "/src/test/resources/data.properties";

        FileInputStream input = new FileInputStream(propertiesFilePath);
        properties.load(input);
        return properties;
    }

    public void waitForElementDisplay(AppiumDriver driver, WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.attributeContains(element, "text", text));
        //element, "text", "Cart"));
    }

    public List<HashMap<String, String>> getJsonData(String jsonFile) throws IOException {
        String jsonString = Files.readString(Paths.get(jsonFile));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, new TypeReference<List<HashMap<String, String>>>() {});
    }

    protected String convertDoubleToString(Double amount) {
        return Double.toString(amount);
    }

    protected String getScreeshotPath(String testName, AppiumDriver driver) throws IOException {
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        String filePath = System.getProperty("user.dir")+ "/src/test/java/org/appium/reports/" + testName+ ".png";
        FileUtils.copyFile(screenshot, new File(filePath));
        return filePath;
    }

    protected String getScreenshotBase64(AppiumDriver driver) {
        return driver.getScreenshotAs(OutputType.BASE64);
    }

}
