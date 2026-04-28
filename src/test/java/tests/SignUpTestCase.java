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
    public void testSignup_InvalidFullName_Empty() {
        SignupPage signupPage = navigateToSignup();
        signupPage.enterFullName("");
        signupPage.enterEmail("user@gmail.com");
        signupPage.enterPassword("Abc@1234");
        signupPage.clickCheckbox();

        Assert.assertTrue(
            !signupPage.isSignupButtonEnabled(),
            "Nút Sign Up vẫn bật khi full name không hợp lệ"
        );


    }

    @Test
    public void testSignup_InvalidEmail() {
        SignupPage signupPage = navigateToSignup();

        signupPage.enterFullName("Nguyen Van A");
        signupPage.enterEmail("abc.com");
        signupPage.enterPassword("Abc@1234");
        signupPage.clickCheckbox();

        Assert.assertTrue(
            !signupPage.isSignupButtonEnabled(),
            "Nút Sign Up vẫn bật khi email không hợp lệ"
        );
    }

    @Test
    public void testSignup_InvalidPassword() {
        SignupPage signupPage = navigateToSignup();
        signupPage.enterFullName("Nguyen Van A");
        signupPage.enterEmail("user@gmail.com");
        signupPage.enterPassword("123"); // < 8 ký tự
        signupPage.clickCheckbox();

        Assert.assertTrue(
            !signupPage.isSignupButtonEnabled(),
            "Nút Sign Up vẫn bật khi password không hợp lệ"
        );
    }

    @Test
    public void testSignup_NotCheckTerms() {
        SignupPage signupPage = navigateToSignup();

        signupPage.enterFullName("Nguyen Van A");
        signupPage.enterEmail("user@gmail.com");
        signupPage.enterPassword("Abc@1234");
        // KHÔNG click checkbox

        Assert.assertTrue(
            !signupPage.isSignupButtonEnabled(),
            "Nút Sign Up vẫn bật khi chưa tick checkbox"
        );

    }

    // ===== POSITIVE TEST CASE (ĐỂ CUỐI) =====

    @Test
    public void testSignup_Success() {

        System.out.println("Start Signup Positive Test");

        long timestamp = System.currentTimeMillis();
        String fullName = "User" + timestamp;

        SignupPage signupPage = navigateToSignup();
        HomePage homePage = new HomePage(driver);

        signupPage.enterFullName(fullName);
        signupPage.enterEmail("user" + timestamp + "@gmail.com");
        signupPage.enterPassword("Abc@12345");
        signupPage.clickCheckbox();
        signupPage.clickSignupButton();
        homePage.waitUntilHomeReady();

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