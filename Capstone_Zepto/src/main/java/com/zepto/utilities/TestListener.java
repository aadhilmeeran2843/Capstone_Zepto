package com.zepto.utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.zepto.base.BaseTest;

public class TestListener implements ITestListener {
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("========================================");
        System.out.println("Test Suite Started: " + context.getName());
        System.out.println("========================================");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getMethod().getMethodName());
        ExtentManager.getTest().log(Status.INFO, 
            "Test Execution Started: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✓ Test PASSED: " + result.getMethod().getMethodName());
        
        ExtentManager.getTest().log(Status.PASS, 
            MarkupHelper.createLabel(result.getMethod().getMethodName() + " - TEST PASSED", 
            ExtentColor.GREEN));
        
        // Capture screenshot for passed test
        captureScreenshot(result, "PASS");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("✗ Test FAILED: " + result.getMethod().getMethodName());
        System.out.println("Reason: " + result.getThrowable().getMessage());
        
        ExtentManager.getTest().log(Status.FAIL, 
            MarkupHelper.createLabel(result.getMethod().getMethodName() + " - TEST FAILED", 
            ExtentColor.RED));
        
        ExtentManager.getTest().log(Status.FAIL, 
            "Test Failure Reason: " + result.getThrowable());
        
        // Capture screenshot for failed test
        captureScreenshot(result, "FAIL");
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⊘ Test SKIPPED: " + result.getMethod().getMethodName());
        
        ExtentManager.getTest().log(Status.SKIP, 
            MarkupHelper.createLabel(result.getMethod().getMethodName() + " - TEST SKIPPED", 
            ExtentColor.ORANGE));
        
        if (result.getThrowable() != null) {
            ExtentManager.getTest().log(Status.SKIP, 
                "Skip Reason: " + result.getThrowable());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test Failed but within success percentage: " + 
            result.getMethod().getMethodName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("========================================");
        System.out.println("Test Suite Finished: " + context.getName());
        System.out.println("Total Tests Run: " + context.getAllTestMethods().length);
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("========================================");
    }
    
    // Helper method to capture screenshots
    private void captureScreenshot(ITestResult result, String status) {
        try {
            Object testClass = result.getInstance();
            if (testClass instanceof BaseTest) {
                BaseTest baseTest = (BaseTest) testClass;
                if (baseTest.driver != null) {
                    String screenshotPath = ScreenshotUtil.captureScreenshot(
                        baseTest.driver, 
                        result.getMethod().getMethodName() + "_" + status);
                    
                    if (screenshotPath != null) {
                        ExtentManager.getTest().addScreenCaptureFromPath(screenshotPath);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error capturing screenshot: " + e.getMessage());
        }
    }
}