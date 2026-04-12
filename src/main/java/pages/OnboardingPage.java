package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

import java.time.Duration;

public class OnboardingPage {

    AndroidDriver driver;

    public OnboardingPage(AndroidDriver driver) {
        this.driver = driver;
    }

    public void clickSkip() {
        driver.findElement(
            AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"qa.onboarding.skip_button\")"
            )
        ).click();
    }

    // optional: check tồn tại
    public void skipIfDisplayed() {
        try {

            driver.findElement(
                    AppiumBy.androidUIAutomator(
                            "new UiSelector().descriptionContains(\"qa.onboarding.skip_button\")"
                    )).click();

        } catch (Exception ignored) {
            System.out.println(ignored.toString());
        }
    }
}