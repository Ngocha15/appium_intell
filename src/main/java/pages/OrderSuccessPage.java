package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderSuccessPage {

	private final AndroidDriver driver;
	private final WebDriverWait wait;

	public OrderSuccessPage(AndroidDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	private By myOrdersTitleBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.my_orders.title_text\")"
		);
	}

	private By myOrdersSearchBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.my_orders.search_input\")"
		);
	}

	private By myOrdersCardBy() {
		return AppiumBy.androidUIAutomator(
				"new UiSelector().descriptionContains(\"qa.my_orders.order_card.\")"
		);
	}

	public boolean isMyOrdersPageVisible() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(myOrdersTitleBy()))
					.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void waitUntilMyOrdersPageVisible() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(myOrdersTitleBy()));
	}

	public boolean hasAnyOrderCard() {
		return !driver.findElements(myOrdersCardBy()).isEmpty();
	}
}
