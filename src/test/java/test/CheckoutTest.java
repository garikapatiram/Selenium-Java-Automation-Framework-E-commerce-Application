package test;

import base.BaseTest;
import pages.*;
import utility.ConfigReader;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class for checkout functionality
 */
public class CheckoutTest extends BaseTest {
    private HomePage homePage;
    
    @BeforeMethod
    public void loginAndAddProductToCart() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = loginPage.login(
                ConfigReader.getProperty("username"),
                ConfigReader.getProperty("password"));
        
        // Verify login was successful
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in before checkout tests");
        staticWait(2);
        // Get all products
        List<String> productTitles = homePage.getProductTitles();
        
        if (!productTitles.isEmpty()) {
            // Add product to cart
            homePage.addProductToCart(productTitles.get(0));
            staticWait(2);
            test.info("Added product to cart: " + productTitles.get(0));
        } else {
            Assert.fail("No products found to add to cart");
        }
    }
    
    @Test(description = "Test complete checkout process")
    public void testCheckoutProcess() {
        // Go to cart
        CartPage cartPage = homePage.goToCart();
        
        // Verify cart has items
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should have items for checkout");
        
        // Proceed to checkout
        CheckoutPage checkoutPage = cartPage.checkout();
        
        // Fill checkout form
        checkoutPage.selectCountry("India")
                    .enterCardDetails("123", "Test User");
                  //  .enterShippingAddress("123 Test Address, Test City, 12345");
        
        // Place order
        OrderConfirmationPage confirmationPage = checkoutPage.placeOrder();
        
        // Verify order confirmation
        Assert.assertTrue(confirmationPage.isOrderConfirmed(), "Order should be confirmed");
        
        // Get and verify order ID
        String orderId = confirmationPage.getOrderId();
        Assert.assertFalse(orderId.isEmpty(), "Order ID should be displayed");
        
        test.info("Order placed successfully with ID: " + orderId);
        
        // Go to orders page
        OrdersPage ordersPage = confirmationPage.goToOrders();
        
        // Verify order is listed
        Assert.assertTrue(ordersPage.hasOrders(), "Orders page should list the placed order");
        
        test.info("Order verified in orders list");
    }
}
