
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.driver.WebDriverSetup;

public class GoogleSearchTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // 使用 WebDriverSetup 初始化 driver
        driver = WebDriverSetup.createDriver();
    }

    @Test
    public void testGoogleSearch() {
        driver.get("https://www.google.com");
        System.out.println("Page title is: " + driver.getTitle());
    }

    @AfterMethod
    public void tearDown() {
        // 測試結束後關閉瀏覽器
        if (driver != null) {
            driver.quit();
        }
    }
}
