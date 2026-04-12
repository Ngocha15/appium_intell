package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SignupPage {

    AndroidDriver driver;
    WebDriverWait wait;

    public SignupPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ===== LOCATORS =====

    private WebElement getFieldByXPath(String resourceId) {
        return wait.until(driver -> {
            // User-provided hierarchy from inspector.
            By nested = AppiumBy.xpath(
                "//android.widget.EditText[@resource-id='" + resourceId + "']/android.widget.EditText"
            );
            // Fallback in case there is no nested EditText node.
            By direct = AppiumBy.xpath(
                "//android.widget.EditText[@resource-id='" + resourceId + "']"
            );

            java.util.List<WebElement> nestedMatches = driver.findElements(nested);
            if (!nestedMatches.isEmpty()) {
                return nestedMatches.get(0);
            }

            java.util.List<WebElement> directMatches = driver.findElements(direct);
            if (!directMatches.isEmpty()) {
                return directMatches.get(0);
            }

            return null;
        });
    }

    private WebElement chkAgree() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"qa.signup.terms_checkbox\")"
            )
        ));
    }

    private By signupButtonContainerBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"qa.signup.submit_button\")"
        );
    }

    private By nativeSignupButtonBy() {
        return AppiumBy.xpath(
            "//*[contains(@content-desc,'qa.signup.submit_button')]//android.widget.Button"
        );
    }

    private WebElement nativeSignupButton() {
        return wait.until(driver -> {
            List<WebElement> nativeButtons = driver.findElements(nativeSignupButtonBy());
            if (!nativeButtons.isEmpty()) {
                return nativeButtons.get(0);
            }

            List<WebElement> containers = driver.findElements(signupButtonContainerBy());
            if (!containers.isEmpty()) {
                return containers.get(0);
            }

            return null;
        });
    }

    private WebElement btnSignUp() {
        return wait.until(ExpectedConditions.elementToBeClickable(nativeSignupButton()));
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
        inputText(getFieldByXPath("qa.signup.full_name_input"), fullName);
    }

    public void enterEmail(String email) {
        inputText(getFieldByXPath("qa.signup.email_input"), email);
    }

    public void enterPassword(String password) {
        inputText(getFieldByXPath("qa.signup.password_input"), password);
    }

    public void clickCheckbox() {
        chkAgree().click();
    }

    public void clickSignupButton() {
        btnSignUp().click();
    }

    public boolean isSignupButtonEnabled() {
        try {
            WebElement button = nativeSignupButton();
            String enabledAttr = button.getAttribute("enabled");
            String clickableAttr = button.getAttribute("clickable");
            String displayedAttr = button.getAttribute("displayed");

            boolean isEnabled = "true".equalsIgnoreCase(enabledAttr) || button.isEnabled();
            boolean isClickable = "true".equalsIgnoreCase(clickableAttr);
            boolean isDisplayed = "true".equalsIgnoreCase(displayedAttr) || button.isDisplayed();

            return isEnabled && isClickable && isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }

}