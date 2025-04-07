package org.appium.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ExtentReport {

    private static String reportFileName;

    public static ExtentReports configReport() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        reportFileName = "ExtentReport_" + timestamp + ".html";
        String reportDir = System.getProperty("user.dir") + "/reports";

        String reportPath = reportDir + "/" + reportFileName;
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

        reporter.config().setReportName("Automation Test Report");
        reporter.config().setDocumentTitle("Test Results");

        ExtentReports report = new ExtentReports();
        report.attachReporter(reporter);
        report.setSystemInfo("Tester", "Lior");
        report.setSystemInfo("Environment", "QA");
        report.setSystemInfo("OS", System.getProperty("os.name"));
        report.setSystemInfo("Java Version", System.getProperty("java.version"));

        return report;
    }

    public static String getReportFileName() {
        return reportFileName;
    }
}
