package org.appium.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import org.appium.framework.BaseTest;
import org.appium.utils.AppiumUtils;
import org.testng.*;


public class ReportListeners extends AppiumUtils implements ITestListener, ISuiteListener {
    ExtentTest test;
    ExtentReports report = ExtentReport.configReport();
    AppiumDriver driver;
    String platformName;

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
        test = report.createTest(result.getMethod().getMethodName());

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
        test.log(Status.PASS, "Test Passed");
        DashboardLogger.log(result, getPlatformName(result), null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);
        test.log(Status.FAIL, result.getThrowable());
        String testName = result.getMethod().getMethodName();

        try {
            Object testInstance = result.getInstance();

            if (testInstance instanceof BaseTest) {
                driver = ((BaseTest) testInstance).getDriver();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve driver from test instance", e);
        }

//        try {
//            driver = (AppiumDriver) result.getTestClass().getRealClass().getField("driver")
//                    .get(result.getInstance());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        //test.addScreenCaptureFromPath(getScreeshotPath(testName, driver), testName); //It doesnt display the images
        String screenshot = getScreenshotBase64(driver);
        test.addScreenCaptureFromBase64String(screenshot, testName);
        DashboardLogger.log(result, getPlatformName(result), screenshot);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
        test.log(Status.SKIP, "Test Skipped");
        DashboardLogger.log(result, getPlatformName(result), null);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
    }

    @Override
    public void onStart(ISuite suite) {
        ISuiteListener.super.onStart(suite);
    }
    @Override
    public void onFinish(ISuite suite) {
        ISuiteListener.super.onFinish(suite);
        report.flush();
        DashboardLogger.saveToFile();
    }


    private String getPlatformName(ITestResult result) {
        try {
            Object testInstance = result.getInstance();

            if (testInstance instanceof BaseTest) {
                platformName = ((BaseTest) testInstance).getPlatformName();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve data from test instance", e);
        }
        return platformName;
    }
}
