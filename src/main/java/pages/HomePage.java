package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    public HomePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===== LOCATOR (By thay vì WebElement) =====
    private By greetingBy(String fullName) {
        return AppiumBy.xpath(
                "//android.view.View[contains(@content-desc,'Hi') and contains(@content-desc,'" + fullName + "')]"
        );
    }

    private By homeBy() {
        return AppiumBy.xpath(
                "//android.view.View[contains(@content-desc,'Hi')]"
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
    
}