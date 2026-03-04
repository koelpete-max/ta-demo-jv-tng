package com.example.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ReportManager {

    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

    public static void startTest(String name) {
        ExtentReports extent = ExtentManager.getInstance();
        ExtentTest test = extent.createTest(name);
        CURRENT.set(test);
    }

    public static ExtentTest getTest() {
        return CURRENT.get();
    }

    public static void unload() {
        CURRENT.remove();
    }

    public static void flushReports() {
        ExtentManager.flush();
    }
}