package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class OrderSuccessPage {

	private final AndroidDriver driver;

	public OrderSuccessPage(AndroidDriver driver) {
		this.driver = driver;
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
		return !driver.findElements(myOrdersSearchBy()).isEmpty();
	}

	public boolean hasAnyOrderCard() {
		return !driver.findElements(myOrdersCardBy()).isEmpty();
	}
}
