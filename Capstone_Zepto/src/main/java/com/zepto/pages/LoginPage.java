package com.zepto.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    
    WebDriver driver;
    WebDriverWait wait;
    
    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }
    
    // Methods
    public void clickLoginButton() {
        try {
            // Try multiple locators for login button
            WebElement loginBtn = null;
            
            // Try provided XPath first
            try {
                loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[1]/div/div/div/div/header/div/div[4]/button/div")));
            } catch (Exception e1) {
                // Try alternative locators
                try {
                    loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(),'Login') or contains(text(),'Sign')]")));
                } catch (Exception e2) {
                    try {
                        loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(@class,'login') or contains(@aria-label,'Login')]")));
                    } catch (Exception e3) {
                        System.out.println("Login button not found with any locator. User may already be logged in.");
                        return;
                    }
                }
            }
            
            if (loginBtn != null) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", loginBtn);
                Thread.sleep(1000);
                loginBtn.click();
                Thread.sleep(2000);
                System.out.println("Login button clicked successfully!");
            }
            
        } catch (Exception e) {
            System.out.println("Login button not found or already logged in");
        }
    }
    
    public void waitForManualLoginEntry() {
        try {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("⏰ MANUAL LOGIN REQUIRED");
            System.out.println("=".repeat(80));
            System.out.println("📱 Please complete the following steps in the browser window:");
            System.out.println("   1. Click on the Login button (if not already clicked)");
            System.out.println("   2. Enter your mobile number");
            System.out.println("   3. Click Continue/Send OTP button");
            System.out.println("   4. Enter the OTP you receive");
            System.out.println("   5. Complete the login process");
            System.out.println("\n⏱️  You have 90 SECONDS to complete this process...");
            System.out.println("=".repeat(80) + "\n");
            
            // Give user 90 seconds to manually login
            for (int i = 90; i > 0; i--) {
                if (i % 15 == 0) {
                    System.out.println("⏰ " + i + " seconds remaining...");
                }
                Thread.sleep(1000);
                
                // Check if user is logged in every 5 seconds
                if (i % 5 == 0 && isLoggedIn()) {
                    System.out.println("\n✓ Login detected! Proceeding with test...\n");
                    return;
                }
            }
            
            System.out.println("\n⏰ Manual login time expired. Checking login status...\n");
            
        } catch (Exception e) {
            System.err.println("Error in manual login wait: " + e.getMessage());
        }
    }
    
    public boolean isLoggedIn() {
        try {
            // Try multiple ways to detect if user is logged in
            
            // Method 1: Check for profile/account button
            try {
                WebElement profile = driver.findElement(
                    By.xpath("//button[contains(@aria-label,'Profile') or contains(@aria-label,'Account')]"));
                if (profile.isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {}
            
            // Method 2: Check if login button is NOT present
            try {
                driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
                return false; // Login button present means not logged in
            } catch (Exception e) {
                // Login button not found, might be logged in
            }
            
            // Method 3: Check for cart or other logged-in features
            try {
                WebElement cart = driver.findElement(
                    By.xpath("//button[contains(@aria-label,'cart') or contains(@aria-label,'Cart')]"));
                if (cart.isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {}
            
            // Method 4: Check page source for logged-in indicators
            String pageSource = driver.getPageSource().toLowerCase();
            if (pageSource.contains("logout") || pageSource.contains("my account") || 
                pageSource.contains("profile")) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error checking login status: " + e.getMessage());
            return false;
        }
    }
    
    public void login(String mobileNumber) {
        clickLoginButton();
        waitForManualLoginEntry();
    }
}