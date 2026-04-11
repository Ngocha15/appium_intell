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

    private WebElement getFieldByIndex(int index) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                AppiumBy.className("android.widget.EditText")
        )).get(index);
    }

    private WebElement chkAgree() {
        return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.CheckBox")
        ));
    }

    private WebElement btnSignUp() {
        return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[@content-desc='Sign Up']")
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
        inputText(getFieldByIndex(0), fullName);
    }

    public void enterEmail(String email) {
        inputText(getFieldByIndex(1), email);
    }

    public void enterPassword(String password) {
        inputText(getFieldByIndex(2), password);
    }

    public void clickCheckbox() {
        chkAgree().click();
    }

    public void clickSignupButton() {
        btnSignUp().click();
    }

    private final By fullNameError = AppiumBy.xpath("//*[contains(@content-desc,'full name')]");
    private final By emailError = AppiumBy.xpath("//*[contains(@content-desc,'valid email')]");
    private final By passwordError = AppiumBy.xpath("//*[contains(@content-desc,'Password')]");
    private final By checkboxError = AppiumBy.xpath("//*[contains(@content-desc,'Terms')]");

    public boolean isFullNameErrorDisplayed() {
        try {
            return wait.until(d -> d.findElement(fullNameError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmailErrorDisplayed() {
        try {
            return wait.until(d -> d.findElement(emailError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordErrorDisplayed() {
        try {
            return wait.until(d -> d.findElement(passwordError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCheckboxErrorDisplayed() {
        try {
            return wait.until(d -> d.findElement(checkboxError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}