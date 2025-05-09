package pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;

/**
 * Page object for forgot password page
 */
public class ForgotPasswordPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(ForgotPasswordPage.class);
    
    // Page elements
    @FindBy(xpath = "//input[@formcontrolname ='userEmail']")
    private WebElement emailInput;
    
    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;
    
    @FindBy(css = ".login-wrapper a")
    private WebElement loginLink;
    
    @FindBy(css = ".ng-trigger-flyInOut")
    private WebElement message;
    
    /**
     * Constructor to initialize ForgotPasswordPage
     * @param driver WebDriver instance
     */
    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
        logger.info("Forgot password page initialized");
    }
    
    /**
     * Submit email for password reset
     * @param email Email address
     * @return This ForgotPasswordPage instance
     */
    public ForgotPasswordPage submitEmail(String email) {
        logger.info("Submitting email for password reset: {}", email);
        sendKeys(emailInput, email);
        click(submitButton);
        return this;
    }
    
    /**
     * Gets message text
     * @return Message text
     */
    public String getMessage() {
        try {
            return waitForVisibility(message).getText();
        } catch (Exception e) {
            logger.warn("Message not displayed: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Clicks on login link
     * @return LoginPage instance
     */
    public LoginPage goToLogin() {
        logger.info("Navigating to login page");
        click(loginLink);
        return new LoginPage(driver);
    }
}
