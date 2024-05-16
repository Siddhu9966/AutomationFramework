package driver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;

public class DriverFactory {
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        WebDriver driver = webDriver.get();
        if (driver == null || ((RemoteWebDriver) driver).getSessionId() == null) {
            driver = createDriver();
            webDriver.set(driver);
        }
        return driver;
    }

    private static WebDriver createDriver() {
        WebDriver driver = null;
        switch (getBrowserType()) {
            case "chrome" -> {
                try {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    WebDriverManager.chromedriver().setup();
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    //chromeOptions.setExperimentalOption("debuggerAddress", "localhost:51131");
                    driver = new ChromeDriver(chromeOptions);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                    driver.manage().window().maximize();
                }
                catch(WebDriverException e){
                    e.printStackTrace();
                }
                break;
            }
            case "edge" -> {
                try {

                    EdgeOptions edgeOptions = new EdgeOptions();
                    WebDriverManager.edgedriver().setup();
                    edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    //edgeOptions.setExperimentalOption("debuggerAddress", "localhost:56204");
                    driver = new EdgeDriver(edgeOptions);
//                    driver = SelfHealingDriver.create(driver1);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                    driver.manage().window().maximize();
                }catch(WebDriverException e){
                    e.printStackTrace();
                }
                break;
            }
            case "firefox" -> {
                try {
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    driver = new FirefoxDriver(firefoxOptions);
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                    driver.manage().window().maximize();
                }

                catch(WebDriverException e){
                    e.printStackTrace();
                }
                break;
            }
            case "headlesschrome" -> {
                ChromeOptions options = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                options.addArguments("headless");
                driver = new ChromeDriver(options);
                driver.manage().window().setSize(new Dimension(1600,700));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            }
            case "headlessedge" -> {
                EdgeOptions options = new EdgeOptions();
                WebDriverManager.edgedriver().setup();
                options.addArguments("headless");
                driver = new EdgeDriver(options);
                driver.manage().window().setSize(new Dimension(1600,700));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            }
            case "remotebrowserchrome" -> {
                try {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("browserName", "chrome");
//                    chromeOptions.setCapability("browserVersion", "104");
                    chromeOptions.setCapability("platformName", "Windows");
                    chromeOptions.setCapability("se:name", "My simple test");
                    chromeOptions.setCapability("se:sampleMetadata", "Sample metadata value");
                    driver = new RemoteWebDriver(new URL("http://192.168.5.103:4444"), chromeOptions);
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                    driver.manage().window().maximize();
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        }

        if(driver != null)
            return driver;
        else
            throw new WebDriverException("No Driver object is created");
    }

    private static String getBrowserType() {
        String browserType = null;

        try {
            Properties properties = new Properties();
            FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/properties/config.properties");
            properties.load(file);
            browserType = properties.getProperty("browser").toLowerCase().trim();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return browserType;
    }

    public static  void cleanupDriver() {
        webDriver.get().quit();
        webDriver.remove();
    }
}
