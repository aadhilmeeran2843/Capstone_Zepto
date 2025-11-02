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

public class SuperSaverPage {
    
    WebDriver driver;
    WebDriverWait wait;
    
    // Constructor
    public SuperSaverPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }
    
    // Methods
    public void enableSuperSaver() {
        try {
            // Try provided XPath first
            WebElement superSaverToggle = null;
            
            try {
                superSaverToggle = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[1]/div/div/header/div/div[1]/button/div/div/div")));
            } catch (Exception e1) {
                // Try alternative locators
                try {
                    superSaverToggle = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(),'Super Saver')]")));
                } catch (Exception e2) {
                    superSaverToggle = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'toggle') and contains(.,'Super Saver')]")));
                }
            }
            
            if (superSaverToggle != null) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", superSaverToggle);
                Thread.sleep(1000);
                
                // Check if already enabled
                String classes = superSaverToggle.getAttribute("class");
                String ariaChecked = superSaverToggle.getAttribute("aria-checked");
                
                if ((classes != null && !classes.contains("active") && !classes.contains("checked")) ||
                    (ariaChecked != null && ariaChecked.equals("false"))) {
                    superSaverToggle.click();
                    Thread.sleep(2000);
                    System.out.println("Super Saver enabled successfully!");
                } else {
                    System.out.println("Super Saver already enabled");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error enabling Super Saver: " + e.getMessage());
        }
    }
    
    public void clickSuperSaverButton() {
        try {
            WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div/div/header/div/div[1]/button/div/div/div")));
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", toggle);
            Thread.sleep(1000);
            toggle.click();
            Thread.sleep(2000);
            System.out.println("Super Saver button clicked");
        } catch (Exception e) {
            System.err.println("Error clicking Super Saver button: " + e.getMessage());
        }
    }
    
    public boolean isSuperSaverEnabled() {
        try {
            WebElement toggle = driver.findElement(
                By.xpath("//button[contains(@class,'active') and contains(text(),'Super Saver')] | " +
                         "//input[@type='checkbox' and contains(@id,'supersaver') and @checked] | " +
                         "//div[contains(@class,'toggle') and contains(@class,'active')]"));
            return toggle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public void scrollToSuperSaver() {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("/html/body/div[1]/div/div/header/div/div[1]/button/div/div/div")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Error scrolling to Super Saver: " + e.getMessage());
        }
    }
}