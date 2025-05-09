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
 * Page object for cart page
 */
public class CartPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(CartPage.class);
    
    // Page elements
    @FindBy(xpath = "//div[@class='cart']//ul")
    private List<WebElement> cartItems;
    
    @FindBy(css = ".btn.btn-danger")
    private List<WebElement> removeButtons;
    
    @FindBy(css = "div[class='wrap cf'] li:nth-child(3) button[type='button']")
    private WebElement checkoutButton;
    
    @FindBy(css = "div[class='wrap cf'] li:nth-child(2) span:nth-child(2)")
    private WebElement totalAmount;
    
    /**
     * Constructor to initialize CartPage
     * @param driver WebDriver instance
     */
    public CartPage(WebDriver driver) {
        super(driver);
        logger.info("Cart page initialized");
    }
    
    /**
     * Gets number of items in cart
     * @return Number of items
     */
    public int getCartItemCount() {
        return cartItems.size();
    }
    
    /**
     * Gets cart item names
     * @return List of cart item names
     */
    public List<String> getCartItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(By.cssSelector("h3")).getText())
                .collect(Collectors.toList());
    }
    
    /**
     * Removes item from cart by item name
     * @param itemName Name of the item to remove
     * @return This CartPage instance
     */
    public CartPage removeItem(String itemName) {
        logger.info("Removing item from cart: {}", itemName);
        
        boolean itemFound = false;
        for (int i = 0; i < cartItems.size(); i++) {
            String name = cartItems.get(i).findElement(By.cssSelector("h3")).getText();
            if (name.equals(itemName)) {
                click(removeButtons.get(i));
                itemFound = true;
                logger.info("Item removed: {}", itemName);
                break;
            }
        }
        
        if (!itemFound) {
            logger.warn("Item not found in cart: {}", itemName);
        }
        
        return this;
    }
    
    /**
     * Gets total amount from cart
     * @return Total amount text
     */
    public String getTotalAmount() {
        return getText(totalAmount);
    }
    
    /**
     * Proceeds to checkout
     * @return CheckoutPage instance
     */
    public CheckoutPage checkout() {
        logger.info("Proceeding to checkout");
        click(checkoutButton);
        return new CheckoutPage(driver);
    }
    
    /**
     * Checks if cart is empty
     * @return true if cart is empty, false otherwise
     */
    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
}
