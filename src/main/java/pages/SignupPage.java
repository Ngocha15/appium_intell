package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignupPage {

    AndroidDriver driver;
    WebDriverWait wait;

    public SignupPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ===== LOCATORS (dùng chung locator cho EditText) =====

    private WebElement getFieldByQaId(String qaId) {
        return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.androidUIAutomator(
                        "new UiSelector().descriptionContains(\"" + qaId + "\")"
                )
        ));
    }

    private WebElement chkAgree() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"qa.signup.terms_checkbox\")"
            )
        ));
    }

    private WebElement btnSignUp() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"qa.signup.submit_button\")"
            )
        ));
    }

    // ===== ACTIONS =====

    private void inputText(WebElement el, String text) {
        el.click();
        el.clear();
        el.sendKeys(text);
        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {}
    }

    public void enterFullName(String fullName) {
        inputText(getFieldByQaId("qa.signup.full_name_input"), fullName);
    }

    public void enterEmail(String email) {
        inputText(getFieldByQaId("qa.signup.email_input"), email);
    }

    public void enterPassword(String password) {
        inputText(getFieldByQaId("qa.signup.password_input"), password);
    }

    public void clickCheckbox() {
        chkAgree().click();
    }

    public void clickSignupButton() {
        btnSignUp().click();
    }

    private WebElement signupButton() {
        return wait.until(d -> d.findElement(
                AppiumBy.androidUIAutomator(
                        "new UiSelector().descriptionContains(\"qa.signup.submit_button\")"
                )));
    }

    public boolean isSignupButtonEnabled() {
        try {
            return signupButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

}