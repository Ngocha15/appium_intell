package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.URL;
import java.time.Duration;

public class BaseTest {

    protected AndroidDriver driver;

    @BeforeClass
    public void setUp() throws Exception {

        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setPlatformVersion("13");
        options.setDeviceName("e7a425eb87f2");
        options.setAutomationName("UiAutomator2");

        options.setAppPackage("com.thinhan.shoestore.stg");
        options.setAppActivity("com.thinhan.shoestore.MainActivity");

        options.setCapability("appium:unicodeKeyboard", true);
        options.setCapability("appium:resetKeyboard", true);

        driver = new AndroidDriver(
                new URL("http://192.168.0.119:4723/wd/hub"),
                options
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        System.out.println("App started successfully");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("App closed");
        }
    }
}
