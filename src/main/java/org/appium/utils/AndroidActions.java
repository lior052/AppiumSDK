package org.appium.utils;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class AndroidActions extends AppiumUtils {

    AndroidDriver driver;

    public AndroidActions(AndroidDriver driver) {
        this.driver = driver;
    }

    public void longPressAction(WebElement element) {
        driver.executeScript("mobile: longClickGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(), "duration", "2000"
        ));
    }

    public void scrollToElementAction(String text) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector()).scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
    }

    public void scrollToEndAction() {
        Boolean canScrollMore;
        do {
            canScrollMore = (Boolean) driver.executeScript("mobile: scrollGesture", ImmutableMap.of(
                    "left", 100, "top", 100, "width", 200, "height", 200,
                    "direction", "down",
                    "percent", 1.0
            ));
        }
        while (Boolean.TRUE.equals(canScrollMore)); // Safe check to avoid NullPointerException
    }


    public void swipeAction(WebElement element, String direction) {
        //after swiping the focusable of the elements is changed from the current element to the swipe element
        driver.executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(),
                "direction", direction,
                "percent", 0.5
        ));
    }

    public void dragAndDropAction(WebElement fromElement, WebElement toElement) {
        driver.executeScript("mobile: dragGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) fromElement).getId(),
                "endX", toElement.getLocation().x,
                "endY", toElement.getLocation().y
        ));
    }
}
