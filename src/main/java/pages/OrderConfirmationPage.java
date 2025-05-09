package pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;

/**
 * Page object for order confirmation page
 */
public class OrderConfirmationPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(OrderConfirmationPage.class);
    
    // Page elements
    @FindBy(css = ".hero-primary")
    private WebElement confirmationMessage;
    
    @FindBy(css = "label[class='ng-star-inserted']")
    private WebElement orderId;
    
    @FindBy(css = "[routerlink='/dashboard/myorders']")
    private WebElement goToOrdersButton;
    
    /**
     * Constructor to initialize OrderConfirmationPage
     * @param driver WebDriver instance
     */
    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
        logger.info("Order confirmation page initialized");
    }
    
    /**
     * Gets confirmation message
     * @return Confirmation message text
     */
    public String getConfirmationMessage() {
        return getText(confirmationMessage);
    }
    
    /**
     * Gets order ID
     * @return Order ID text
     */
    public String getOrderId() {
        return getText(orderId);
    }
    
    /**
     * Navigates to orders page
     * @return OrdersPage instance
     */
    public OrdersPage goToOrders() {
        logger.info("Navigating to orders page");
        click(goToOrdersButton);
        return new OrdersPage(driver);
    }
    
    /**
     * Checks if order is confirmed successfully
     * @return true if confirmation message is displayed, false otherwise
     */
    public boolean isOrderConfirmed() {
        return isDisplayed(confirmationMessage) && 
               getText(confirmationMessage).contains("THANKYOU FOR THE ORDER");
    }
}
