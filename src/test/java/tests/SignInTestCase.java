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
import pages.SignInPage;

import java.time.Duration;

public class SignInTestCase extends BaseTest {

    private static final String EMPTY_EMAIL_MESSAGE = "Please enter your email";
    private static final String EMPTY_PASSWORD_MESSAGE = "Please enter your password";

    // Use SignInPage for all sign-in interactions

    @Test
    public void testSignin_EmptyEmail_ShowRequiredMessage() {
        SignInPage signIn = new SignInPage(driver);
        signIn.open();

        signIn.enterEmail("");
        signIn.enterPassword("1233haah@");

        if (signIn.isSignInButtonEnabled()) {
            signIn.clickSignIn();
        }

        Assert.assertEquals(
            signIn.getEmailValidationText(),
            EMPTY_EMAIL_MESSAGE,
            "Message khi bỏ trống email không đúng"
        );
    }

    @Test
    public void testSignin_EmptyPassword_ShowRequiredMessage() {
        SignInPage signIn = new SignInPage(driver);
        signIn.open();

        signIn.enterEmail("heheh@gmail.com");
        signIn.enterPassword("");

        if (signIn.isSignInButtonEnabled()) {
            signIn.clickSignIn();
        }

        Assert.assertEquals(
            signIn.getPasswordValidationText(),
            EMPTY_PASSWORD_MESSAGE,
            "Message khi bỏ trống password không đúng"
        );
    }

    @Test
    public void testSignin_NonExistingAccount_ShowErrorDialog() {
        SignInPage signIn = new SignInPage(driver);
        signIn.open();

        signIn.enterEmail("mqmq@gmail.com");
        signIn.enterPassword("1234hqhq))");
        signIn.clickSignIn();

        String content = signIn.getCommonErrorDialogContentText();
        Assert.assertTrue(
            content != null && !content.trim().isEmpty(),
            "Không hiển thị nội dung lỗi cho tài khoản không tồn tại"
        );
    }

    @Test
    public void testSignin_ButtonDisabled_WhenOneFieldEmpty() {
        SignInPage signIn = new SignInPage(driver);
        signIn.open();

        signIn.enterEmail("");
        signIn.enterPassword("1233haah@");

        Assert.assertTrue(
            !signIn.isSignInButtonEnabled(),
            "Nút Sign In phải disable khi email rỗng"
        );

        signIn.enterEmail("heheh@gmail.com");
        signIn.enterPassword("");

        Assert.assertTrue(
            !signIn.isSignInButtonEnabled(),
            "Nút Sign In phải disable khi password rỗng"
        );
    }

    @Test
    public void testSignin_Success_WithExistingAccount() {
        SignInPage signIn = new SignInPage(driver);
        signIn.open();

        signIn.enterEmail("mimi135@gmail.com");
        signIn.enterPassword("123456hoho))");
        signIn.clickSignIn();

        HomePage homePage = new HomePage(driver);
        homePage.waitUntilHomeReady();

        Assert.assertTrue(
            homePage.isHomeDisplayed(),
            "Đăng nhập thành công nhưng không vào Home"
        );
    }
}
