package com.zepto.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {
    
    WebDriver driver;
    WebDriverWait wait;
    
    // Constructor
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }
    
    // Methods
    public void addFirstProductToCart() {
        try {
            Thread.sleep(3000);
            
            // Try provided XPath first
            WebElement addButton = null;
            
            try {
                addButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='left-carousel']/div[2]/div/div[2]/button")));
            } catch (Exception e1) {
                // Try to find any "Add" button
                List<WebElement> addButtons = driver.findElements(
                    By.xpath("//button[contains(text(),'Add') or contains(text(),'ADD')]"));
                
                if (addButtons.size() > 0) {
                    addButton = addButtons.get(0);
                } else {
                    System.err.println("No Add to Cart button found");
                    return;
                }
            }
            
            if (addButton != null) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", addButton);
                Thread.sleep(1000);
                
                js.executeScript("arguments[0].click();", addButton);
                Thread.sleep(2000);
                System.out.println("Product added to cart successfully!");
            }
            
        } catch (Exception e) {
            System.err.println("Error adding product to cart: " + e.getMessage());
        }
    }
    
    public void addProductToCartByName(String productName) {
        try {
            String xpath = String.format(
                "//h4[contains(text(),'%s')]/ancestor::div[contains(@class,'product')]//button[contains(text(),'Add')] | " +
                "//p[contains(text(),'%s')]/ancestor::div[contains(@class,'card')]//button[contains(text(),'Add')]",
                productName, productName);
            
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", addButton);
            Thread.sleep(1000);
            
            js.executeScript("arguments[0].click();", addButton);
            Thread.sleep(2000);
            System.out.println("Product added to cart: " + productName);
        } catch (Exception e) {
            System.err.println("Error adding product to cart: " + e.getMessage());
        }
    }
    
    public void clickCartIcon() {
        try {
            // Try provided XPath first
            WebElement cartIcon = null;
            
            try {
                cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[1]/div/div/header/div/div[5]/div/div[1]/button/svg/path[2]")));
            } catch (Exception e1) {
                // Try parent button element
                try {
                    cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div[1]/div/div/header/div/div[5]/div/div[1]/button")));
                } catch (Exception e2) {
                    // Try alternative locators
                    cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@aria-label,'cart') or contains(@aria-label,'Cart')]")));
                }
            }
            
            if (cartIcon != null) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", cartIcon);
                Thread.sleep(3000);
                System.out.println("Cart icon clicked successfully!");
            }
            
        } catch (Exception e) {
            System.err.println("Error clicking cart icon: " + e.getMessage());
        }
    }
    
    public boolean isCartAccessible() {
        try {
            clickCartIcon();
            // Check if cart page or cart section is displayed
            WebElement cartSection = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h1[contains(text(),'Cart')] | " +
                         "//h2[contains(text(),'Cart')] | " +
                         "//div[contains(@class,'cart-container')] | " +
                         "//div[contains(text(),'Your Cart')] | " +
                         "//button[contains(text(),'Proceed')]")));
            return cartSection.isDisplayed();
        } catch (Exception e) {
            System.err.println("Cart is not accessible: " + e.getMessage());
            return false;
        }
    }
    
    public int getCartItemCount() {
        try {
            List<WebElement> items = driver.findElements(
                By.xpath("//div[contains(@class,'cart-item') or contains(@class,'cart-product')]"));
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public void clickProceedButton() {
        try {
            WebElement proceedBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Proceed') or contains(text(),'Checkout')]")));
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", proceedBtn);
            Thread.sleep(3000);
            System.out.println("Proceed button clicked");
        } catch (Exception e) {
            System.err.println("Error clicking proceed button: " + e.getMessage());
        }
    }
    
    public boolean isProductInCart(String productName) {
        try {
            String xpath = String.format("//*[contains(text(),'%s')]", productName);
            WebElement product = driver.findElement(By.xpath(xpath));
            return product.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}