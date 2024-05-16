package PageObjects;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

public class TestCurrentOpenedPage{


    public  static void main(String args[]) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver(options);

// getCapabilities will return all browser capabilities

        Capabilities cap=driver.getCapabilities();

// asMap method will return all capability in MAP
        Map<String, Object> myCap=cap.asMap();

// print the map data-
        System.out.println(myCap);


    }
}





















