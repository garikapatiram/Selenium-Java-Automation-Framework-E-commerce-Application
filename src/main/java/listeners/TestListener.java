package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

import base.BaseTest;
import utility.ScreenshotUtils;

/**
 * Custom TestNG listener for test execution tracking and reporting
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finished test suite: {}", context.getName());
        logger.info("Passed tests: {}", context.getPassedTests().size());
        logger.info("Failed tests: {}", context.getFailedTests().size());
        logger.info("Skipped tests: {}", context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {}", result.getName());
        
        // Get the test instance and update extent test
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            BaseTest test = (BaseTest) testInstance;
            test.getTest().info("Test Started");
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            BaseTest test = (BaseTest) testInstance;
            test.getTest().log(Status.PASS, "Test Passed");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        logger.error("Failure reason: {}", result.getThrowable().getMessage());
        
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            BaseTest test = (BaseTest) testInstance;
            WebDriver driver = test.getDriver();
            
            // Capture screenshot
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getName());
            if (screenshotPath != null) {
                test.getTest().addScreenCaptureFromPath(screenshotPath);
            }
            
            // Log failure details
            test.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
            test.getTest().log(Status.INFO, result.getThrowable().toString());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
        
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            BaseTest test = (BaseTest) testInstance;
            test.getTest().log(Status.SKIP, "Test Skipped");
            if (result.getThrowable() != null) {
                test.getTest().log(Status.INFO, result.getThrowable().toString());
            }
        }
    }
}
