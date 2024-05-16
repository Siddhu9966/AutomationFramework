package PageObjects;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

public class Testingcurrentpage {

    public static void main(String args[]) throws InterruptedException, IOException {


        ChromeOptions opt = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        opt.setExperimentalOption("debuggerAddress", "localhost:60444");

        WebDriver driver = new ChromeDriver(opt);
    }
}
