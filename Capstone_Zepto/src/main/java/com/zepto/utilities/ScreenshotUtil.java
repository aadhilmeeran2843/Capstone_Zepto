package com.zepto.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {
    
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String filePath = System.getProperty("user.dir") + "/screenshots/" + fileName;
        
        try {
            // Create screenshots directory if it doesn't exist
            File directory = new File(System.getProperty("user.dir") + "/screenshots/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Capture screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot saved: " + filePath);
            
            return filePath;
        } catch (IOException e) {
            System.err.println("Error capturing screenshot: " + e.getMessage());
            return null;
        }
    }
    
    public static String getScreenshotForReport(WebDriver driver, String screenshotName) {
        String screenshotPath = captureScreenshot(driver, screenshotName);
        return screenshotPath != null ? screenshotPath : "";
    }
}