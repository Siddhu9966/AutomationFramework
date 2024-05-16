package runners;

import PageObjects.Base_PO;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import driver.DriverFactory;
import org.testng.Assert;
import org.xml.sax.SAXException;
import utils.ExtentTestManager;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SharedStep extends MainRunner {
    public static com.codoid.products.fillo.Connection connectionShared_stepRetrieve = null;

    public Recordset RetrieveSharedSteps() throws FilloException {
        Recordset resultSet = null;
        try {
            Fillo fillo = new Fillo();

            System.out.println(ExcelPath);

            connectionShared_stepRetrieve = fillo.getConnection(ExcelPath);

            ArrayList<String> sheetNames = connectionShared_stepRetrieve.getMetaData().getTableNames();
            String sheetName = "Coverages";
            String query = String.format("SELECT * FROM %s WHERE Sharedstep LIKE 'SharedStep_%%'", sheetName);
            try {
                resultSet = connectionShared_stepRetrieve.executeQuery(query);
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

        return resultSet;
    }

    public void SharedStepExecution(ExtentTest test, String txt, Recordset SharedList, String Sstepvariable, ExtentTestManager exTest, String testcase, Base_PO base, HashMap<String, String> objectRepository, String state) throws FilloException, XPathExpressionException, IOException, ParserConfigurationException, InterruptedException, TransformerException, SAXException {
        int flagoftestdata = 0;
        String[] SharedStepTestdata = new String[0];
        String step, obj, Action, xpath = null;


        if (txt.contains(";")) {

            flagoftestdata = 1;
            SharedStepTestdata = txt.split(";");

        }
        int Failcounter = 0;

        while (SharedList.next()) {

            if (SharedList.getField("Sharedstep").contains(Sstepvariable)) {
                if (SharedList.getField("custom").equals(state) || SharedList.getField("custom").contains(state) || SharedList.getField("custom").equals("")) {
                    step = SharedList.getField("Test_Step");
                    obj = SharedList.getField("Object_name");
                    Action = SharedList.getField("Action");
                    txt = SharedList.getField("Test_data");

                    if (flagoftestdata == 1 && !txt.equals("NA") && !txt.equals(" ")) {

                        for (int l = 0; l < SharedStepTestdata.length; l++) {
                            String[] compareobj = SharedStepTestdata[l].split(":");
                            if (compareobj[0].equalsIgnoreCase(obj)) {
                                txt = compareobj[1];
                                break;
                            }
                        }
                    }

                    try {

                        if (!txt.equalsIgnoreCase("skip")) {
                            xpath = objectRepository.get(obj);
                            System.out.println(testcase + " : " + Sstepvariable + " -> " + step);
                            try {
                                base.actions(step, Action, xpath, txt, test, exTest);
                            } catch (Exception e) {
                                base.actions(step, Action, xpath, txt, test, exTest);
                            }
                        } else {
                            exTest.Skip(test, step);
                        }
                    } catch (Exception e) {

                        String excep = e.getMessage();
                        String descF = "<b>Test Step    : </b>" + Sstepvariable + " -> " + step + "<br>" +
                                "<b>Object name  : </b>" + xpath + "<br>" +
                                "<b>Action method: </b>" + Action + "<br>" +
                                "<b>Testdata used: </b>" + txt + "<br>" +
                                "<b>SharedStep Variable used : </b>" + Sstepvariable + "<br>" +
                                "    <br>" +
                                "<b>This Step was ignored(Not found)<b> <br>";


                        if(txt.contains("multiselectcheckbox"))
                            exTest.FailCapture(test, descF);
                        else
                        exTest.Info(test, descF);

                        Failcounter++;

                        if (Failcounter > 10) {
                            exTest.FailCapture(test, descF);
                            DriverFactory.cleanupDriver();
                            Assert.fail();

                        }
                        //exTest.FailException(test, new Exception(e));
//                        DriverFactory.cleanupDriver();
//                        Assert.fail();

                    }


                }
            }
        }


            SharedList.moveFirst();
            System.out.println(SharedList.getField("Test_Step"));
        }
    }




