package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;

/**
 * Page object for login page
 */
public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    
    // Page elements
    @FindBy(id = "userEmail")
    private WebElement emailInput;
    
    @FindBy(id = "userPassword")
    private WebElement passwordInput;
    
    @FindBy(id = "login")
    private WebElement loginButton;
    
    @FindBy(css = ".forgot-password-link")
    private WebElement forgotPasswordLink;
    
    @FindBy(css = ".ng-trigger-flyInOut")
    private WebElement errorMessage;
    
    /**
     * Constructor to initialize LoginPage
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
        logger.info("Login page initialized");
    }
    
    /**
     * Login with provided credentials
     * @param email User email
     * @param password User password
     * @return HomePage instance after successful login
     */
    public HomePage login(String email, String password) {
        logger.info("Logging in with email: {}", email);
        
        sendKeys(emailInput, email);
        sendKeys(passwordInput, password);
        click(loginButton);
        
        logger.info("Login attempt completed");
        return new HomePage(driver);
    }
    
    /**
     * Attempt login with invalid credentials to test error handling
     * @param email User email
     * @param password User password
     * @return This LoginPage instance
     */
    public LoginPage loginWithInvalidCredentials(String email, String password) {
        logger.info("Attempting login with invalid credentials");
        
        sendKeys(emailInput, email);
        sendKeys(passwordInput, password);
        click(loginButton);
        
        logger.info("Invalid login attempt completed");
        return this;
    }
    
    /**
     * Click forgot password link
     * @return ForgotPasswordPage instance
     */
    public ForgotPasswordPage clickForgotPassword() {
        logger.info("Clicking forgot password link");
        click(forgotPasswordLink);
        return new ForgotPasswordPage(driver);
    }
    
    /**
     * Gets error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        try {
            return waitForVisibility(errorMessage).getText();
        } catch (Exception e) {
            logger.warn("Error message not displayed: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Checks if error message is displayed
     * @return true if error message is displayed, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
}
