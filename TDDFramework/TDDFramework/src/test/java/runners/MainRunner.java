package runners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import driver.DriverFactory;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import PageObjects.Base_PO;
import org.xml.sax.SAXException;
import tech.grasshopper.pdf.extent.ExtentPDFReportDataGenerator;
import utils.ExtentTestManager;
import utils.RetryAnalyzer;
import utils.TestRunConfig;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import static com.aventstack.extentreports.Status.INFO;

public class MainRunner {

    TestRunConfig testconfig = new TestRunConfig();
    String ExcelPath      = System.getProperty("user.dir")+ "/src/main/resources/WorkBook/"+TestRunConfig.ExcelFile;  //***
    String Screenshot     = "Yes";
    Fillo fillo           = new Fillo();
    Connection connection = null;
    private static String THREAD_COUNT_VALUE = TestRunConfig.THREAD_COUNT;
    private static final String THREAD_COUNT_KEY   = "dataproviderthreadcount";
    ExtentSparkReporter spark;
    ExtentReports extent;
    String ReportPath;
    public  ThreadLocal <ExtentTest> test1       = new ThreadLocal < ExtentTest > ();
    HashMap<String,String> ObjectRepository = new HashMap<>();

    String ReportName = " ";


    @BeforeClass(alwaysRun = true)
    public void setUpClass() throws FilloException {
        FilloAndExcelOperations FEoperations = new FilloAndExcelOperations();
        SetParallelExecution();
        ObjectRepository = FEoperations.getxpath();
//               try {
//            if (!System.getProperty("os.name").toLowerCase().contains("linux"))
//                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
//        } catch (IOException e) {
//            //log.info(e.getMessage());
//        }

        Random rand = new Random();
        String date_time                               = ReportName+"-"+LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))+ "-"+rand.nextInt(1000);
        String PathR                                   = System.getProperty("user.dir") + "/Reports/" + date_time + "_Report/";
        ReportPath                                     = PathR;
        spark                            = new ExtentSparkReporter(PathR + "testReport_spark.html");
        extent                           = new ExtentReports();
        extent.attachReporter(spark);
        ReportConfig();

    }


    @DataProvider(name = "TestCases",parallel = true)
    public Object[][] excelData() throws FilloException {

        Recordset YesTestcasesSet = GetTestcasesYes();
        Object [][] TestlabCases = CreateObjectArrayForRecordset(YesTestcasesSet);
        return TestlabCases;
    }
    @Test(dataProvider = "TestCases",retryAnalyzer = RetryAnalyzer.class)

    public void runScenario(String TestCaseName,String state, String Dataset,String Project, String description, String tag) throws FilloException, IOException, InterruptedException, XPathExpressionException, ParserConfigurationException, TransformerException, SAXException {

        ExtentTest extentnode          = null;
        ExtentTest chartnode           = null;
        Base_PO base; FilloAndExcelOperations FEoperations = new FilloAndExcelOperations();
        String RunType,Action = null,step = null,object,xpath = null,text = null,Sstepvariable;
        ExtentTestManager ExTest       = new ExtentTestManager();


        connection                = fillo.getConnection(ExcelPath);
        String strQuery          = "Select * from " + TestCaseName;
        Recordset Testcaserecordset      = connection.executeQuery(strQuery);

        int NotExecuted           = Testcaserecordset.getCount();
        SharedStep sharedStep     = new SharedStep();
        Recordset sharedsteplocal = sharedStep.RetrieveSharedSteps();

        test1.set(extent.createTest(TestCaseName+state, description));
        test1.get().assignCategory(tag);
        chartnode = test1.get().createNode("Test Step Analysis - Pie Chart");

        ExTest.setstatus_variables();
        base            = new Base_PO();

        String currentPageName = "";

      try{
        while(Testcaserecordset.next()) {
            NotExecuted--;

            String PageName = Testcaserecordset.getField("PageName");
            if (!currentPageName.equals(PageName)) {
                currentPageName = PageName;
                extentnode = test1.get().createNode(PageName);
            }

            if (!Testcaserecordset.getField("RunType").equalsIgnoreCase("skip")) {

                if (Testcaserecordset.getField("custom").equals(state)||Testcaserecordset.getField("custom").contains(state)||Testcaserecordset.getField("custom").equals("")) {


                    step = Testcaserecordset.getField("Test_Step");
                    object = Testcaserecordset.getField("Object_name");
                    Action = Testcaserecordset.getField("Action");
                    text = Testcaserecordset.getField(Dataset);

                    if (text.contains("~") && !Action.contains("SharedStep") && !Action.contains("getdata") && !Action.contains("validatedata") && !Action.contains("PdfValidations")) {
                        text = base.retrivetestdata(text);
                    }

                    Sstepvariable = Action;

                    if (object.contains("sharedstep")) {
                        sharedStep.SharedStepExecution(extentnode, text, sharedsteplocal, Sstepvariable, ExTest, TestCaseName+state,base,ObjectRepository,state);
                    }
                    else {
                        xpath = ObjectRepository.get(object);

                        try {
                            System.out.println(TestCaseName + state + " : " + Testcaserecordset.getField("Test_Step"));
                            base.actions(step, Action, xpath, text, extentnode,ExTest);
                        }
                        catch (Exception e) {
                            System.out.println(TestCaseName+state + ": " + step + " : Retrying");
                            base.actions(step, Action, xpath, text, extentnode,ExTest);
                        }
                    }


                }

            } else {
                ExTest.Skip(extentnode, Testcaserecordset.getField("Test_Step"));
            }

        }
          DriverFactory.cleanupDriver();
          Testcaserecordset.close();
        }
      catch(Exception e){

          try {
              String excep = e.getMessage();
              String descF = "<b>Test Step    : </b>" + step + "<br>" +
                      "<b>Object name  : </b>" + xpath + "<br>" +
                      "<b>Action method: </b>" + Action + "<br>" +
                      "<b>Testdata used: </b>" + text+"<br>"+
                      "    <br>"+
                      "<b>Screenshot captured:<b> <br>";

              ExTest.FailCapture(extentnode, MediaEntityBuilder.createScreenCaptureFromBase64String(base.capture(base.getDriver())).build(),descF);
              ExTest.FailException(extentnode, new Exception(e));

              Markup M = MarkupHelper.createLabel("Remaining steps are to be executed: " + NotExecuted, ExtentColor.YELLOW);
              extentnode.log(INFO, M);

              System.out.println(excep);


              ExTest.createchart(chartnode, ExTest.getPASSCount() - 1, NotExecuted, ExTest.getSkipCount(), ExTest.getWARNINGCount(), ExTest.getFAILCount());

              if (!base.sendObjectdatamap().isEmpty()) {
                  ExTest.PassMarkup(extentnode, MarkupHelper.createOrderedList(base.sendObjectdatamap()),"");
                  base.createdetailsfile(TestCaseName,base.sendObjectdatamap());
              }


          }
          catch (IOException ex) {
              throw new RuntimeException(ex);
          }

          DriverFactory.cleanupDriver();
          Assert.fail();


      }

        ExTest.createchart(chartnode, ExTest.getPASSCount(), NotExecuted, ExTest.getSkipCount(), ExTest.getWARNINGCount(), ExTest.getFAILCount());
        if (!base.sendObjectdatamap().isEmpty()) {
            ExTest.PassMarkup(extentnode, MarkupHelper.createOrderedList(base.sendObjectdatamap()),"");
            base.createdetailsfile(TestCaseName+state,base.sendObjectdatamap());
        }




    }


    @AfterClass(alwaysRun = true)
    public void testCompleted(){
        extent.flush();
        connection.close();
    }

    @AfterSuite
    public void openReport() throws IOException {
        Desktop.getDesktop().open(new File(ReportPath.replace("/","\\")+"testReport_spark.html"));
    }


    public Recordset GetTestcasesYes() throws FilloException {

        connection                  = fillo.getConnection(ExcelPath);
        String testlabquery         = "Select * from Test_Lab where Execution_Flag='Yes'";
        Recordset recordsettestcase = connection.executeQuery(testlabquery);

        return recordsettestcase;

    }

    public void SetParallelExecution() throws FilloException {
        Recordset recordsetOfYesCases = GetTestcasesYes();
        while(recordsetOfYesCases.next())
        {
            if(recordsetOfYesCases.getField("ParallelExecution").toUpperCase().equals("YES"))
                THREAD_COUNT_VALUE=TestRunConfig.THREAD_COUNT;
            else
                THREAD_COUNT_VALUE = "1";
            ReportName = recordsetOfYesCases.getField("Report_Name");
            break;
        }
        System.setProperty(THREAD_COUNT_KEY, THREAD_COUNT_VALUE);

    }

    public void ReportConfig() {

        spark.config().setDocumentTitle("Test-Report");
        spark.config().setReportName("AUTOMATION REPORT");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        spark.viewConfigurer().viewOrder().as(new ViewName[]{ViewName.DASHBOARD,ViewName.TEST,ViewName.CATEGORY,ViewName.EXCEPTION});

    }



    public Object[][] CreateObjectArrayForRecordset(Recordset YesRecordsetValues) throws FilloException {

        int i  = 0;
        Object[][] testlabcases = new Object[YesRecordsetValues.getCount()][6];

        while (YesRecordsetValues.next()) {
            int j=0;
            testlabcases[i][j++] = YesRecordsetValues.getField("Test_case");
            testlabcases[i][j++] = YesRecordsetValues.getField("state specific");
            testlabcases[i][j++] = YesRecordsetValues.getField("Dataset");
            testlabcases[i][j++] = YesRecordsetValues.getField("Project");
            testlabcases[i][j++] = YesRecordsetValues.getField("Description");
            testlabcases[i][j++] = YesRecordsetValues.getField("Tag");
            i++;

        }

        return testlabcases;

    }


    public int  DatasetCount(Recordset YesRecordset) throws FilloException {
        int count =0;
        while(YesRecordset.next())
        {
            String [] DatasetArray = YesRecordset.getField("Dataset").split(";");
            count = count+DatasetArray.length;
        }
        return count;
    }


    


}
