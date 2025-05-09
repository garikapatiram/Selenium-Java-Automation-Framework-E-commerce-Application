package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Factory class to create WebDriver instances
 */

public class WebDriverFactory {
	
	    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);
	    
	    private WebDriverFactory() {
	        // Private constructor to prevent instantiation
	    }
	    
	    /**
	     * Creates and returns a WebDriver instance based on browser name
	     * @param browserName Name of the browser (chrome, firefox, edge, safari)
	     * @return WebDriver instance
	     */
	    public static WebDriver getDriver(String browserName) {
	        WebDriver driver;
	        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless", "false"));
	        
	        switch (browserName.toLowerCase()) {
	            case "chrome" -> {
	                logger.info("Initializing Chrome driver");
	                WebDriverManager.chromedriver().setup();
	                ChromeOptions options = new ChromeOptions();
	                if (isHeadless) {
	                    options.addArguments("--headless=new");
	                    logger.info("Running Chrome in headless mode");
	                }
	                options.addArguments("--disable-gpu");
	                options.addArguments("--no-sandbox");
	                options.addArguments("--disable-dev-shm-usage");
	                driver = new ChromeDriver(options);
	            }
	            case "firefox" -> {
	                logger.info("Initializing Firefox driver");
	                WebDriverManager.firefoxdriver().setup();
	                FirefoxOptions options = new FirefoxOptions();
	                if (isHeadless) {
	                    options.addArguments("--headless");
	                    logger.info("Running Firefox in headless mode");
	                }
	                driver = new FirefoxDriver(options);
	            }
	            case "edge" -> {
	                logger.info("Initializing Edge driver");
	                WebDriverManager.edgedriver().setup();
	                EdgeOptions options = new EdgeOptions();
	                if (isHeadless) {
	                    options.addArguments("--headless");
	                    logger.info("Running Edge in headless mode");
	                }
	                driver = new EdgeDriver(options);
	            }
	            case "safari" -> {
	                logger.info("Initializing Safari driver");
	                driver = new SafariDriver();
	                logger.info("Safari does not support headless mode");
	            }
	            default -> {
	                logger.warn("Unknown browser: {}. Defaulting to Chrome.", browserName);
	                WebDriverManager.chromedriver().setup();
	                driver = new ChromeDriver();
	            }
	        }
	        
	        logger.info("{} driver initialized successfully", browserName);
	        return driver;
	    }
	}


