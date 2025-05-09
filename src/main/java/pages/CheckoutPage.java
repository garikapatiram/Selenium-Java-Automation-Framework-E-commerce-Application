package pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import base.BasePage;

/**
 * Page object for checkout page
 */
public class CheckoutPage extends BasePage {
	private static final Logger logger = LogManager.getLogger(CheckoutPage.class);

	// Page elements
	@FindBy(css = "input[placeholder='Select Country']")
	private WebElement countryInput;

	@FindBy(css = " span[class ='ng-star-inserted']")
	private List<WebElement> selectCountrys;

	@FindBy(xpath = "(//input[@type='text'])[2]")
	private WebElement cvvInput;

	@FindBy(xpath = "(//input[@type='text'])[3]")
	private WebElement nameOnCardInput;

	@FindBy(css = ".action__submit")
	private WebElement placeOrderButton;

	@FindBy(css = ".user__address")
	private WebElement addressTextarea;

	/**
	 * Constructor to initialize CheckoutPage
	 * 
	 * @param driver WebDriver instance
	 */
	public CheckoutPage(WebDriver driver) {
		super(driver);
		logger.info("Checkout page initialized");
	}

	/**
	 * Selects country from dropdown
	 * 
	 * @param country Country name
	 * @return This CheckoutPage instance
	 */
	public CheckoutPage selectCountry(String country) {
		logger.info("Selecting country: {}", country);
		
		sendKeys(countryInput, country);
		// Let the dropdown suggestions appear
		/**
		 * try { Thread.sleep(1000); } catch (InterruptedException e) {
		 * Thread.currentThread().interrupt(); }
		 * 
		 * // Press Enter to select the first suggestion
		countryInput.sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
		 */
		
		// Autosuggestive DropDown Selection
		for (WebElement selectCountry : selectCountrys) {

			if (selectCountry.getText().equalsIgnoreCase(country)) {
				selectCountry.click();
				break;
			}
		}
		

		return this;
	}

	/**
	 * Enters card details
	 * 
	 * @param cvv        CVV number
	 * @param nameOnCard Name on card
	 * @return This CheckoutPage instance
	 */
	public CheckoutPage enterCardDetails(String cvv, String nameOnCard) {
		logger.info("Entering card details");
		sendKeys(cvvInput, cvv);
		sendKeys(nameOnCardInput, nameOnCard);
		return this;
	}

	/**
	 * Enters shipping address
	 * 
	 * @param address Shipping address
	 * @return This CheckoutPage instance
	 */
	public CheckoutPage enterShippingAddress(String address) {
		logger.info("Entering shipping address");
		sendKeys(addressTextarea, address);
		return this;
	}

	/**
	 * Places order
	 * 
	 * @return OrderConfirmationPage instance
	 */
	public OrderConfirmationPage placeOrder() {
		logger.info("Placing order");
		click(placeOrderButton);
		return new OrderConfirmationPage(driver);
	}
}
