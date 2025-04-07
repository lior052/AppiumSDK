package org.appium.utils;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.ios.IOSDriver;
import org.appium.utils.AppiumUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;
import java.util.Map;

public class IOSActions extends AppiumUtils {

    IOSDriver driver;


    public IOSActions(IOSDriver driver) {
        this.driver = driver;
    }

    public void longPressAction(WebElement element, int duration) {
        driver.executeScript("mobile: touchAndHold", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(), "duration", duration
        ));
    }

    public void scrollToElementAction(WebElement element, String direction) {
        Map<String, Object> params = new HashMap<>();
        params.put("element", ((RemoteWebElement) element).getId());
        params.put("direction", direction.toLowerCase());
        params.put("percent", 1.0);

        Boolean canScrollMore;
        do {
            canScrollMore = (Boolean) driver.executeScript("mobile: scroll", params);
        }
        while (Boolean.TRUE.equals(canScrollMore));
    }


    public void swipeAction(WebElement element, String direction) {
        driver.executeScript("mobile: swipe", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(),
                "direction", direction.toLowerCase()
        ));
    }

    public void swipeAction(String direction) {
        driver.executeScript("mobile: swipe", ImmutableMap.of(
                "direction", direction.toLowerCase()
        ));
    }

    public void swipeFullyElement(WebElement element, String direction) {
        Point location = element.getLocation();
        Dimension size = element.getSize();

        int startX, startY, endX, endY;

        switch (direction.toLowerCase()) {
            case "left":
                startX = location.getX() + size.getWidth() - 5; // Start from near the right edge
                endX = location.getX() + 5; // Move fully to the left edge
                startY = endY = location.getY() + size.getHeight() / 2; // Center Y
                break;
            case "right":
                startX = location.getX() + 5; // Start from near the left edge
                endX = location.getX() + size.getWidth() - 5; // Move fully to the right edge
                startY = endY = location.getY() + size.getHeight() / 2; // Center Y
                break;
            case "up":
                startX = endX = location.getX() + size.getWidth() / 2; // Center X
                startY = location.getY() + size.getHeight() - 5; // Start near the bottom edge
                endY = location.getY() + 5; // Move fully to the top edge
                break;
            case "down":
                startX = endX = location.getX() + size.getWidth() / 2; // Center X
                startY = location.getY() + 5; // Start near the top edge
                endY = location.getY() + size.getHeight() - 5; // Move fully to the bottom edge
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        Map<String, Object> params = Map.of(
                "fromX", startX,
                "fromY", startY,
                "toX", endX,
                "toY", endY,
                "duration", 1.0
        );

        driver.executeScript("mobile: dragFromToForDuration", params);
    }

}
