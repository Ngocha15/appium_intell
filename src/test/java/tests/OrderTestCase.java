package tests;

import base.BaseTest;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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

    private void backToHomeWithRetry(HomePage homePage) {
        for (int attempt = 0; attempt < 5; attempt++) {
            if (homePage.hasFeaturedProducts()) {
                homePage.waitUntilFeaturedProductsReady();
                return;
            }

            try {
                driver.pressKey(new KeyEvent(AndroidKey.BACK));
            } catch (Exception ignored) {
                driver.navigate().back();
            }

            try {
                homePage.waitUntilFeaturedProductsReady();
                return;
            } catch (Exception ignored) {
                // Retry sending back in case first back does not pop Product Details.
            }
        }

        Assert.fail("Không thể quay lại màn Home sau khi thêm sản phẩm thứ nhất");
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
        Assert.assertTrue(
            homePage.hasAtLeastFeaturedProducts(2),
            "Cần ít nhất 2 featured products để test luồng thêm 2 sản phẩm khác nhau"
        );

        String firstProductQaId = homePage.getFeaturedProductQaIdAtIndex(0);
        homePage.openFeaturedProductAtIndex(0);

        ProductPage productPage = new ProductPage(driver);
        Assert.assertTrue(productPage.isDisplayed(), "Không mở được màn Product Details");

        productPage.selectFirstAvailableSizeIfPresent();
        productPage.selectFirstAvailableColorIfPresent();

        productPage.clickAddToCart();
        productPage.waitForAddToCartSuccess();
        backToHomeWithRetry(homePage);

        String secondProductQaId = homePage.openFeaturedProductDifferentFrom(firstProductQaId);
        Assert.assertNotEquals(
            secondProductQaId,
            firstProductQaId,
            "Sản phẩm thứ 2 phải khác sản phẩm thứ nhất"
        );

        productPage = new ProductPage(driver);
        Assert.assertTrue(productPage.isDisplayed(), "Không mở được màn Product Details cho sản phẩm thứ 2");

        productPage.selectFirstAvailableSizeIfPresent();
        productPage.selectFirstAvailableColorIfPresent();

        productPage.clickAddToCart();
        productPage.waitForAddToCartSuccess();
        productPage.openCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isDisplayed(), "Không mở được màn Cart");
        Assert.assertTrue(cartPage.hasCartItems(), "Cart không có item sau khi add to cart");

        cartPage.deleteCartItemAtIndex(1);

        Assert.assertTrue(cartPage.hasCartItems(), "Cart bị rỗng sau khi xóa sản phẩm thứ 2");

        cartPage.proceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isDisplayed(), "Không mở được màn Checkout");

        checkoutPage.placePickupOrder();

        OrderSuccessPage orderSuccessPage = new OrderSuccessPage(driver);
        orderSuccessPage.waitUntilMyOrdersPageVisible();
        Assert.assertTrue(
                orderSuccessPage.isMyOrdersPageVisible(),
            "Không thấy title My Orders sau khi đặt hàng"
        );

        Assert.assertTrue(
            orderSuccessPage.hasAnyOrderCard(),
            "Không tìm thấy order card sau khi đặt hàng"
        );
    }
}