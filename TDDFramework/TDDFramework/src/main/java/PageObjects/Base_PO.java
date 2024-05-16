package PageObjects;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import driver.DriverFactory;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExtentTestManager;
import utils.TestRunConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class Base_PO {

    String loadbar="//div[@class='ngx-bar ng-star-inserted']|//body[contains(@class,'pace-running')]|//span[contains(@class,'MuiCircularProgress')]|//div[@class='vam-loader-bg' and @style='display: block;']";
    int DEFAULT_EXPLICIT_TIMEOUT = 8;
    HashMap<String, String> getobjectdatamap = new HashMap<>();

    public Base_PO() {
        PageFactory.initElements(getDriver(), this);
    }

    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
    public void waitFor(By by) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(8));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }
    public void javascriptexe(WebElement ele){
        JavascriptExecutor exe = (JavascriptExecutor) getDriver();
        exe.executeScript("arguments[0].click();", ele);
    }
    public void scrollToElemet(WebElement element) {
        JavascriptExecutor j = (JavascriptExecutor)getDriver();
        j.executeScript ("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
    }

    public  void switchtolatestwindow() {
        ArrayList<String> listoftabs = new ArrayList<String>(DriverFactory.getDriver().getWindowHandles());
        int latesttab = listoftabs.size();
        Integer NoofWindows = 1;
        if (!NoofWindows.equals(latesttab))
            getDriver().switchTo().window(listoftabs.get(latesttab - 1));
    }

    public void createdetailsfile(String filename,HashMap<String, String> Mapi) throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        Random rand = new Random();

        String filepath=System.getProperty("user.dir")+"/src/main/resources/WorkBook/TestcaseDetails/";
        FileOutputStream output1 = new FileOutputStream(filepath.replace("/","\\")+filename+"_"+date+rand.nextInt(1000)+".txt");
        String list              = "";

        for (Map.Entry<String, String> entry : Mapi.entrySet()) {
            String key    = entry.getKey();
            String value1 = entry.getValue();
            list          = list + key+ ":" +value1 + "\n";
        }

        Files.writeString(Path.of(filepath.replace("/","\\")+filename+"_"+date+".txt"),list, StandardCharsets.UTF_8);

    }

    public HashMap<String,String> sendObjectdatamap(){return getobjectdatamap;}

    public void actions(String step,String action, String xpath, String text, ExtentTest test,ExtentTestManager testR) throws InterruptedException, IOException {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_EXPLICIT_TIMEOUT));
        Actions useraction = new Actions(getDriver());
        waitforloadbar(loadbar);
        boolean Reportflag = false;

        switch (action) {


            case "sendkey" -> {
                try {
                    scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                    getDriver().findElement(By.xpath(xpath)).clear();
                    getDriver().findElement(By.xpath(xpath)).sendKeys(text);
                } catch (Exception e) {
                    xpath = CheckXpathDuplicate(xpath);
                    scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                    waitFor(By.xpath(xpath));
                    getDriver().findElement(By.xpath(xpath)).clear();
                    getDriver().findElement(By.xpath(xpath)).sendKeys(text);
                }


            }
            case "Action_sendkey_withClear" -> {
                JavascriptExecutor jsExecutor1 = (JavascriptExecutor) getDriver();
                jsExecutor1.executeScript("return document.readyState").equals("complete");
                xpath = CheckXpathDuplicate(xpath);
                WebElement ele1 = getDriver().findElement(By.xpath(xpath));


                try {
                    waitFor(By.xpath(xpath));

                    scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                    Actions action1 = new Actions(getDriver());
                    action1.click(ele1).build().perform();
                    action1.doubleClick();
                    for (int i = 0; i < 10; i++)
                        action1.keyDown(Keys.BACK_SPACE).build().perform();
                    Thread.sleep(1000);
                    action1.sendKeys(ele1, text).build().perform();
                    action1.sendKeys(Keys.ALT).build().perform();
                } catch (Exception e) {
                    try {
                        xpath = CheckXpathDuplicate(xpath);
                        Actions action1 = new Actions(getDriver());
                        action1.sendKeys(text).build().perform();
                        action1.sendKeys(Keys.ALT).build().perform();
                    } catch (Exception e1) {
                        xpath = CheckXpathDuplicate(xpath);
                        Actions action1 = new Actions(getDriver());
                        action1.sendKeys(getDriver().findElement(By.xpath(xpath)), text).build().perform();
                        action1.sendKeys(Keys.LEFT_ALT).build().perform();
                    }
                }

            }
            case "ActionSendkeywithTAB" -> {
                JavascriptExecutor jsExecutor1 = (JavascriptExecutor) getDriver();
                jsExecutor1.executeScript("return document.readyState").equals("complete");
                xpath = CheckXpathDuplicate(xpath);
                WebElement ele1 = getDriver().findElement(By.xpath(xpath));


                try {
                    waitFor(By.xpath(xpath));

                    scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                    Actions action1 = new Actions(getDriver());
                    action1.click(ele1).build().perform();
                    action1.doubleClick();
                    for (int i = 0; i < 6; i++)
                        action1.keyDown(Keys.BACK_SPACE).build().perform();
                    Thread.sleep(1000);
                    action1.sendKeys(ele1, text).build().perform();
                    action1.sendKeys(Keys.TAB).build().perform();
                } catch (Exception e) {
                    try {
                        xpath = CheckXpathDuplicate(xpath);
                        Actions action1 = new Actions(getDriver());
                        action1.sendKeys(text).build().perform();
                        action1.sendKeys(Keys.TAB).build().perform();
                    } catch (Exception e1) {
                        xpath = CheckXpathDuplicate(xpath);
                        Actions action1 = new Actions(getDriver());
                        action1.sendKeys(getDriver().findElement(By.xpath(xpath)), text).build().perform();
                        action1.sendKeys(Keys.TAB).build().perform();
                    }
                }

            }
            case "enterdata" -> {

               // getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(300));

                if (text.contains("|select")) {

                    ArrayList<String> DropdownValuesList = new ArrayList<String>();
                    String[] text1 = text.split("\\|");
                    String firstindex = text1[0];
                    xpath = CheckXpathDuplicate(xpath);

                    try {

                        scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                        WebElement ele4 = getDriver().findElement(By.xpath(xpath));
                        javascriptexe(ele4);
                        Thread.sleep(500);

                        try {
                            //wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//span[contains(@class,'k-state-focused')]")));
                            WebElement dropdownlist = getDriver().findElement(By.xpath("//span[contains(@class,'k-state-focused')]/parent::span[contains(@aria-expanded,'true')]"));
                            String aria_owns[] = dropdownlist.getAttribute("aria-owns").split("\\_");
                            String ariaownsvalue = aria_owns[0];

                            String drpval = "//ul[contains(@id,'" + ariaownsvalue + "')]";
                            drpval = CheckXpathDuplicate(drpval);

                            List<WebElement> options = getDriver().findElements(By.xpath(drpval+"/li"));


                            for (int i = 0; i < options.size(); i++) {
                                DropdownValuesList.add(options.get(i).getText());
                            }

                            String drp = "//ul[contains(@id,'" + ariaownsvalue + "')]/li[text()='" + firstindex + "']";
                            javascriptexe(getDriver().findElement(By.xpath(CheckXpathDuplicate(drp))));

                        }
                        catch (Exception e) {

                            String xpathF = "//li[text()='" + firstindex + "']";
                            xpathF = CheckXpathDuplicate(xpathF);
                            try {
                                getDriver().findElement(By.xpath(xpathF)).click();
                                System.out.println("Dropdown select using catch impelentation !!!!!");
                            } catch (Exception e1) {
                                Actions action23 = new Actions(getDriver());
                                action23.sendKeys(Keys.ARROW_DOWN).build().perform();
                                action23.sendKeys(Keys.ENTER).build().perform();
                            }

                        }
                    }
                    catch (NoSuchElementException e) {
                        scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                        WebElement ele4 = getDriver().findElement(By.xpath(xpath));
                        ele4.sendKeys(firstindex);
                        Thread.sleep(500);
                        ele4.sendKeys(Keys.ARROW_DOWN);
                        Thread.sleep(500);
                        ele4.sendKeys(Keys.ENTER);
                    }
                    String stepdesc = "<b>Test Step    : </b>" + step + "<br>" +
                            "<b>Object name  : </b>" + xpath + "<br>" +
                            "<b>Action method: </b>" + action + "<br>" +
                            "<b>Testdata used: </b>" + text + "<br>" +
                            "    <br>";
                    String stepdescdrop = "<b>Values of dropdown are : <b>";

                    if (TestRunConfig.ScreenShot_All) {
                        testR.PassCapture(test, MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(), stepdesc);
                        testR.PassMarkup(test, MarkupHelper.createOrderedList(DropdownValuesList), stepdescdrop);
                    } else
                        testR.PassMarkup(test, MarkupHelper.createOrderedList(DropdownValuesList), stepdesc + stepdescdrop);
                    Reportflag = true;
                }
                else if (text.contains("|dropdown")) {

                    scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                    WebElement textdropdown = getDriver().findElement(By.xpath(xpath));
                    textdropdown.clear();
                    String[] text1 = text.split("\\|");
                    String firstindex = text1[0];
                    textdropdown.sendKeys(firstindex);
                    textdropdown.sendKeys(Keys.BACK_SPACE);
                    try {
                        Thread.sleep(750);
                        textdropdown.sendKeys(Keys.ARROW_DOWN);
                        Thread.sleep(750);
                        textdropdown.sendKeys(Keys.ENTER);
                        Thread.sleep(750);
                        textdropdown.sendKeys(Keys.TAB);
                    } catch (Exception NoSuchElementException) {
                        textdropdown.sendKeys(Keys.BACK_SPACE);
                        WebDriverWait dropdown = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
                        dropdown.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//span[text()='Address']//..//..//input[@aria-expanded='true']|//span[contains(@class,'k-state-focused')]")));
                        Thread.sleep(750);
                        textdropdown.sendKeys(Keys.ARROW_DOWN);
                        Thread.sleep(750);
                        textdropdown.sendKeys(Keys.ENTER);
                        Thread.sleep(750);
                        textdropdown.sendKeys(Keys.TAB);
                    }
                }
                else if (text.contains("|multiselectcheckbox")) {
                    scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                    WebElement textdropdown = getDriver().findElement(By.xpath(xpath));
                    textdropdown.click();
                    String[] text1 = text.split("\\|");
                    String[] firstindex = text1[0].split(";");
                    for (String chck : firstindex) {
                        WebElement checkbox = getDriver().findElement(By.xpath(CheckXpathDuplicate("//input[contains(@name,'testMultiSelect')or contains(@name,'option_Id')]//..//label[text()='" + chck + "']/../input")));
                        useraction.moveByOffset(0, 0).perform();
                        checkbox.click();
                    }
                    textdropdown.click();
                }
                else {
                    try {
                        scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                        WebElement data = getDriver().findElement(By.xpath(xpath));
                        data.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                        useraction.sendKeys(Keys.DELETE);
                        useraction.sendKeys(Keys.ALT).build().perform();
                        data.sendKeys(text);
                        data.sendKeys(Keys.TAB);
                    } catch (Exception e) {
                        xpath = CheckXpathDuplicate(xpath);
                        waitFor(By.xpath(xpath));
                        WebElement data = getDriver().findElement(By.xpath(xpath));
                        JavascriptExecutor js = (JavascriptExecutor) getDriver();
                        js.executeScript("arguments[0].value='" + text + "';", data);
                        System.out.println("2nd try of enterdata success");
                    }
                }

            }
            case "Navigate_to_url" -> {
                getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(100));
                getDriver().get(text);
            }
            case "pointclick" -> {
                getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(500));
                useraction.sendKeys(Keys.ALT).build().perform();

                JavascriptExecutor jsExecutor1 = (JavascriptExecutor) getDriver();
                jsExecutor1.executeScript("return document.readyState").equals("complete");

                waitforloadbar(loadbar);
                text.toLowerCase();
                if (!text.isBlank()) {
                    if (text.contains("|switchlabel")) {
                        waitFor(By.xpath(xpath));
                        scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                        WebElement switchlabeltext = getDriver().findElement(By.xpath(xpath));
                        String[] text1 = text.split("\\|");
                        String firstindex = text1[0];
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                        if (firstindex.equals("Yes")) {
                            if (switchlabeltext.getText().equals("No")) {
                                switchlabeltext.click();
                                WebDriverWait waitforswitchlabel = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
                                waitforswitchlabel.until(ExpectedConditions.textToBePresentInElement(switchlabeltext, "Yes"));
                            }
                        } else {
                            if (switchlabeltext.getText().equals("Yes")) {
                                switchlabeltext.click();
                                WebDriverWait waitforswitchlabel = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
                                waitforswitchlabel.until(ExpectedConditions.textToBePresentInElement(switchlabeltext, "No"));
                            }
                        }
                    } else {
                        String textclick = xpath.replace("{}", text);
                        waitFor(By.xpath(textclick));
                        scrollToElemet(getDriver().findElement(By.xpath(textclick)));
                        JavascriptExecutor jsExecutor6 = (JavascriptExecutor) getDriver();
                        jsExecutor6.executeScript("return document.readyState").equals("complete");
                        WebElement object = getDriver().findElement(By.xpath(textclick));
                        object.click();
                    }
                } else {

                    Boolean success = false;
                    getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(500));
                    xpath = CheckXpathDuplicate(xpath);
                    waitFor(By.xpath(xpath));
                    scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                    if (!success) {
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                            javascriptexe(getDriver().findElement(By.xpath(xpath)));
                            waitforloadbar(loadbar);
                            switchtolatestwindow();
                            success = true;
                        } catch (Exception e) {
                            success = false;
                        }
                    }
                    if (!success) {
                        try {
                            Actions clckoper = new Actions(getDriver());
                            Action mouse1 = clckoper.moveToElement(getDriver().findElement(By.xpath(xpath))).click().build();
                            mouse1.perform();
                            switchtolatestwindow();
                        } catch (Exception e) {
                            success = true;
                        }
                    }

                }
                switchtolatestwindow();
            }
            case "wait" -> {
                int sec = Integer.parseInt(text);
                sec = sec * 1000;
                Thread.sleep(sec);
            }
            case "Form_Validations" -> {
                javascriptexe(getDriver().findElement(By.xpath(xpath)));
                Thread.sleep(1000);
                WebElement dropdownlist = getDriver().findElement(By.xpath("//span[contains(@class,'k-state-active')]/parent::span[contains(@aria-expanded,'true')]"));
                String aria_owns = dropdownlist.getAttribute("aria-owns");
                String[] aria_owns1 = aria_owns.split("\\_");
                String ariaownsvalue = aria_owns1[0];

                String xpathL = "//ul[contains(@id,'" + ariaownsvalue + "')]//li/label";
                //xpath = CheckXpathDuplicate(xpath);
                List<WebElement> values = getDriver().findElements(By.xpath(xpathL));
                List<String> ActualValues = new ArrayList<>();
                List<String> ExpectedValues = new ArrayList<>();

                for (int i = 0; i < values.size(); i++) {
                    if (!values.get(i).getText().isEmpty())
                        ActualValues.add(values.get(i).getText());
                }

                ExpectedValues = List.of(text.split("\\|\\|"));
                String stepdesc = "<b>Test Step    : </b>" + step + "<br>" +
                        "<b>Object name  : </b>" + xpath + "<br>" +
                        "<b>Action method: </b>" + action + "<br>" +
                        "<b>Testdata used: </b>" + text + "<br>" +
                        "    <br>";
                if (TestRunConfig.ScreenShot_All) {
                    testR.PassCapture(test, MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(), stepdesc);
                } else
                    testR.PassCapture(test, stepdesc);
                // Case 1: Get the entries from the expected list that are missing in the actual list
                List<String> missingEntries = findMissingEntries(ActualValues, ExpectedValues);


                testR.PassMarkup(test, MarkupHelper.createOrderedList(ActualValues), "<b> Application Form names: </b>");
                testR.PassMarkup(test, MarkupHelper.createOrderedList(ExpectedValues),"<b> Expected Form names: </b>");
                testR.PassMarkup(test, MarkupHelper.createOrderedList(missingEntries), "<b>Missed Form names<b><br>");

//                // Case 2: Get the entries from the actual list that are extra compared to the expected list
//                List<String> extraEntries = findExtraEntries(ActualValues, ExpectedValues);
//                testR.PassMarkup(test, MarkupHelper.createOrderedList(extraEntries), "<b>Extra Form names<b><br>");
                Reportflag = true;

            }
            case "getdata" -> {

                getDriver().findElement(By.xpath("//body")).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                waitFor(By.xpath(xpath));

                String objectdata = "";
                if (getDriver().findElement(By.xpath(xpath)).getAttribute("value") != null)
                    objectdata = getDriver().findElement(By.xpath(xpath)).getAttribute("value");

                else {
                    if (xpath.contains("k-dropdown")) {
                        xpath = xpath + "/span/span[1]";
                        System.out.println(xpath);
                    }
                    objectdata = getDriver().findElement(By.xpath(xpath)).getText();
                    System.out.println(objectdata);
                }
                if (text.equals("~QuoteID")) {
                    if (objectdata.length() >= 9) {
                        objectdata = objectdata.substring(0, 9);
                    }
                }
                String stepdesc = "<b>Test Step    : </b>" + step + "<br>" +
                        "<b>Object name  : </b>" + xpath + "<br>" +
                        "<b>Action method: </b>" + action + "<br>" +
                        "<b>Data From UI : </b>" + objectdata + "<br>" +
                        "    <br>";
                int premium = 0;
                if(text.contains("Premium"))
                premium = Integer.parseInt(objectdata.substring(1,objectdata.indexOf(".")).replaceAll(",",""));

                if(text.contains("Premium")&&objectdata.contains("$0.00")&&premium<=200)
                {
                    testR.FailCapture(test,MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(),stepdesc+"<b>0$ Premium error</b>");
                }
                else {
                    if (TestRunConfig.ScreenShot_All) {
                        testR.PassCapture(test, MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(), stepdesc);
                    } else
                        testR.PassCapture(test, stepdesc);
                }
                Reportflag = true;
                getobjectdatamap.put(text, objectdata);

            }
            case "validatedata" -> {

                waitFor(By.xpath(xpath));
                String actualdata = "";

                if (getDriver().findElement(By.xpath(xpath)).getAttribute("value") != null)
                    actualdata = getDriver().findElement(By.xpath(xpath)).getAttribute("value");
                else
                    actualdata = getDriver().findElement(By.xpath(xpath)).getText();

                String expecteddata = "";
                if (text.contains("~"))
                    expecteddata = replacetext(text, getobjectdatamap);
                else
                    expecteddata = text;

                Boolean result = false;

                if (expecteddata.contains("contains")) {

                    int lengthoftext = expecteddata.length();
                    expecteddata = expecteddata.substring(9, lengthoftext - 1);
                    result = actualdata.contains(expecteddata);
                } else
                    result = expecteddata.equals(actualdata);

                String validationresult = null;

                if (result) {
                    validationresult = "PASS" + "<br> Expected Data: " + expecteddata + "<br> Actual Data: " + actualdata;
                    testR.PassCapture(test, MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(), validationresult);
                } else {
                    validationresult = "FAIL" + "<br> Expected Data: " + expecteddata + "<br> Actual Data: " + actualdata;
                    testR.FailCapture(test, MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(), validationresult);
                }
                Reportflag = true;
            }
            case "RetrieveData" -> {
                getobjectdatamap.clear();
                String filepath = System.getProperty("user.dir") + "/src/main/resources/WorkBook/TestcaseDetails/".replace("/", "\\");
                File file = new File(filepath + text + ".txt");
                Scanner scan = new Scanner(file);
                while (scan.hasNextLine()) {
                    String[] keyValue = scan.nextLine().split(":");
                    getobjectdatamap.put(keyValue[0], keyValue[1]);
                }
            }
            case "js_click" -> {

                waitforloadbar(loadbar);
                // js.scrollToElemet(By.xpath(element));
                JavascriptExecutor exe = (JavascriptExecutor) getDriver();

                exe.executeScript("arguments[0].click();", getDriver().findElement(By.xpath(xpath)));
            }
            case "Alert"->{
                try {
                    Thread.sleep(2000);
                    getDriver().switchTo().alert().accept();
                    getDriver().switchTo().defaultContent();
                }
                catch(Exception e)
                {

                }
                break;
            }
            case "Action_click"-> {
                scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                Actions builder = new Actions(getDriver());
                Action mouse = builder.moveToElement(getDriver().findElement(By.xpath(xpath))).click().build();
                mouse.perform();
            }
            case "Default_Validation"->
            {
                boolean result = false;
                String Actual = null;
                scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                if(!xpath.contains("input"))
                {
                    xpath = xpath+"/../preceding-sibling::span";
                    Actual = getDriver().findElement(By.xpath(xpath)).getText();
                    if(Actual.equals(text))
                        result = true;
                }
                else
                {
                    xpath = xpath+"/following-sibling::input";
                    Actual = getDriver().findElement(By.xpath(xpath)).getAttribute("aria-valuenow");
                    if(Actual.equals(text))
                        result=true;
                }
                String validationresult;
                if (result) {
                    validationresult = "<b>Description: </b>"+step+"<br><b>PASS, Default value is displaying as Expected.</b>" + "<br><b> Expected Data: </b>" + text + "<br><b> Actual Data: </b>" + Actual;
                    testR.PassCapture(test, MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(), validationresult);
                }
                else {
                    validationresult = "<b>Description: </b>"+step+"<br><b>FAIL, Default value is not displaying as Expected.</b>" + "<br><b> Expected Data: </b>" + text + "<br><b> Actual Data: </b>" + Actual;
                    testR.FailCapture(test, MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(), validationresult);
                }
                Reportflag = true;
            }
            case "Dropdown_Select_Frist"->
            {
                xpath= CheckXpathDuplicate(xpath);
                scrollToElemet(getDriver().findElement(By.xpath(xpath)));
                WebElement textdropdown = getDriver().findElement(By.xpath(xpath));
                textdropdown.click();
                Actions actionDrp = new Actions(getDriver());
                actionDrp.sendKeys(Keys.ARROW_DOWN).build().perform();
                actionDrp.sendKeys(Keys.ENTER).build().perform();

            }


        }

        if(Reportflag==false) {

            String stepdesc = "<b>Test Step    : </b>" + step + "<br>" +
                    "<b>Object name  : </b>" + xpath + "<br>" +
                    "<b>Action method: </b>" + action + "<br>" +
                    "<b>Testdata used: </b>" + text + "<br>" +
                    "    <br>";
            if (TestRunConfig.ScreenShot_All)
                testR.PassCapture(test, MediaEntityBuilder.createScreenCaptureFromBase64String(capture(getDriver())).build(), stepdesc);
            else
                testR.PassCapture(test, stepdesc);
        }

        }

    public void waitforloadbar(String xpath) throws InterruptedException {
        List<WebElement> targetElement = getDriver().findElements(By.xpath(xpath));

        if (targetElement.size() >= 1) {
            try {
                WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(200));
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
            }
            catch(Exception e)
            {

            }

        }

    }

    public String CheckXpathDuplicate(String xpath)
    {
        List<WebElement> targetElement = getDriver().findElements(By.xpath(xpath));
        String Modifiedxpath=xpath;
        if(targetElement.size()>1)
        {
            System.out.println("Duplicate xpath found, searching for correct one..........");
            for(int i=1;i<=targetElement.size();i++)
            {
                String xp="("+xpath+")["+i+"]";
                if(getDriver().findElement(By.xpath(xp)).isDisplayed())
                {
                    Modifiedxpath=xp;
                    break;
                }
            }

        }
        return Modifiedxpath;

    }

    public String replacetext(String text, HashMap<String, String> getobjectdatamap) {

        for (Map.Entry<String, String> entry : getobjectdatamap.entrySet()) {
            String key   = entry.getKey();
            String value = entry.getValue();
            text         = text.replaceAll(Pattern.quote(key),value);
        }

        return text;
    }

    public String retrivetestdata(String variable){
        String testdata       = "";

        for(Map.Entry m:getobjectdatamap.entrySet()){
            if(m.getKey().toString().equals(variable))
                testdata     = m.getValue().toString();
        }

        return testdata;

    }

    public String capture(WebDriver driver) throws IOException {
        String base64Screenshot = captureScreenshotAsBase64(getDriver());
        return base64Screenshot;
    }

    public String captureScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshotBytes = ts.getScreenshotAs(OutputType.BYTES);
            return Base64.encodeBase64String(screenshotBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<String> findMissingEntries(List<String> actualList, List<String> expectedList) {
        List<String> missingEntries = new ArrayList<>(expectedList);
        missingEntries.removeAll(actualList);
        return missingEntries;
    }

    public List<String> findExtraEntries(List<String> actualList, List<String> expectedList) {
        List<String> extraEntries = new ArrayList<>(actualList);
        extraEntries.removeAll(expectedList);
        return extraEntries;
    }





}
