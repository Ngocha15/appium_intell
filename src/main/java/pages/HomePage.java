package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public HomePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===== LOCATOR (By thay vì WebElement) =====
    private By greetingBy(String fullName) {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"Hi\").descriptionContains(\"" + fullName + "\")"
        );
    }

    private By homeBy() {
        return AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"Hi\")"
        );
    }

    private By featuredProductBy() {
        return AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"qa.home.featured_product.card.\")"
        );
    }

    // ===== ACTION / GET ELEMENT =====
    private WebElement getGreetingElement(String fullName) {
        return wait.until(d -> d.findElement(greetingBy(fullName)));
    }

    private WebElement getHomeElement() {
        return wait.until(d -> d.findElement(homeBy()));
    }

    // ===== VERIFY USER ĐÃ LOGIN =====
    public boolean isUserLoggedIn(String fullName) {
        try {
            return getGreetingElement(fullName).isDisplayed();
        } catch (Exception e) {
            System.out.println("Không tìm thấy greeting với user: " + fullName);
            return false;
        }
    }

    // ===== VERIFY VÀO HOME =====
    public boolean isHomeDisplayed() {
        try {
            return getHomeElement().isDisplayed();
        } catch (Exception e) {
            System.out.println("Không vào được Home");
            return false;
        }
    }

    public void waitUntilHomeReady() {
        // Home có thể render trước, nhưng data vẫn loading.
        // Chờ đến khi greeting hiển thị và có ít nhất 1 featured product.
        wait.until(ExpectedConditions.visibilityOfElementLocated(homeBy()));

        wait.until(driver -> {
            List<WebElement> products = driver.findElements(featuredProductBy());
            return !products.isEmpty() && products.get(0).isDisplayed();
        });
    }

    public boolean hasFeaturedProducts() {
        return !driver.findElements(featuredProductBy()).isEmpty();
    }

    public void openFirstFeaturedProduct() {
        List<WebElement> products = wait.until(driver -> {
            List<WebElement> found = driver.findElements(featuredProductBy());
            return found.isEmpty() ? null : found;
        });

        products.get(0).click();
    }

    public void openFeaturedProductAtIndex(int index) {
        List<WebElement> products = wait.until(driver -> {
            List<WebElement> found = driver.findElements(featuredProductBy());
            return found.size() > index ? found : null;
        });

        products.get(index).click();
    }
    
}