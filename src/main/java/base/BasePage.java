package base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utility.ConfigReader;

public class BasePage {
	
	/**
	 * Base class for all page objects
	 */
	   protected WebDriver driver;
	    protected WebDriverWait wait;
	    protected static final Logger logger = LogManager.getLogger(BasePage.class);
	    protected JavascriptExecutor js;
	    
	    /**
	     * Constructor to initialize page elements
	     * @param driver WebDriver instance
	     */
	    public BasePage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(ConfigReader.getProperty("explicit.wait"))));
	        this.js = (JavascriptExecutor) driver;
	        PageFactory.initElements(driver, this);
	        logger.debug("Initialized page: {}", this.getClass().getSimpleName());
	    }
	    
	    /**
	     * Waits for element to be clickable and clicks on it
	     * @param element WebElement to click
	     */
	    protected void click(WebElement element) {
	        try {
	            wait.until(ExpectedConditions.elementToBeClickable(element));
	            element.click();
	            logger.debug("Clicked on element: {}", element);
	        } catch (Exception e) {
	            logger.error("Failed to click element: {}", e.getMessage());
	            jsClick(element);
	        }
	    }
	    
	    /**
	     * JavaScript click for elements that are difficult to click
	     * @param element WebElement to click
	     */
	    protected void jsClick(WebElement element) {
	        try {
	            js.executeScript("arguments[0].click();", element);
	            logger.debug("JS clicked on element: {}", element);
	        } catch (Exception e) {
	            logger.error("Failed to JS click element: {}", e.getMessage());
	            throw e;
	        }
	    }
	    
	    /**
	     * Sends text to an input field after clearing it
	     * @param element WebElement to type into
	     * @param text Text to type
	     */
	    protected void sendKeys(WebElement element, String text) {
	        try {
	            wait.until(ExpectedConditions.visibilityOf(element));
	            element.clear();
	            element.sendKeys(text);
	            logger.debug("Typed '{}' into element: {}", text, element);
	        } catch (Exception e) {
	            logger.error("Failed to type into element: {}", e.getMessage());
	            throw e;
	        }
	    }
	    
	    /**
	     * Waits for element to be visible
	     * @param element WebElement to wait for
	     * @return The visible WebElement
	     */
	    protected WebElement waitForVisibility(WebElement element) {
	        return wait.until(ExpectedConditions.visibilityOf(element));
	    }
	    
	    /**
	     * Checks if element is displayed
	     * @param element WebElement to check
	     * @return true if element is displayed, false otherwise
	     */
	    protected boolean isDisplayed(WebElement element) {
	        try {
	            return element.isDisplayed();
	        } catch (NoSuchElementException | StaleElementReferenceException e) {
	            logger.debug("Element is not displayed: {}", element);
	            return false;
	        }
	    }
	    
	    /**
	     * Gets text from element
	     * @param element WebElement to get text from
	     * @return Text content of the element
	     */
	    protected String getText(WebElement element) {
	        waitForVisibility(element);
	        return element.getText();
	    }
	    
	    /**
	     * Scrolls element into view
	     * @param element WebElement to scroll to
	     */
	    protected void scrollIntoView(WebElement element) {
	        js.executeScript("arguments[0].scrollIntoView(true);", element);
	        logger.debug("Scrolled element into view: {}", element);
	    }
	}


