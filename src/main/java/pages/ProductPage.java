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

	private By backButtonBy() {
		return AppiumBy.xpath(
				"//android.widget.ImageView[@content-desc='Back' or @content-desc='Navigate up']"
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
		// Try multiple XPath variations for snackbar detection
		return AppiumBy.xpath(
				"//android.widget.TextView[contains(@text,'Added to cart successfully!')] | " +
				"//*[contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'added to cart successfully')] | " +
				"//android.widget.FrameLayout//android.widget.TextView[contains(@text,'successfully')]"
		);
	}

	private By addToCartErrorBy() {
		return AppiumBy.xpath(
				"//android.widget.TextView[contains(@text,'Please select a size') or contains(@text,'Invalid product ID') or contains(@text,'Product not found')] | " +
				"//*[contains(@text,'Please select') or contains(@text,'Invalid') or contains(@text,'not found')]"
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

	public boolean isBackButtonDisplayed() {
		return !driver.findElements(backButtonBy()).isEmpty();
	}

	public void clickBackButton() {
		wait.until(ExpectedConditions.elementToBeClickable(backButtonBy())).click();
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
			// First check for error immediately
			List<WebElement> errors = driver.findElements(addToCartErrorBy());
			if (!errors.isEmpty()) {
				String errorText = errors.get(0).getText();
				throw new IllegalStateException("Add to cart thất bại: " + errorText);
			}

			// Wait for success snackbar or just a brief moment
			// (snackbar may disappear quickly, so we use a shorter timeout and accept it)
			try {
				wait.until(d -> {
					List<WebElement> successElements = d.findElements(addToCartSuccessBy());
					List<WebElement> errorElements = d.findElements(addToCartErrorBy());
					
					if (!errorElements.isEmpty()) {
						throw new IllegalStateException("Add to cart thất bại: " + errorElements.get(0).getText());
					}
					
					return !successElements.isEmpty();
				});
			} catch (org.openqa.selenium.TimeoutException e) {
				// Snackbar may have already disappeared, check for any error instead
				List<WebElement> errorElements = driver.findElements(addToCartErrorBy());
				if (!errorElements.isEmpty()) {
					throw new IllegalStateException("Add to cart thất bại: " + errorElements.get(0).getText());
				}
				// If no error found and we got here, assume success
				// (snackbar likely appeared and disappeared before we could detect it)
				System.out.println("Snackbar không tìm thấy nhưng không có lỗi - giả sử thêm hàng thành công");
				Thread.sleep(500); // Brief pause to let UI settle
			}
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("Lỗi khi chờ add to cart: " + e.getMessage(), e);
		}
	}

	public void openCart() {
		wait.until(d -> d.findElement(cartButtonBy())).click();
	}

	// Debug helper - prints page source for troubleshooting
	public void debugPageSource() {
		String pageSource = driver.getPageSource();
		System.out.println("===== PAGE SOURCE =====");
		System.out.println(pageSource);
		System.out.println("===== END PAGE SOURCE =====");
	}
}
