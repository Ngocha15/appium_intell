package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

	private final AndroidDriver driver;
	private final WebDriverWait wait;

	public CartPage(AndroidDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	private By checkoutButtonBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.cart.summary.checkout_button\")"
		);
	}

	private By cartItemCardBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.cart.item.card.\")"
		);
	}

	private By cartItemDeleteButtonBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.cart.item.delete_button.\")"
		);
	}

	public boolean isDisplayed() {
		try {
			return wait.until(d -> d.findElement(checkoutButtonBy())).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean hasCartItems() {
		return !driver.findElements(cartItemCardBy()).isEmpty();
	}

	public void proceedToCheckout() {
		wait.until(d -> d.findElement(checkoutButtonBy())).click();
	}

	public void deleteCartItemAtIndex(int index) {
		List<WebElement> deleteButtons = wait.until(driver -> {
			List<WebElement> found = driver.findElements(cartItemDeleteButtonBy());
			return found.size() > index ? found : null;
		});

		deleteButtons.get(index).click();
	}
}
