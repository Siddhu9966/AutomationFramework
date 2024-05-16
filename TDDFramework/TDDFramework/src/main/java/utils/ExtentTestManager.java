package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.model.Media;
import org.apache.commons.codec.binary.Base64;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * extentTestMap holds the information of thread ids and ExtentTest instances.
 * ExtentReports instance created by calling createExtentReports() method from ExtentManager.
 * At startTest() method, an instance of ExtentTest created and put into extentTestMap with current thread id.
 * At getTest() method, return ExtentTest instance in extentTestMap by using current thread id.
 */
public class ExtentTestManager {
    int PASS=0,FAIL=0,Skip=0,WARNING=0,Info=0;

    public void setstatus_variables(){
        PASS=0;
        FAIL=0;
        Skip=0;
        WARNING=0;
    }

    public int getPASSCount(){return PASS;}
    public int getFAILCount(){return FAIL;}
    public int getSkipCount(){return Skip;}
    public int getWARNINGCount(){return WARNING;}

    public int getInfocount(){return Info;}
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
   // static ExtentReports            extent        = ExtentManager.createExtentReports();


    public void Pass(ExtentTest test,String desc)
    {
        test.log(Status.PASS,desc);
        PASS++;
    }

    public void PassCapture(ExtentTest test, Media M,String desc)
    {
        test.log(Status.PASS,desc,M);
        PASS++;
    }

    public void PassCapture(ExtentTest test,String desc)
    {
        test.log(Status.PASS,desc);
        PASS++;
    }

    public void PassMarkup(ExtentTest test, Markup M,String S)
    {
        test.log(Status.PASS,S+"<b>Details/Validated values: <b><br>"+M.getMarkup());
    }

    public void Fail(ExtentTest test,String desc)
    {
        test.log(Status.FAIL,desc);
        FAIL++;
    }

    public void FailCapture(ExtentTest test, Media M, String desc)
    {

        test.log(Status.FAIL,desc,M);
        FAIL++;
    }

    public void FailCapture(ExtentTest test, String desc)
    {

        test.log(Status.FAIL,desc);
        FAIL++;
    }

    public void FailException(ExtentTest test, Exception e)
    {
        test.log(Status.FAIL,e);
    }


    public void Skip(ExtentTest test,String desc)
    {
//        test.log(Status.SKIP,desc);
        Skip++;
    }

    public void Warn(ExtentTest test, String desc)
    {
        test.log(Status.WARNING,desc);
        WARNING++;
    }

    public void createchart(ExtentTest test, int Pass,int notexecuted,int skip,int warning,int fail) throws IOException {

        PieChart chart = new PieChartBuilder().width(1899).height(854).title("Test Steps Analysis - Pie Chart").theme(Styler.ChartTheme.GGPlot2).build();
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAnnotationType(PieStyler.AnnotationType.LabelAndValue);
        chart.getStyler().setAnnotationDistance(1.15);
        chart.getStyler().setPlotContentSize(.7);
        chart.getStyler().setStartAngleInDegrees(90);
        chart.getStyler().setAnnotationsFontColor(Color.BLACK);
        chart.getStyler().setBorderWidth(2.5);
        chart.getStyler().setSeriesColors(new Color[]{Color.GREEN,Color.GRAY,Color.YELLOW,Color.ORANGE,Color.RED});



        chart.addSeries("Pass", Pass);
        chart.addSeries("Not Executed", notexecuted);
        chart.addSeries("Skip",skip);
        chart.addSeries("Warning",warning);
        chart.addSeries("Fail",fail);

        // BitmapEncoder.saveBitmap(chart, Path, BitmapEncoder.BitmapFormat.PNG);
        byte[] screenshotBytes=  BitmapEncoder.getBitmapBytes(chart,BitmapEncoder.BitmapFormat.BMP);


        test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(Base64.encodeBase64String(screenshotBytes)).build());
    }


    public void Info(ExtentTest test, String desc)
    {
        test.log(Status.INFO,desc);
        Info++;
    }



}