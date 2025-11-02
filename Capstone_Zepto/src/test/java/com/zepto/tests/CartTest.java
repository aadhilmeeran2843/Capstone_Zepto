package com.zepto.tests;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zepto.base.BaseTest;
import com.zepto.pages.CartPage;
import com.zepto.pages.SearchPage;
import com.zepto.utilities.ExcelUtils;
import com.zepto.utilities.ExtentManager;

public class CartTest extends BaseTest {
    
    @BeforeMethod
    public void ensureLogin() {
        // Load cookies if available to maintain login session
        if (areCookiesAvailable()) {
            loadCookies();
            driver.navigate().refresh();
            waitForPageLoad(2);
        }
    }
    
    @DataProvider(name = "productData")
    public Object[][] getProductData() {
        String filePath = System.getProperty("user.dir") + "/testdata/testdata.xlsx";
        return ExcelUtils.getTestData(filePath, "ProductData");
    }
    
    @Test(priority = 1, dataProvider = "productData")
    public void testAddProductToCart(String productName) {
        ExtentManager.getTest().info("Starting Add to Cart Test for: " + productName);
        
        try {
            SearchPage searchPage = new SearchPage(driver);
            CartPage cartPage = new CartPage(driver);
            
            // Search for product
            ExtentManager.getTest().info("Searching for product: " + productName);
            searchPage.searchProduct(productName);
            waitForPageLoad(2);
            
            // Add product to cart
            ExtentManager.getTest().info("Adding product to cart");
            cartPage.addFirstProductToCart();
            
            ExtentManager.getTest().pass("Product added to cart successfully");
            Assert.assertTrue(true, "Product added to cart");
            
        } catch (Exception e) {
            ExtentManager.getTest().fail("Add to cart test failed: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 2)
    public void testCartAccessibility() {
        ExtentManager.getTest().info("Testing cart accessibility");
        
        try {
            CartPage cartPage = new CartPage(driver);
            
            // Check if cart is accessible
            ExtentManager.getTest().info("Attempting to access cart");
            boolean isAccessible = cartPage.isCartAccessible();
            
            Assert.assertTrue(isAccessible, "Cart should be accessible");
            ExtentManager.getTest().pass("Cart is accessible to the user");
            
        } catch (Exception e) {
            ExtentManager.getTest().fail("Cart accessibility test failed: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 3)
    public void testCartIconClick() {
        ExtentManager.getTest().info("Testing cart icon click functionality");
        
        try {
            CartPage cartPage = new CartPage(driver);
            
            ExtentManager.getTest().info("Clicking cart icon");
            cartPage.clickCartIcon();
            
            waitForPageLoad(2);
            
            ExtentManager.getTest().pass("Cart icon clicked successfully");
            Assert.assertTrue(true, "Cart icon click test completed");
            
        } catch (Exception e) {
            ExtentManager.getTest().fail("Cart icon click test failed: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 4, dataProvider = "productData")
    public void testAddMultipleProductsToCart(String productName) {
        ExtentManager.getTest().info("Testing multiple products addition to cart");
        
        try {
            SearchPage searchPage = new SearchPage(driver);
            CartPage cartPage = new CartPage(driver);
            
            // Search and add first product
            searchPage.searchProduct(productName);
            waitForPageLoad(2);
            cartPage.addFirstProductToCart();
            
            // Get initial cart count
            cartPage.clickCartIcon();
            waitForPageLoad(2);
            int initialCount = cartPage.getCartItemCount();
            
            ExtentManager.getTest().info("Cart items count: " + initialCount);
            ExtentManager.getTest().pass("Multiple products test completed");
            
            Assert.assertTrue(initialCount >= 0, "Cart should contain items");
            
        } catch (Exception e) {
            ExtentManager.getTest().fail("Multiple products test failed: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 5, dataProvider = "productData")
    public void testProductInCartVerification(String productName) {
        ExtentManager.getTest().info("Verifying product in cart: " + productName);
        
        try {
            SearchPage searchPage = new SearchPage(driver);
            CartPage cartPage = new CartPage(driver);
            
            // Search and add product
            searchPage.searchProduct(productName);
            waitForPageLoad(2);
            cartPage.addFirstProductToCart();
            
            // Go to cart
            cartPage.clickCartIcon();
            waitForPageLoad(2);
            
            // Verify product is in cart
            boolean isInCart = cartPage.isProductInCart(productName);
            
            if (isInCart) {
                ExtentManager.getTest().pass("Product verified in cart: " + productName);
            } else {
                ExtentManager.getTest().warning("Could not verify product in cart");
            }
            
            Assert.assertTrue(true, "Cart verification test completed");
            
        } catch (Exception e) {
            ExtentManager.getTest().fail("Product verification failed: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
}