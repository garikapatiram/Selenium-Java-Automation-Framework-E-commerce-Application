package base;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import utility.ConfigReader;
import utility.ExtentManager;
import utility.ScreenshotUtils;
import utility.WebDriverFactory;

/**
 * Base class for all test classes
 */
public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    
    @BeforeSuite
    public void beforeSuite() {
        logger.info("Starting test execution");
        ConfigReader.initialize();
        extent = ExtentManager.createInstance();
        
        // Create directories if they don't exist
        new File("logs").mkdirs();
        new File("screenshots").mkdirs();
        new File("reports").mkdirs();
        new File("test-output").mkdirs();
    }
    
    @BeforeMethod
    public void setup(Method method) {
        logger.info("Setting up WebDriver for test: {}", method.getName());
        
        // Initialize test report
        test = extent.createTest(method.getName(), method.getAnnotation(Test.class).description());
        
        // Initialize WebDriver
        driver = WebDriverFactory.getDriver(ConfigReader.getProperty("browser"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.getProperty("implicit.wait"))));
        
        // Open application URL
        driver.get(ConfigReader.getProperty("base.url"));
        logger.info("Navigated to URL: {}", ConfigReader.getProperty("base.url"));
    }
    
 // ststic wait for pause the browser
 		public void staticWait(int seconds) {
 		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
 		
 		}
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        // Log test result
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test FAILED: {} - {}", result.getName(), result.getThrowable().getMessage());
            test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
            
            // Capture screenshot on failure if configured
            if (Boolean.parseBoolean(ConfigReader.getProperty("screenshot.on.failure"))) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getName());
                test.addScreenCaptureFromPath(screenshotPath);
                logger.info("Screenshot captured: {}", screenshotPath);
            }
            
            // Log stack trace for debugging
            test.log(Status.INFO, Arrays.toString(result.getThrowable().getStackTrace()));
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test PASSED: {}", result.getName());
            test.log(Status.PASS, "Test Passed");
        } else {
            logger.info("Test SKIPPED: {}", result.getName());
            test.log(Status.SKIP, "Test Skipped");
        }
        
        // Quit WebDriver
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed");
        }
    }
    
    @AfterSuite
    public void afterSuite() {
        // Flush extent reports
        if (extent != null) {
            extent.flush();
            logger.info("Extent Report generated successfully");
        }
        logger.info("Test execution completed");
    }
    
    /**
     * Gets the current test instance for logging
     * @return ExtentTest instance
     */
    public ExtentTest getTest() {
        return test;
    }
    
    /**
     * Gets the WebDriver instance
     * @return WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
}
