package listeners;

import utility.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry listener for failed tests
 */
public class RetryListener implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(RetryListener.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = Integer.parseInt(
            ConfigReader.getProperty("retry.count", "2"));

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            logger.info("Retrying test {} for the {} time", result.getName(), retryCount + 1);
            retryCount++;
            return true;
        }
        return false;
    }
}
