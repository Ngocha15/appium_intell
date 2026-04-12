package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

	private final AndroidDriver driver;
	private final WebDriverWait wait;

	public CheckoutPage(AndroidDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	private By continueToPaymentButtonBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.checkout.payment.continue_button\")"
		);
	}

	private By placeOrderButtonBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.checkout.payment.place_order_button\")"
		);
	}

	public boolean isDisplayed() {
		try {
			return wait.until(d -> d.findElement(continueToPaymentButtonBy()))
					.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void continueToPayment() {
		wait.until(d -> d.findElement(continueToPaymentButtonBy())).click();
	}

	public void placeOrder() {
		wait.until(d -> d.findElement(placeOrderButtonBy())).click();
	}

	public void placePickupOrder() {
		continueToPayment();
		placeOrder();
	}
}
