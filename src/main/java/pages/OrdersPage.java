package pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page object for orders page
 */
public class OrdersPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(OrdersPage.class);
    
    // Page elements
    @FindBy(css = "tr")
    private List<WebElement> orderRows;
    
    @FindBy(css = ".btn-primary")
    private List<WebElement> viewButtons;
    
    /**
     * Constructor to initialize OrdersPage
     * @param driver WebDriver instance
     */
    public OrdersPage(WebDriver driver) {
        super(driver);
        logger.info("Orders page initialized");
    }
    
    /**
     * Gets number of orders
     * @return Number of orders
     */
    public int getOrderCount() {
        // First row is header, so subtract 1
        return orderRows.size() > 0 ? orderRows.size() - 1 : 0;
    }
    
    /**
     * Gets order IDs
     * @return List of order IDs
     */
    public List<String> getOrderIds() {
        // Skip the first row (header)
        return orderRows.stream()
                .skip(1)
                .map(row -> row.findElement(By.cssSelector("th")).getText())
                .collect(Collectors.toList());
    }
    
    /**
     * Views order details by index (0-based)
     * @param index Order index
     * @return OrderDetailsPage instance
     */
    public OrderDetailsPage viewOrderDetails(int index) {
        logger.info("Viewing order details at index: {}", index);
        
        if (index >= viewButtons.size()) {
            logger.error("Invalid order index: {}", index);
            throw new IllegalArgumentException("Invalid order index: " + index);
        }
        
        click(viewButtons.get(index));
        return new OrderDetailsPage(driver);
    }
    
    /**
     * Views order details by order ID
     * @param orderId Order ID
     * @return OrderDetailsPage instance
     */
    public OrderDetailsPage viewOrderDetails(String orderId) {
        logger.info("Viewing order details for ID: {}", orderId);
        
        boolean orderFound = false;
        List<String> orderIds = getOrderIds();
        
        for (int i = 0; i < orderIds.size(); i++) {
            if (orderIds.get(i).equals(orderId)) {
                click(viewButtons.get(i));
                orderFound = true;
                logger.info("Order found and details opened");
                break;
            }
        }
        
        if (!orderFound) {
            logger.error("Order ID not found: {}", orderId);
            throw new IllegalArgumentException("Order ID not found: " + orderId);
        }
        
        return new OrderDetailsPage(driver);
    }
    
    /**
     * Checks if any orders exist
     * @return true if orders exist, false otherwise
     */
    public boolean hasOrders() {
        return getOrderCount() > 0;
    }
}
