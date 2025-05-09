package pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;

/**
 * Page object for order details page
 */
public class OrderDetailsPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(OrderDetailsPage.class);
    
    // Page elements
    @FindBy(css = ".email-title")
    private WebElement orderTitle;
    
    @FindBy(css = ".orderDetails span")
    private WebElement orderDate;
    
    @FindBy(css = ".orderStatus .title")
    private WebElement orderStatus;
    
    @FindBy(css = ".col-text")
    private WebElement shippingAddress;
    
    @FindBy(css = ".btn-primary")
    private WebElement goBackButton;
    
    /**
     * Constructor to initialize OrderDetailsPage
     * @param driver WebDriver instance
     */
    public OrderDetailsPage(WebDriver driver) {
        super(driver);
        logger.info("Order details page initialized");
    }
    
    /**
     * Gets order ID from title
     * @return Order ID
     */
    public String getOrderId() {
        String title = getText(orderTitle);
        return title.replace("Your Order Is Confirmed ", "").trim();
    }
    
    /**
     * Gets order date
     * @return Order date text
     */
    public String getOrderDate() {
        return getText(orderDate);
    }
    
    /**
     * Gets order status
     * @return Order status text
     */
    public String getOrderStatus() {
        return getText(orderStatus);
    }
    
    /**
     * Gets shipping address
     * @return Shipping address text
     */
    public String getShippingAddress() {
        return getText(shippingAddress);
    }
    
    /**
     * Goes back to orders page
     * @return OrdersPage instance
     */
    public OrdersPage goBackToOrders() {
        logger.info("Going back to orders page");
        click(goBackButton);
        return new OrdersPage(driver);
    }
}
