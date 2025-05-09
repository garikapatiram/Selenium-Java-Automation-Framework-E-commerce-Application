package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

/**
 * Utility class for WebDriver waits
 */
public class WaitUtils {
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    
    private WaitUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Creates a WebDriverWait with default timeout
     * @param driver WebDriver instance
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait(WebDriver driver) {
        int timeout = Integer.parseInt(ConfigReader.getProperty("explicit.wait", "30"));
        return new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }
    
    /**
     * Creates a WebDriverWait with custom timeout
     * @param driver WebDriver instance
     * @param timeoutInSeconds Timeout in seconds
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait(WebDriver driver, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }
    
    /**
     * Creates a FluentWait with custom configuration
     * @param driver WebDriver instance
     * @param timeoutInSeconds Timeout in seconds
     * @param pollingIntervalInMillis Polling interval in milliseconds
     * @return FluentWait instance
     */
    public static FluentWait<WebDriver> getFluentWait(WebDriver driver, int timeoutInSeconds, int pollingIntervalInMillis) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalInMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }
    
    /**
     * Waits for element to be clickable
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @return Clickable WebElement
     */
    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        try {
            return getWait(driver).until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be clickable: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Waits for element to be visible
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @return Visible WebElement
     */
    public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
        try {
            return getWait(driver).until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be visible: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Waits for element to be present in DOM
     * @param driver WebDriver instance
     * @param locator By locator to find element
     * @return WebElement when present
     */
    public static WebElement waitForElementToBePresent(WebDriver driver, By locator) {
        try {
            return getWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be present: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Waits for page to load completely (document.readyState)
     * @param driver WebDriver instance
     */
    public static void waitForPageLoad(WebDriver driver) {
        try {
            ExpectedCondition<Boolean> pageLoadCondition = d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").equals("complete");
            getWait(driver).until(pageLoadCondition);
            logger.debug("Page loaded completely");
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for page to load: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Waits for Ajax calls to complete (jQuery)
     * @param driver WebDriver instance
     */
    public static void waitForAjaxToComplete(WebDriver driver) {
        try {
            Function<WebDriver, Boolean> jQueryLoaded = d -> {
                try {
                    return ((Long) ((JavascriptExecutor) d)
                            .executeScript("return jQuery.active")) == 0;
                } catch (Exception e) {
                    return true; // If jQuery is not defined, return true to exit wait
                }
            };
            getFluentWait(driver, 30, 500).until(jQueryLoaded);
            logger.debug("Ajax calls completed");
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for Ajax calls to complete: {}", e.getMessage());
            throw e;
        }
    }
}
