package com.zepto.base;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    
    public WebDriver driver;
    protected String baseURL = "https://www.zeptonow.com/";
    private static final String COOKIE_FILE = "cookies.json";
    
    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        // Initialize WebDriver based on browser parameter
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--start-maximized");
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        
        // Navigate to base URL with retry mechanism
        try {
            driver.get(baseURL);
            System.out.println("Successfully loaded: " + baseURL);
        } catch (Exception e) {
            System.err.println("Error loading page, retrying...");
            driver.navigate().refresh();
            waitForPageLoad(5);
        }
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    // Cookie Management Methods
    public void saveCookies() {
        try {
            Set<Cookie> cookies = driver.manage().getCookies();
            
            // Convert cookies to a simple format
            StringBuilder cookieData = new StringBuilder();
            for (Cookie cookie : cookies) {
                cookieData.append(cookie.getName()).append("=")
                          .append(cookie.getValue()).append(";")
                          .append(cookie.getDomain()).append(";")
                          .append(cookie.getPath()).append(";")
                          .append(cookie.getExpiry() != null ? cookie.getExpiry().getTime() : "null").append(";")
                          .append(cookie.isSecure()).append("\n");
            }
            
            File cookieFile = new File(COOKIE_FILE);
            java.nio.file.Files.write(cookieFile.toPath(), cookieData.toString().getBytes());
            System.out.println("✓ Cookies saved successfully!");
        } catch (Exception e) {
            System.err.println("Error saving cookies: " + e.getMessage());
        }
    }
    
    public void loadCookies() {
        try {
            File cookieFile = new File(COOKIE_FILE);
            if (cookieFile.exists()) {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(cookieFile.toPath());
                
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split(";");
                    if (parts.length >= 5) {
                        String[] nameValue = parts[0].split("=", 2);
                        if (nameValue.length == 2) {
                            try {
                                Cookie.Builder builder = new Cookie.Builder(nameValue[0], nameValue[1])
                                    .domain(parts[1])
                                    .path(parts[2])
                                    .isSecure(Boolean.parseBoolean(parts[4]));
                                
                                if (!parts[3].equals("null")) {
                                    builder.expiresOn(new java.util.Date(Long.parseLong(parts[3])));
                                }
                                
                                driver.manage().addCookie(builder.build());
                            } catch (Exception e) {
                                System.err.println("Could not add cookie: " + nameValue[0]);
                            }
                        }
                    }
                }
                driver.navigate().refresh();
                System.out.println("✓ Cookies loaded successfully!");
            } else {
                System.out.println("No saved cookies found.");
            }
        } catch (Exception e) {
            System.err.println("Error loading cookies: " + e.getMessage());
        }
    }
    
    public boolean areCookiesAvailable() {
        File cookieFile = new File(COOKIE_FILE);
        return cookieFile.exists() && cookieFile.length() > 0;
    }
    
    public void waitForPageLoad(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}