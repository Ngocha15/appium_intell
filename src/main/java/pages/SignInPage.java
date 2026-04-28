package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SignInPage {

    public static final String EMPTY_EMAIL_MESSAGE = "Please enter your email";
    public static final String EMPTY_PASSWORD_MESSAGE = "Please enter your password";

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
    // ===== LOCATORS + Helpers like SignupPage =====
    private WebElement getFieldByXPath(String resourceId) {
        return wait.until(driver -> {
            By nested = AppiumBy.xpath(
                "//android.widget.EditText[@resource-id='" + resourceId + "']/android.widget.EditText"
            );
            By direct = AppiumBy.xpath(
                "//android.widget.EditText[@resource-id='" + resourceId + "']"
            );

            List<WebElement> nestedMatches = driver.findElements(nested);
            if (!nestedMatches.isEmpty()) {
                return nestedMatches.get(0);
            }

            List<WebElement> directMatches = driver.findElements(direct);
            if (!directMatches.isEmpty()) {
                return directMatches.get(0);
            }

            return null;
        });
    }

    private By signInButtonContainerBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"qa.login.sign_in_button\")"
        );
    }

    private By nativeSignInButtonBy() {
        return AppiumBy.xpath(
            "//*[contains(@content-desc,'qa.login.sign_in_button')]//android.widget.Button"
        );
    }

    private WebElement nativeSignInButton() {
        return wait.until(driver -> {
            List<WebElement> nativeButtons = driver.findElements(nativeSignInButtonBy());
            if (!nativeButtons.isEmpty()) {
                return nativeButtons.get(0);
            }

            List<WebElement> containers = driver.findElements(signInButtonContainerBy());
            if (!containers.isEmpty()) {
                return containers.get(0);
            }

            return null;
        });
    }

    private WebElement btnSignIn() {
        return wait.until(ExpectedConditions.elementToBeClickable(nativeSignInButton()));
    }

    private void inputText(WebElement el, String text) {
        el.click();
        el.clear();
        el.sendKeys(text);
        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {}
    }

    public void enterEmail(String email) {
        inputText(getFieldByXPath("qa.login.email_input"), email);
    }

    public void enterPassword(String password) {
        inputText(getFieldByXPath("qa.login.password_input"), password);
    }

    public void clickSignIn() {
        btnSignIn().click();
    }

    public boolean isSignInButtonEnabled() {
        try {
            WebElement button = nativeSignInButton();
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

    public String getEmailValidationText() {
        return getValidationMessage(getEmailValidationId());
    }

    public String getPasswordValidationText() {
        return getValidationMessage(getPasswordValidationId());
    }

    public String getEmailValidationId() {
        return "qa.login.email.validation_message";
    }

    public String getPasswordValidationId() {
        return "qa.login.password.validation_message";
    }

    public WebElement getValidationElement(String validationKey) {
        By validationMessageBy = AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"" + validationKey + "\")"
        );

        // Prefer presence via findElements to avoid visibility/animation flakiness
        return wait.until(d -> {
            List<WebElement> els = d.findElements(validationMessageBy);
            if (!els.isEmpty()) {
                return els.get(0);
            }
            return null;
        });
    }

    public WebElement getEmailValidationElement() {
        return getValidationElement(getEmailValidationId());
    }

    public WebElement getPasswordValidationElement() {
        return getValidationElement(getPasswordValidationId());
    }

    public String getCommonErrorDialogContentText() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(commonErrorDialogContentBy()));
        return el.getText();
    }

    public String getValidationMessage(String validationKey) {
        try {
            WebElement message = getValidationElement(validationKey);
            if (message == null) return "";

            String text = "";
            try { text = message.getText(); } catch (Exception ignored) {}
            if (text != null && !text.trim().isEmpty()) return text.trim();

            // Fallback: some Flutter elements expose the message via content-desc
            try {
                String desc = message.getAttribute("content-desc");
                if (desc != null && !desc.trim().isEmpty()) return desc.trim();
            } catch (Exception ignored) {}

            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isValidationMessageDisplayed(String validationKey) {
        try {
            By validationMessageBy = AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"" + validationKey + "\")"
            );
            List<WebElement> els = driver.findElements(validationMessageBy);
            if (els.isEmpty()) return false;
            for (WebElement el : els) {
                try { if (el.isDisplayed()) return true; } catch (Exception ignored) {}
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
