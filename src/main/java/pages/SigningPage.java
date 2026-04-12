package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;

public class SigningPage {

    AndroidDriver driver;

    public SigningPage(AndroidDriver driver) {
        this.driver = driver;
    }


    public void clickSignUp() {
        driver.findElement(
                AppiumBy.androidUIAutomator(
                        "new UiSelector().descriptionContains(\"qa.login.goto_signup_link\")"
                )).click();
    }
}
