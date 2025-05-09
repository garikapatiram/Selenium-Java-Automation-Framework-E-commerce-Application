package utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Utility class to manage Extent Reports
 */
public class ExtentManager {
	
	    private static final Logger logger = LogManager.getLogger(ExtentManager.class);
	    
	    private ExtentManager() {
	        // Private constructor to prevent instantiation
	    }
	    
	    /**
	     * Creates and returns an ExtentReports instance
	     * @return ExtentReports instance
	     */
	    public static ExtentReports createInstance() {
	        String reportPath = ConfigReader.getProperty("report.path", 
	                "reports/ExtentReport_" + getFormattedDateTime() + ".html");
	        
	        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
	        sparkReporter.config().setDocumentTitle(ConfigReader.getProperty("report.title", "Automation Test Report"));
	        sparkReporter.config().setReportName(ConfigReader.getProperty("report.name", "Test Execution Report"));
	        sparkReporter.config().setTheme(Theme.STANDARD);
	        sparkReporter.config().setEncoding("utf-8");
	        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
	        
	        ExtentReports extent = new ExtentReports();
	        extent.attachReporter(sparkReporter);
	        
	        // Set system info
	        extent.setSystemInfo("OS", System.getProperty("os.name"));
	        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
	        extent.setSystemInfo("Browser", ConfigReader.getProperty("browser", "chrome"));
	        extent.setSystemInfo("Environment", "QA");
	        extent.setSystemInfo("Application URL", ConfigReader.getProperty("base.url"));
	        
	        logger.info("Extent Report configured at: {}", reportPath);
	        return extent;
	    }
	    
	    /**
	     * Gets current date time in formatted string
	     * @return Formatted date time string
	     */
	    private static String getFormattedDateTime() {
	        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	    }
	}


