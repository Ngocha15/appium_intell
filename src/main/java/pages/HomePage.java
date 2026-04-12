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
        // Home có thể render trước, nhưng greeting text đôi khi lên chậm hơn list sản phẩm.
        // Ưu tiên dấu hiệu featured products để tránh false negative khi chờ Home.
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(homeBy()));
        } catch (Exception ignored) {
            // Fallback to featured products visibility.
        }

        waitUntilFeaturedProductsReady();
    }

    public void waitUntilFeaturedProductsReady() {
        wait.until(driver -> {
            List<WebElement> products = driver.findElements(featuredProductBy());
            return !products.isEmpty() && products.get(0).isDisplayed();
        });
    }

    public boolean hasFeaturedProducts() {
        return !driver.findElements(featuredProductBy()).isEmpty();
    }

    public boolean hasAtLeastFeaturedProducts(int count) {
        return driver.findElements(featuredProductBy()).size() >= count;
    }

    public void openFirstFeaturedProduct() {
        List<WebElement> products = wait.until(driver -> {
            List<WebElement> found = driver.findElements(featuredProductBy());
            return found.isEmpty() ? null : found;
        });

        products.get(0).click();
    }

    public String getFeaturedProductQaIdAtIndex(int index) {
        List<WebElement> products = wait.until(driver -> {
            List<WebElement> found = driver.findElements(featuredProductBy());
            return found.size() > index ? found : null;
        });

        WebElement product = products.get(index);
        String qaId = product.getAttribute("content-desc");
        if (qaId == null || qaId.isBlank()) {
            qaId = product.getAttribute("description");
        }
        return (qaId == null || qaId.isBlank()) ? "index-" + index : qaId;
    }

    public void openFeaturedProductAtIndex(int index) {
        List<WebElement> products = wait.until(driver -> {
            List<WebElement> found = driver.findElements(featuredProductBy());
            return found.size() > index ? found : null;
        });

        products.get(index).click();
    }

    public String openFeaturedProductDifferentFrom(String excludedQaId) {
        List<WebElement> products = wait.until(driver -> {
            List<WebElement> found = driver.findElements(featuredProductBy());
            return found.size() > 1 ? found : null;
        });

        String safeExcluded = (excludedQaId == null || excludedQaId.isBlank())
            ? "index-0"
            : excludedQaId;

        int index = 0;
        for (WebElement product : products) {
            String qaId = product.getAttribute("content-desc");
            if (qaId == null || qaId.isBlank()) {
                qaId = product.getAttribute("description");
            }
            if (qaId == null || qaId.isBlank()) {
                qaId = "index-" + index;
            }

            if (!qaId.equals(safeExcluded)) {
                product.click();
                return qaId;
            }
            index++;
        }

        throw new IllegalStateException("Không tìm thấy featured product khác sản phẩm đầu tiên");
    }
    
}