package pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;

/**
 * Page object for product details page
 */
public class ProductDetailsPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(ProductDetailsPage.class);
    
    // Page elements
    @FindBy(css =  "div.col-lg-6.rtl-text div h2")
    private WebElement productTitle;
    
    @FindBy(css = "div[class='col-lg-6 rtl-text'] div h3")
    private WebElement productPrice;
    
    @FindBy(css = ".fa-shopping-cart")
    private WebElement addToCartButton;
    
    @FindBy(css = ".btn-primary:nth-child(2)")
    private WebElement buyNowButton;
    
    @FindBy(css = ".product-description")
    private WebElement productDescription;
    
    /**
     * Constructor to initialize ProductDetailsPage
     * @param driver WebDriver instance
     */
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
        logger.info("Product details page initialized");
    }
    
    /**
     * Gets product title
     * @return Product title text
     */
    public String getProductTitle() {
        return getText(productTitle);
    }
    
    /**
     * Gets product price
     * @return Product price text
     */
    public String getProductPrice() {
        return getText(productPrice);
    }
    
    /**
     * Gets product description
     * @return Product description text
     */
    public String getProductDescription() {
        return getText(productDescription);
    }
    
    /**
     * Adds product to cart
     * @return This ProductDetailsPage instance
     */
    public ProductDetailsPage addToCart() {
        logger.info("Adding product to cart");
        click(addToCartButton);
        return this;
    }
    
    /**
     * Clicks buy now button
     * @return CartPage instance
     */
    public CartPage buyNow() {
        logger.info("Clicking Buy Now button");
        click(buyNowButton);
        return new CartPage(driver);
    }
    
    /**
     * Navigates back to home page
     * @return HomePage instance
     */
    public HomePage goBackToHome() {
        logger.info("Navigating back to home page");
        driver.navigate().back();
        return new HomePage(driver);
    }
}
