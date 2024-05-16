package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestRunConfig {

    public static String BROWSER = get("browser");

    public static final String THREAD_COUNT = get("threadCount");

    public static int RETRY_COUNTER = Integer.parseInt(get("retryCounter"));

    public static final boolean ScreenShot_All = Boolean.parseBoolean(get("Screenshot"));

    public static String ExcelFile = get("Excel_File_name");

    public TestRunConfig() {
    }

    public static String get(String property) {
        String value = null;

        if (System.getenv(property) != null) {
            value = System.getenv(property);
        } else if (System.getenv(property.toUpperCase()) != null) {
            value = System.getenv(property);
        } else if (System.getProperty(property) != null) {
            value = System.getProperty(property);
        } else {
            try {
                FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/properties/config.properties");
                Properties properties = new Properties();
                properties.load(inputStream);
                value = properties.getProperty(property);

            }catch (IOException ex){
            }
        }
        return value;
    }



}
