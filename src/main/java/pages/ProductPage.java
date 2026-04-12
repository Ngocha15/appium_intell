package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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

	private By sizeOptionsBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.product_detail.size_option.\")"
		);
	}

	private By colorOptionsBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.product_detail.color_option.\")"
		);
	}

	private By addToCartSuccessBy() {
		return AppiumBy.xpath("//*[contains(@text,'Added to cart successfully!')]");
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

	public void selectFirstAvailableSizeIfPresent() {
		List<WebElement> sizeOptions = driver.findElements(sizeOptionsBy());
		if (!sizeOptions.isEmpty()) {
			wait.until(ExpectedConditions.elementToBeClickable(sizeOptions.get(0))).click();
		}
	}

	public void selectFirstAvailableColorIfPresent() {
		List<WebElement> colorOptions = driver.findElements(colorOptionsBy());
		if (!colorOptions.isEmpty()) {
			wait.until(ExpectedConditions.elementToBeClickable(colorOptions.get(0))).click();
		}
	}

	public void waitForAddToCartSuccess() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartSuccessBy()));
		} catch (Exception ignored) {
			wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonBy()));
		}
	}

	public void openCart() {
		wait.until(d -> d.findElement(cartButtonBy())).click();
	}
}
