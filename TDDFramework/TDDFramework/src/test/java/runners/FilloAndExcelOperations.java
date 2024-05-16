package runners;

import PageObjects.Base_PO;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.xml.sax.SAXException;
import utils.ExtentTestManager;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilloAndExcelOperations extends MainRunner {


    public HashMap<String,String> getxpath() throws FilloException {

         connection = fillo.getConnection(ExcelPath);
        String query = "SELECT * FROM Object_Repository"; // Adjust the sheet name accordingly

        HashMap<String, String> objectRepository = new HashMap<>();
        Recordset recordset = connection.executeQuery(query);
        while (recordset.next()) {
            String objectName = recordset.getField("Object_name");
            String xpath = recordset.getField("Locator");
            objectRepository.put(objectName, xpath);
        }
        recordset.close();
        return objectRepository;
    }







    }
