package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage {

	private final AndroidDriver driver;
	private final WebDriverWait wait;

	public ProductPage(AndroidDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	private By addToCartButtonBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.product_detail.add_to_cart_button\")"
		);
	}

	private By cartButtonBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.product_detail.cart_button\")"
		);
	}

	public boolean isDisplayed() {
		return !driver.findElements(addToCartButtonBy()).isEmpty();
	}

	public void clickAddToCart() {
		wait.until(d -> d.findElement(addToCartButtonBy())).click();
	}

	public void openCart() {
		wait.until(d -> d.findElement(cartButtonBy())).click();
	}
}
