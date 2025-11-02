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

public class AddressPage {
    
    WebDriver driver;
    WebDriverWait wait;
    
    // Constructor
    public AddressPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }
    
    // Methods
    public void clickAddAddressButton() {
        try {
            // Try provided XPath first
            WebElement addAddressBtn = null;
            
            try {
                addAddressBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='radix-«r2c»']/div/div/div/div[8]/div/button")));
            } catch (Exception e1) {
                // Try alternative locators
                try {
                    addAddressBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(),'Add Address') or contains(text(),'Add New Address')]")));
                } catch (Exception e2) {
                    System.out.println("Add Address button not found");
                    return;
                }
            }
            
            if (addAddressBtn != null) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", addAddressBtn);
                Thread.sleep(1000);
                addAddressBtn.click();
                Thread.sleep(2000);
                System.out.println("Add Address button clicked");
            }
            
        } catch (Exception e) {
            System.err.println("Error clicking Add Address button: " + e.getMessage());
        }
    }
    
    public void clickAddressInput() {
        try {
            WebElement addressField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='Enter delivery address' or contains(@placeholder,'address') or @type='text']")));
            addressField.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Error clicking address input: " + e.getMessage());
        }
    }
    
    public void enterAddress(String address) {
        try {
            WebElement addressField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter delivery address' or contains(@placeholder,'address') or @type='text']")));
            addressField.clear();
            addressField.sendKeys(address);
            Thread.sleep(2000);
            System.out.println("Address entered: " + address);
        } catch (Exception e) {
            System.err.println("Error entering address: " + e.getMessage());
        }
    }
    
    public void selectFirstAddressSuggestion() {
        try {
            Thread.sleep(2000);
            WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class,'suggestion') or contains(@class,'autocomplete')])[1] | " +
                         "(//li[contains(@class,'address-item')])[1]")));
            suggestion.click();
            Thread.sleep(2000);
            System.out.println("First address suggestion selected");
        } catch (Exception e) {
            // If no suggestion found, press Enter
            try {
                WebElement addressField = driver.findElement(
                    By.xpath("//input[@placeholder='Enter delivery address' or contains(@placeholder,'address')]"));
                addressField.sendKeys(Keys.ENTER);
                Thread.sleep(2000);
            } catch (Exception ex) {
                System.err.println("Error selecting address suggestion: " + ex.getMessage());
            }
        }
    }
    
    public void enterHouseDetails(String houseNo, String landmark) {
        try {
            WebElement houseField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='House No.' or @placeholder='Flat/House No.' or contains(@placeholder,'house')]")));
            houseField.clear();
            houseField.sendKeys(houseNo);
            Thread.sleep(1000);
            
            WebElement landmarkField = driver.findElement(
                By.xpath("//input[@placeholder='Landmark' or contains(@placeholder,'landmark')]"));
            landmarkField.clear();
            landmarkField.sendKeys(landmark);
            Thread.sleep(1000);
            System.out.println("House details entered");
        } catch (Exception e) {
            System.err.println("Error entering house details: " + e.getMessage());
        }
    }
    
    public void clickSaveButton() {
        try {
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Save') or contains(text(),'Submit') or contains(text(),'Continue')]")));
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", saveBtn);
            Thread.sleep(1000);
            
            saveBtn.click();
            Thread.sleep(3000);
            System.out.println("Save button clicked");
        } catch (Exception e) {
            System.err.println("Error clicking save button: " + e.getMessage());
        }
    }
    
    public void clickDeliverHereButton() {
        try {
            WebElement deliverBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Deliver Here') or contains(text(),'Select') or contains(text(),'Proceed')]")));
            deliverBtn.click();
            Thread.sleep(2000);
            System.out.println("Deliver Here button clicked");
        } catch (Exception e) {
            System.err.println("Error clicking deliver here button: " + e.getMessage());
        }
    }
    
    public boolean isAddressSaved() {
        try {
            WebElement savedAddress = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'saved-address')] | " +
                         "//div[contains(@class,'address-card')] | " +
                         "//*[contains(text(),'Saved Address')]")));
            return savedAddress.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public void addCompleteAddress(String address, String houseNo, String landmark) {
        clickAddressInput();
        enterAddress(address);
        selectFirstAddressSuggestion();
        enterHouseDetails(houseNo, landmark);
        clickSaveButton();
    }
}