package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignInPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public SignInPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private By emailInputBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"qa.login.email_input\")"
        );
    }

    private By passwordInputBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"qa.login.password_input\")"
        );
    }

    private By signInButtonBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"qa.login.sign_in_button\")"
        );
    }

    private By commonErrorDialogContentBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"qa.common.error_dialog.content\")"
        );
    }

    private By emailValidationBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"qa.login.email.validation_message\")"
        );
    }

    private By passwordValidationBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"qa.login.password.validation_message\")"
        );
    }

    public void open() {
        OnboardingPage onboarding = new OnboardingPage(driver);
        onboarding.skipIfDisplayed();
    }

    public void enterEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInputBy()));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInputBy()));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickSignIn() {
        wait.until(ExpectedConditions.elementToBeClickable(signInButtonBy())).click();
    }

    public boolean isSignInButtonEnabled() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(signInButtonBy())).isEnabled();
    }

    public String getEmailValidationText() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(emailValidationBy()));
        return el.getText();
    }

    public String getPasswordValidationText() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordValidationBy()));
        return el.getText();
    }

    public String getCommonErrorDialogContentText() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(commonErrorDialogContentBy()));
        return el.getText();
    }
}
