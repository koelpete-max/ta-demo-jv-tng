package com.example.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports INSTANCE;

    public static synchronized void init() {
        if (INSTANCE == null) {
            ExtentSparkReporter reporter =
                    new ExtentSparkReporter("test-output/ExtentReport.html");
            ExtentReports extent = new ExtentReports();
            extent.attachReporter(reporter);
            INSTANCE = extent;
        }
    }

    public static ExtentReports getInstance() {
        return INSTANCE;
    }

    public static synchronized void flush() {
        if (INSTANCE != null) {
            INSTANCE.flush();
        }
    }
}