package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.OnboardingPage;
import pages.SigningPage;
import pages.SignupPage;

public class SignUpTestCase extends BaseTest {

    // ===== COMMON STEP =====
    private SignupPage navigateToSignup() {
        OnboardingPage onboarding = new OnboardingPage(driver);
        SigningPage signinPage = new SigningPage(driver);

        onboarding.skipIfDisplayed();
        signinPage.clickSignUp();

        return new SignupPage(driver);
    }

    // ===== NEGATIVE TEST CASES =====
    @Test
    public void testSignup_InvalidFullName_WithNumber() {
        SignupPage signupPage = navigateToSignup();

        signupPage.enterFullName("Tuti123");
        signupPage.enterEmail("user@gmail.com");
        signupPage.enterPassword("Abc@1234");
        signupPage.clickCheckbox();
        signupPage.clickSignupButton();

        Assert.assertTrue(
                signupPage.isFullNameErrorDisplayed(),
                "Không hiển thị lỗi khi Full Name chứa số"
        );
    }

    @Test
    public void testSignup_InvalidEmail() {
        SignupPage signupPage = navigateToSignup();

        signupPage.enterFullName("Nguyen Van A");
        signupPage.enterEmail("abc.com");
        signupPage.enterPassword("Abc@1234");
        signupPage.clickCheckbox();
        signupPage.clickSignupButton();

        Assert.assertTrue(
                signupPage.isEmailErrorDisplayed(),
                "Không hiển thị lỗi Email"
        );
    }

    @Test
    public void testSignup_InvalidPassword() {
        SignupPage signupPage = navigateToSignup();

        signupPage.enterFullName("Nguyen Van A");
        signupPage.enterEmail("user@gmail.com");
        signupPage.enterPassword("123"); // < 8 ký tự
        signupPage.clickCheckbox();
        signupPage.clickSignupButton();

        Assert.assertTrue(
                signupPage.isPasswordErrorDisplayed(),
                "Không hiển thị lỗi Password"
        );
    }

    @Test
    public void testSignup_NotCheckTerms() {
        SignupPage signupPage = navigateToSignup();

        signupPage.enterFullName("Nguyen Van A");
        signupPage.enterEmail("user@gmail.com");
        signupPage.enterPassword("Abc@1234");
        // KHÔNG click checkbox
        signupPage.clickSignupButton();

        Assert.assertTrue(
                signupPage.isCheckboxErrorDisplayed(),
                "Không hiển thị lỗi checkbox"
        );
    }

    // ===== POSITIVE TEST CASE (ĐỂ CUỐI) =====

    @Test
    public void testSignup_Success() {

        System.out.println("Start Signup Positive Test");

        String fullName = "User" + System.currentTimeMillis();

        SignupPage signupPage = navigateToSignup();
        HomePage homePage = new HomePage(driver);

        signupPage.enterFullName(fullName);
        signupPage.enterEmail("user" + System.currentTimeMillis() + "@gmail.com");
        signupPage.enterPassword("Abc@1234");
        signupPage.clickCheckbox();
        signupPage.clickSignupButton();

        // VERIFY
        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "Không vào được Home sau signup"
        );

        Assert.assertTrue(
                homePage.isUserLoggedIn(fullName),
                "Sai tên user sau signup"
        );

        System.out.println("Signup SUCCESS");
    }
}