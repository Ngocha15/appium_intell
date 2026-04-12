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

	private By addToCartErrorBy() {
		return AppiumBy.xpath(
				"//*[contains(@text,'Please select a size') or contains(@text,'Invalid product ID') or contains(@text,'Product not found')]"
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
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < 20000) {
			if (!driver.findElements(addToCartSuccessBy()).isEmpty()) {
				return;
			}

			if (!driver.findElements(addToCartErrorBy()).isEmpty()) {
				String errorText = driver.findElements(addToCartErrorBy()).get(0).getText();
				throw new IllegalStateException("Add to cart thất bại: " + errorText);
			}

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IllegalStateException("Bị gián đoạn khi chờ add to cart", e);
			}
		}

		throw new IllegalStateException("Hết thời gian chờ thông báo add to cart thành công");
	}

	public void openCart() {
		wait.until(d -> d.findElement(cartButtonBy())).click();
	}
}
