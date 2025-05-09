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
 * Page object for home page
 */
public class HomePage extends BasePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    
    // Page elements
    @FindBy(css = ".card")
    private List<WebElement> productCards;
    
    @FindBy (xpath = "(//button[@class='btn w-40 rounded'][normalize-space()='View'])[1]")
    private WebElement ViewButton;
    
    @FindBy(css = "[routerlink*='cart']")
    private WebElement cartButton;
    
    @FindBy(css = "[routerlink*='myorders']")
    private WebElement ordersButton;
    
    @FindBy(css = ".fa-sign-out")
    private WebElement signOutButton;
    
    @FindBy(css = "app-dashboard h5")
    private WebElement welcomeMessage;
    
    @FindBy(xpath = "(//input[@placeholder='search'])[2]")
    private WebElement searchInput;
    
    /**
     * Constructor to initialize HomePage
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        super(driver);
        logger.info("Home page initialized");
    }
    
    /**
     * Gets all product titles from home page
     * @return List of product titles
     */
    public List<String> getProductTitles() {
        return productCards.stream()
                .map(card -> card.findElement(By.cssSelector("h5")).getText())
                .collect(Collectors.toList());
    }
    
    /**
     * Adds product to cart by product name
     * @param productName Name of the product to add
     * @return This HomePage instance
     */
    public HomePage addProductToCart(String productName) {
        logger.info("Adding product to cart: {}", productName);
        
        boolean productFound = false;
        for (WebElement card : productCards) {
            String title = card.findElement(By.cssSelector("h5")).getText();
            if (title.equals(productName)) {
                WebElement addToCartButton = card.findElement(By.cssSelector(".btn.w-10"));
                click(addToCartButton);
                productFound = true;
                logger.info("Product added to cart: {}", productName);
                break;
            }
        }
        
        if (!productFound) {
            logger.warn("Product not found: {}", productName);
        }
        
        return this;
    }
    
    /**
     * Opens product details page by product name
     * @param productName Name of the product
     * @return ProductDetailsPage instance
     */
    public ProductDetailsPage openProductDetails(String productName) {
        logger.info("Opening product details: {}", productName);
        
        for (WebElement card : productCards) {
            String title = card.findElement(By.cssSelector("h5")).getText();
            if (title.equals(productName)) {
                click(ViewButton);
                logger.info("Product details opened: {}", productName);
                
                return new ProductDetailsPage(driver);
            }
        }
        
        logger.warn("Product not found: {}", productName);
        throw new RuntimeException("Product not found: " + productName);
    }
    
    /**
     * Clicks on cart button
     * @return CartPage instance
     */
    public CartPage goToCart() {
        logger.info("Navigating to cart");
        click(cartButton);
        return new CartPage(driver);
    }
    
    /**
     * Clicks on orders button
     * @return OrdersPage instance
     */
    public OrdersPage goToOrders() {
        logger.info("Navigating to orders");
        click(ordersButton);
        return new OrdersPage(driver);
    }
    
    /**
     * Clicks on sign out button
     * @return LoginPage instance
     */
    public LoginPage signOut() {
        logger.info("Signing out");
        click(signOutButton);
        return new LoginPage(driver);
    }
    
    /**
     * Gets welcome message text
     * @return Welcome message text
     */
    public String getWelcomeMessage() {
        return getText(welcomeMessage);
    }
    
    /**
     * Searches for products
     * @param searchTerm Term to search for
     * @return This HomePage instance with filtered results
     */
    public HomePage searchProduct(String searchTerm) {
        logger.info("Searching for: {}", searchTerm);
        sendKeys(searchInput, searchTerm);
        return this;
    }
    
    /**
     * Checks if user is logged in
     * @return true if welcome message is displayed, false otherwise
     */
    public boolean isUserLoggedIn() {
        return isDisplayed(welcomeMessage);
    }
}
