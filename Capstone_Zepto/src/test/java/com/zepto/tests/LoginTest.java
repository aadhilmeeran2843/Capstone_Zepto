package com.zepto.tests;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zepto.base.BaseTest;
import com.zepto.pages.LoginPage;
import com.zepto.utilities.ExcelUtils;
import com.zepto.utilities.ExtentManager;

public class LoginTest extends BaseTest {
    
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        String filePath = System.getProperty("user.dir") + File.separator + "testdata" + File.separator + "testdata.xlsx";
        Object[][] data = ExcelUtils.getTestData(filePath, "LoginData");
        
        // If no data found, return default data to prevent test failure
        if (data.length == 0) {
            System.err.println("WARNING: No login data found in Excel. Using default mobile number.");
            System.err.println("Please run CreateExcelHelper.java to create testdata.xlsx file!");
            return new Object[][] {{"9876543210"}};
        }
        
        return data;
    }
    
    @Test(priority = 1, dataProvider = "loginData")
    public void testLoginWithMobileNumber(String mobileNumber) {
        ExtentManager.getTest().info("Starting Login Test with Mobile: " + mobileNumber);
        
        try {
            LoginPage loginPage = new LoginPage(driver);
            
            // Check if cookies are available and user is already logged in
            if (areCookiesAvailable()) {
                ExtentManager.getTest().info("Cookies found, attempting to reuse session");
                loadCookies();
                driver.navigate().refresh();
                waitForPageLoad(3);
                
                if (loginPage.isLoggedIn()) {
                    ExtentManager.getTest().pass("User is already logged in using saved cookies");
                    Assert.assertTrue(true, "Login successful using cookies");
                    return;
                }
            }
            
            // Perform manual login if cookies don't work
            ExtentManager.getTest().info("No valid cookies found. Manual login required.");
            System.out.println("\n" + "=".repeat(80));
            System.out.println("📱 MANUAL LOGIN REQUIRED");
            System.out.println("=".repeat(80));
            System.out.println("Mobile Number from Excel: " + mobileNumber);
            System.out.println("=".repeat(80) + "\n");
            
            // Click login button
            loginPage.clickLoginButton();
            
            // Wait for user to manually complete login (90 seconds)
            loginPage.waitForManualLoginEntry();
            
            // Verify login success
            boolean isLoggedIn = loginPage.isLoggedIn();
            
            if (isLoggedIn) {
                // Save cookies for future use
                saveCookies();
                ExtentManager.getTest().pass("Login successful and cookies saved");
                System.out.println("\n✓✓✓ Login successful! Cookies saved for future runs.\n");
            } else {
                ExtentManager.getTest().warning("Could not verify login status. Please check manually.");
                System.out.println("\n⚠ Could not verify login. Please check browser manually.\n");
            }
            
            Assert.assertTrue(isLoggedIn, "User should be logged in successfully");
            
        } catch (Exception e) {
            ExtentManager.getTest().fail("Login test failed: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 2, dependsOnMethods = "testLoginWithMobileNumber")
    public void testLoginPersistence() {
        ExtentManager.getTest().info("Testing login persistence with saved cookies");
        
        try {
            LoginPage loginPage = new LoginPage(driver);
            
            if (areCookiesAvailable()) {
                loadCookies();
                driver.navigate().refresh();
                waitForPageLoad(3);
                
                boolean isLoggedIn = loginPage.isLoggedIn();
                Assert.assertTrue(isLoggedIn, "User should remain logged in after page refresh");
                ExtentManager.getTest().pass("Login persistence verified successfully");
            } else {
                ExtentManager.getTest().skip("No cookies available for persistence test");
            }
            
        } catch (Exception e) {
            ExtentManager.getTest().fail("Login persistence test failed: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
}