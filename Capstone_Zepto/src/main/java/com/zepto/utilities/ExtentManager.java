package com.zepto.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.zepto.base.BaseTest;

public class ExtentManager implements ITestListener {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    public static ExtentReports createInstance() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_" + timestamp + ".html";
        
        // Create reports directory if it doesn't exist
        File directory = new File(System.getProperty("user.dir") + "/reports/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        
        // Configuration
        sparkReporter.config().setDocumentTitle("Zepto Automation Test Report");
        sparkReporter.config().setReportName("Zepto Functional Test Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // System Information
        extent.setSystemInfo("Application", "Zepto");
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Environment", "QA");
        
        return extent;
    }
    
    @Override
    public void onStart(ITestContext context) {
        extent = createInstance();
        System.out.println("Test Suite started: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        test.get().log(Status.INFO, "Test Started: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, 
            MarkupHelper.createLabel(result.getMethod().getMethodName() + " - PASSED", ExtentColor.GREEN));
        
        // Capture screenshot for passed test
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            BaseTest baseTest = (BaseTest) testClass;
            if (baseTest.driver != null) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(
                    baseTest.driver, result.getMethod().getMethodName() + "_PASS");
                if (screenshotPath != null) {
                    test.get().addScreenCaptureFromPath(screenshotPath);
                }
            }
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, 
            MarkupHelper.createLabel(result.getMethod().getMethodName() + " - FAILED", ExtentColor.RED));
        test.get().log(Status.FAIL, result.getThrowable());
        
        // Capture screenshot for failed test
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            BaseTest baseTest = (BaseTest) testClass;
            if (baseTest.driver != null) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(
                    baseTest.driver, result.getMethod().getMethodName() + "_FAIL");
                if (screenshotPath != null) {
                    test.get().addScreenCaptureFromPath(screenshotPath);
                }
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, 
            MarkupHelper.createLabel(result.getMethod().getMethodName() + " - SKIPPED", ExtentColor.ORANGE));
        test.get().log(Status.SKIP, result.getThrowable());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        System.out.println("Test Suite finished: " + context.getName());
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
}