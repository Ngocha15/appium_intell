package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;

public class SigningPage {

    AndroidDriver driver;

    public SigningPage(AndroidDriver driver) {
        this.driver = driver;
    }

    private WebElement btnSignUp() {
        return driver.findElement(
                AppiumBy.xpath("//android.view.View[@content-desc='Sign Up']")
        );
    }

    public void clickSignUp() {
        btnSignUp().click();
    }
}
