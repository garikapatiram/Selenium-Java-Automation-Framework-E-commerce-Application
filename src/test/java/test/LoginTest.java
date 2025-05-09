package test;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import utility.ConfigReader;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for login functionality
 */
public class LoginTest extends BaseTest {

	@Test(description = "Test valid login with correct credentials", priority = 1)
	public void testValidLogin() {
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.login(ConfigReader.getProperty("username"), ConfigReader.getProperty("password"));

		// Verify login was successful
		Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in");
		test.info("Login successful");
	}

	@Test(priority = 2, description = "Test login with invalid credentials")
	public void testInvalidLogin() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage = loginPage.loginWithInvalidCredentials(ConfigReader.getProperty("ivalidusername"),
				ConfigReader.getProperty("invalidpassword"));
		
		staticWait(1);
		// Verify error message is displayed
		Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");

		test.info("Error message displayed as expected");
	}

	// @Test(description = "Test forgot password functionality")
	public void testForgotPassword() {
		LoginPage loginPage = new LoginPage(driver);
		var forgotPasswordPage = loginPage.clickForgotPassword();

		// Submit email for password reset
		forgotPasswordPage = forgotPasswordPage.submitEmail(ConfigReader.getProperty("username"));

		// Verify message is displayed
		String message = forgotPasswordPage.getMessage();
		Assert.assertFalse(message.isEmpty(), "Password reset message should be displayed");
		test.info("Forgot password function works as expected");
	}
}
