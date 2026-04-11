package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class OnboardingPage {

    AndroidDriver driver;

    public OnboardingPage(AndroidDriver driver) {
        this.driver = driver;
    }

    public void clickSkip() {
        driver.findElement(
                AppiumBy.xpath("//android.view.View[@content-desc='Skip']")
        ).click();
    }

    // optional: check tồn tại
    public void skipIfDisplayed() {
        try {
            if (driver.findElement(
                    AppiumBy.xpath("//android.view.View[@content-desc='Skip']")
            ).isDisplayed()) {
                clickSkip();
            }
        } catch (Exception ignored) {}
    }
}