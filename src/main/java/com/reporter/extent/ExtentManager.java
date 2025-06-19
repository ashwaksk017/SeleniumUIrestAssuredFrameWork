package com.reporter.extent;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private ExtentManager() {
        // prevent instantiation
    }

    public static synchronized ExtentReports getExtentReports() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent.html");
            spark.config().setDocumentTitle("Test Automation Report");
            spark.config().setReportName("Combined UI & API Tests");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Framework", "Hybrid (Selenium + RestAssured)");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Author", "YourName");
        }
        return extent;
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
