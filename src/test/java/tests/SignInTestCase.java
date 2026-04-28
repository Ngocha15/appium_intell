package tests;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.OnboardingPage;

import java.time.Duration;

public class SignInTestCase extends BaseTest {

    private static final String EMPTY_EMAIL_MESSAGE = "Please enter your email";
    private static final String EMPTY_PASSWORD_MESSAGE = "Please enter your password";

    private WebDriverWait wait() {
        return new WebDriverWait(driver, Duration.ofSeconds(20));
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

    private By textBy(String text) {
        return AppiumBy.xpath("//*[contains(@text,\"" + text + "\")]");
    }

    private void navigateToSignIn() {
        OnboardingPage onboarding = new OnboardingPage(driver);
        onboarding.skipIfDisplayed();
    }

    private void enterEmail(String email) {
        WebElement emailInput = wait().until(ExpectedConditions.visibilityOfElementLocated(emailInputBy()));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    private void enterPassword(String password) {
        WebElement passwordInput = wait().until(ExpectedConditions.visibilityOfElementLocated(passwordInputBy()));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    private void clickSignIn() {
        wait().until(ExpectedConditions.elementToBeClickable(signInButtonBy())).click();
    }

    private boolean isSignInButtonEnabled() {
        return wait().until(ExpectedConditions.visibilityOfElementLocated(signInButtonBy())).isEnabled();
    }

    @Test
    public void testSignin_EmptyEmail_ShowRequiredMessage() {
        navigateToSignIn();

        enterEmail("");
        enterPassword("1233haah@");

        if (isSignInButtonEnabled()) {
            clickSignIn();
        }

        WebElement errorMessage = wait().until(
            ExpectedConditions.visibilityOfElementLocated(textBy(EMPTY_EMAIL_MESSAGE))
        );

        Assert.assertEquals(
            errorMessage.getText(),
            EMPTY_EMAIL_MESSAGE,
            "Message khi bỏ trống email không đúng"
        );
    }

    @Test
    public void testSignin_EmptyPassword_ShowRequiredMessage() {
        navigateToSignIn();

        enterEmail("heheh@gmail.com");
        enterPassword("");

        if (isSignInButtonEnabled()) {
            clickSignIn();
        }

        WebElement errorMessage = wait().until(
            ExpectedConditions.visibilityOfElementLocated(textBy(EMPTY_PASSWORD_MESSAGE))
        );

        Assert.assertEquals(
            errorMessage.getText(),
            EMPTY_PASSWORD_MESSAGE,
            "Message khi bỏ trống password không đúng"
        );
    }

    @Test
    public void testSignin_NonExistingAccount_ShowErrorDialog() {
        navigateToSignIn();

        enterEmail("mqmq@gmail.com");
        enterPassword("1234hqhq))");
        clickSignIn();

        WebElement errorContent = wait().until(
            ExpectedConditions.visibilityOfElementLocated(commonErrorDialogContentBy())
        );

        Assert.assertTrue(
            errorContent.getText() != null && !errorContent.getText().trim().isEmpty(),
            "Không hiển thị nội dung lỗi cho tài khoản không tồn tại"
        );
    }

    @Test
    public void testSignin_ButtonDisabled_WhenOneFieldEmpty() {
        navigateToSignIn();

        enterEmail("");
        enterPassword("1233haah@");

        Assert.assertTrue(
            !isSignInButtonEnabled(),
            "Nút Sign In phải disable khi email rỗng"
        );

        enterEmail("heheh@gmail.com");
        enterPassword("");

        Assert.assertTrue(
            !isSignInButtonEnabled(),
            "Nút Sign In phải disable khi password rỗng"
        );
    }

    @Test
    public void testSignin_Success_WithExistingAccount() {
        navigateToSignIn();

        enterEmail("mimi135@gmail.com");
        enterPassword("123456hoho))");
        clickSignIn();

        HomePage homePage = new HomePage(driver);
        homePage.waitUntilHomeReady();

        Assert.assertTrue(
            homePage.isHomeDisplayed(),
            "Đăng nhập thành công nhưng không vào Home"
        );
    }
}
