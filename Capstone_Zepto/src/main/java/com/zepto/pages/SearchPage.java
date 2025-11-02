package com.zepto.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {
    
    WebDriver driver;
    WebDriverWait wait;
    
    // Constructor
    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }
    
    // Methods
    public void clickSearchBox() {
        try {
            // Try provided XPath first
            WebElement searchBox = null;
            
            try {
                searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[1]/div/div/div/div/header/div/div[3]/a/span/span")));
            } catch (Exception e1) {
                // Try alternative locators
                try {
                    searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@type='text' and contains(@placeholder,'Search')]")));
                } catch (Exception e2) {
                    searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@placeholder='Search' or contains(@placeholder,'search')]")));
                }
            }
            
            if (searchBox != null) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", searchBox);
                Thread.sleep(500);
                searchBox.click();
                Thread.sleep(1000);
                System.out.println("Search box clicked successfully!");
            }
            
        } catch (Exception e) {
            System.err.println("Error clicking search box: " + e.getMessage());
        }
    }
    
    public void enterSearchText(String productName) {
        try {
            // Find search input field
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='text' and (contains(@placeholder,'Search') or contains(@placeholder,'search'))]")));
            
            searchInput.clear();
            searchInput.sendKeys(productName);
            Thread.sleep(2000);
            System.out.println("Entered search text: " + productName);
            
        } catch (Exception e) {
            System.err.println("Error entering search text: " + e.getMessage());
        }
    }
    
    public void pressEnter() {
        try {
            WebElement searchInput = driver.findElement(
                By.xpath("//input[@type='text' and (contains(@placeholder,'Search') or contains(@placeholder,'search'))]"));
            searchInput.sendKeys(Keys.ENTER);
            Thread.sleep(3000);
            System.out.println("Pressed Enter to search");
        } catch (Exception e) {
            System.err.println("Error pressing enter: " + e.getMessage());
        }
    }
    
    public void searchProduct(String productName) {
        clickSearchBox();
        enterSearchText(productName);
        pressEnter();
    }
    
    public boolean isProductDisplayed(String productName) {
        try {
            String xpath = String.format("//h4[contains(text(),'%s')] | //p[contains(text(),'%s')] | //*[contains(text(),'%s')]", 
                productName, productName, productName);
            WebElement product = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            return product.isDisplayed();
        } catch (Exception e) {
            System.out.println("Product not found in search results: " + productName);
            return false;
        }
    }
    
    public boolean areSearchResultsDisplayed() {
        try {
            WebElement results = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'product') or contains(@class,'item') or contains(@class,'card')]")));
            return results.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}