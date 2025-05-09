package test;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductDetailsPage;
import utility.ConfigReader;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class for product functionality
 */
public class ProductTest extends BaseTest {
    private HomePage homePage;
    
    @BeforeMethod
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = loginPage.login(
                ConfigReader.getProperty("username"),
                ConfigReader.getProperty("password"));
        
        // Verify login was successful
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in before product tests");
        staticWait(4);
    }
    
    @Test(description = "Test product listing on home page")
    public void testProductListing() {
    	
        List<String> productTitles = homePage.getProductTitles();
        
        // Verify products are displayed
        Assert.assertFalse(productTitles.isEmpty(), "Product list should not be empty");
        test.info("Found " + productTitles.size() + " products on home page");
        
        // Log product names
        for (String title : productTitles) {
        //	System.out.println(title);
          test.info("Product: " + title);
        }
    }
    
    @Test(description = "Test product search functionality")
    public void testProductSearch() {
        // Get all products first
        List<String> allProducts = homePage.getProductTitles();
        
        // Get the first product name and use part of it for search
        if (!allProducts.isEmpty()) {
            String firstProduct = allProducts.get(0);
            String searchTerm = firstProduct.substring(0, Math.min(firstProduct.length(), 4));
          //  System.out.println(searchTerm);
            
            // Search for the product
            homePage.searchProduct(searchTerm);
            
            // Get filtered products
            List<String> filteredProducts = homePage.getProductTitles();
            
            // Verify search results
            Assert.assertFalse(filteredProducts.isEmpty(), "Filtered product list should not be empty");
            test.info("Search returned " + filteredProducts.size() + " results for term: " + searchTerm);
        } else {
            Assert.fail("No products found to test search functionality");
        }
    }
    
    @Test(description = "Test product details page")
    public void testProductDetails() {
        // Get all products
    	
        List<String> productTitles = homePage.getProductTitles();
        
        if (!productTitles.isEmpty()) {
            String productName = productTitles.get(0);
            
            // Open product details
            ProductDetailsPage productDetailsPage = homePage.openProductDetails(productName);
            
            // Verify product title on details page
            String detailsTitle = productDetailsPage.getProductTitle();
            Assert.assertEquals(detailsTitle, productName, "Product title on details page should match");
            
            // Verify price is displayed
            String price = productDetailsPage.getProductPrice();
            Assert.assertFalse(price.isEmpty(), "Product price should be displayed");
            
            // Verify description is displayed
      //      String description = productDetailsPage.getProductDescription();
     //       Assert.assertFalse(description.isEmpty(), "Product description should be displayed");
            
            test.info("Product details page validated for: " + productName);
        } else {
            Assert.fail("No products found to test details page");
        }
    }
}
