package test;

import base.BaseTest;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import utility.ConfigReader;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class for cart functionality
 */
public class CartTest extends BaseTest {
    private HomePage homePage;
    
    @BeforeMethod
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = loginPage.login(
                ConfigReader.getProperty("username"),
                ConfigReader.getProperty("password"));
        
        // Verify login was successful
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in before cart tests");
        staticWait(2);
    }
    
    @Test(description = "Test adding product to cart")
    public void testAddProductToCart() {
        // Get all products
        List<String> productTitles = homePage.getProductTitles();
        
        if (!productTitles.isEmpty()) {
            String productName = productTitles.get(0);
            
            // Add product to cart
            homePage.addProductToCart(productName);
            test.info("Added product to cart: " + productName);
            staticWait(3);
            // Go to cart
            CartPage cartPage = homePage.goToCart();
            
            // Verify product is in cart
            List<String> cartItems = cartPage.getCartItemNames();
         //   System.out.println(cartItems);
            boolean productInCart = cartItems.contains(productName);
            
            Assert.assertTrue(productInCart, "Product should be in cart: " + productName);
            test.info("Product verified in cart: " + productName);
        } else {
            Assert.fail("No products found to test add to cart");
        }
    }
    
    @Test(description = "Test removing product from cart")
    public void testRemoveProductFromCart() {
        // Get all products
        List<String> productTitles = homePage.getProductTitles();
        
        if (!productTitles.isEmpty()) {
            String productName = productTitles.get(0);
            
            // Add product to cart
            homePage.addProductToCart(productName);
            test.info("Added product to cart: " + productName);
            staticWait(3);
            // Go to cart
            CartPage cartPage = homePage.goToCart();
            
            // Verify product is in cart
            int initialItemCount = cartPage.getCartItemCount();
            Assert.assertTrue(initialItemCount > 0, "Cart should have items");
            
            // Remove product from cart
            cartPage.removeItem(productName);
            staticWait(3);
            test.info("Removed product from cart: " + productName);
            
            // Refresh cart page to ensure changes are reflected
       /**     driver.navigate().refresh();
        *    cartPage = new CartPage(driver);
            */
            // Verify product is removed
            int finalItemCount = cartPage.getCartItemCount();
            staticWait(1);
            
       //   Assert.assertTrue(finalItemCount<initialItemCount-1, "Cart should have one less item after remova");
            Assert.assertEquals(finalItemCount, initialItemCount-1,  "Cart should have one less item after removal");
        } else {
            Assert.fail("No products found to test remove from cart");
        }
    }
    
    @Test(description = "Test cart total calculation")
    public void testCartTotal() {
        // Get all products
        List<String> productTitles = homePage.getProductTitles();
        
        if (productTitles.size() >= 2) {
            // Add two products to cart
        	
        	homePage.addProductToCart(productTitles.get(0)).addProductToCart(productTitles.get(1));
         //   homePage.addProductToCart(productTitles.get(0));
         //   staticWait(3);
         //   homePage.addProductToCart(productTitles.get(1));
        //    staticWait(3);
            test.info("Added products to cart: " + productTitles.get(0) + ", " + productTitles.get(1));
            
            // Go to cart
            CartPage cartPage = homePage.goToCart();
         //   staticWait(3);
            
            System.out.println(cartPage.getCartItemCount());
            // Verify cart has 2 items
            Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");
            
            // Verify total amount is not empty
            String totalAmount = cartPage.getTotalAmount();
            Assert.assertFalse(totalAmount.isEmpty(), "Total amount should be displayed");
            
            test.info("Cart total verified: " + totalAmount);
        } else {
            Assert.fail("Not enough products found to test cart total");
        }
    }
}
