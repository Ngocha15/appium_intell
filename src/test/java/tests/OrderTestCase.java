package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.OnboardingPage;
import pages.OrderSuccessPage;
import pages.ProductPage;
import pages.SigningPage;
import pages.SignupPage;

public class OrderTestCase extends BaseTest {

    private SignupPage navigateToSignup() {
        OnboardingPage onboarding = new OnboardingPage(driver);
        SigningPage signinPage = new SigningPage(driver);

        onboarding.skipIfDisplayed();
        signinPage.clickSignUp();

        return new SignupPage(driver);
    }

    @Test
    public void testOrder_PlacePickupOrder_Success() {
        long timestamp = System.currentTimeMillis();
        String fullName = "OrderUser" + timestamp;
        String email = "orderuser" + timestamp + "@gmail.com";

        SignupPage signupPage = navigateToSignup();
        HomePage homePage = new HomePage(driver);

        signupPage.enterFullName(fullName);
        signupPage.enterEmail(email);
        signupPage.enterPassword("Abc@1234");
        signupPage.clickCheckbox();
        signupPage.clickSignupButton();

        Assert.assertTrue(homePage.isHomeDisplayed(), "Không vào được Home sau signup");
        Assert.assertTrue(homePage.isUserLoggedIn(fullName), "Sai tên user sau signup");
        Assert.assertTrue(homePage.hasFeaturedProducts(), "Không có featured products để chọn");

        homePage.openFirstFeaturedProduct();

        ProductPage productPage = new ProductPage(driver);
        Assert.assertTrue(productPage.isDisplayed(), "Không mở được màn Product Details");

        productPage.clickAddToCart();
        productPage.openCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isDisplayed(), "Không mở được màn Cart");
        Assert.assertTrue(cartPage.hasCartItems(), "Cart không có item sau khi add to cart");

        cartPage.proceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isDisplayed(), "Không mở được màn Checkout");

        checkoutPage.placePickupOrder();

        OrderSuccessPage orderSuccessPage = new OrderSuccessPage(driver);
        Assert.assertTrue(
                orderSuccessPage.isMyOrdersPageVisible(),
                "Không chuyển tới màn My Orders sau khi đặt hàng"
        );
    }
}