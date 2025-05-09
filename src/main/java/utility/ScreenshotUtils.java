package utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Utility class to capture screenshots
 */
public class ScreenshotUtils {
	
	    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
	    private static final String SCREENSHOT_DIRECTORY = "screenshots/";
	    
	    private ScreenshotUtils() {
	        // Private constructor to prevent instantiation
	    }
	    
	    /**
	     * Captures screenshot and saves it to disk
	     * @param driver WebDriver instance
	     * @param testName Name of the test
	     * @return Path to the saved screenshot
	     */
	    public static String captureScreenshot(WebDriver driver, String testName) {
	        if (driver == null) {
	            logger.error("WebDriver is null, cannot capture screenshot");
	            return null;
	        }
	        
	        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	        String screenshotName = testName + "_" + timestamp + ".png";
	        String destination = SCREENSHOT_DIRECTORY + screenshotName;
	        
	        try {
	            TakesScreenshot ts = (TakesScreenshot) driver;
	            File source = ts.getScreenshotAs(OutputType.FILE);
	            File destFile = new File(destination);
	            FileUtils.copyFile(source, destFile);
	            logger.info("Screenshot saved: {}", destination);
	            return destination;
	        } catch (IOException e) {
	            logger.error("Failed to capture screenshot: {}", e.getMessage());
	            return null;
	        }
	    }
	

}
