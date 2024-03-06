package config.reporteWeb;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.observer.ExtentObserver;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import config.Constants;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static config.Constants.RUTA_CONFIG_HTML_REPORT;

public class HtmlReportMobile {
    private static ExtentReports reports;
    private static ExtentTest test;
    public static ExtentSparkReporter spark;
    private static WebDriver driver;
    private static final DateFormat dateFormat = new SimpleDateFormat("hhmmSSS");
    private static int paso;
    private static String suite;
    private static ArrayList<String> errorMessages;
    //private static String URL_REPORT = "src\\test\\resources\\reports\\";
    private static String URL_REPORT = "HTMLREPORTS\\";
    private ITestContext testContext;
    
    public HtmlReportMobile(String suiteName,String plataforma,String modelo,String versionSO,String navegador) {
    
        reports = new ExtentReports();
        spark = new ExtentSparkReporter(URL_REPORT + suiteName + "\\ReporteEjecucion.html").viewConfigurer().viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.CATEGORY,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                })
                .apply();

        reports.attachReporter(new ExtentObserver[]{spark});
        reports.setSystemInfo("OS",System.getProperty("os.name").toLowerCase());
       	reports.setSystemInfo("OS Mobile",plataforma);
        reports.setSystemInfo("OS Version ",versionSO);
        reports.setSystemInfo("Modelo",modelo);
        reports.setSystemInfo("Navegador",navegador);
        try {
            File json = new File("htmlConfig.json");
            spark.loadJSONConfig(json);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        suite = suiteName;
        paso = 1;
        errorMessages = new ArrayList();

        File screenShotFolder = new File(URL_REPORT + suite + "\\htmlScreenshots");
        if(screenShotFolder.exists()){
            try {
                FileUtils.forceDelete(screenShotFolder);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ocurrió un error al borrar los screenshots anteriores");
            }
        }
        else {
            screenShotFolder.mkdir();
        }
    }

    public static void setReportPath(String path) {
        URL_REPORT = path;
    }

    public static void newTest(String testName,String tag,String url,String plataforma) {
        test = reports.createTest(testName).assignCategory(tag);
        test.log(Status.INFO,"Ir a Qtest : <a href='"+url+"' target=\"_blank\">"+testName+"</a>");
        test.assignDevice(plataforma);
        paso = 1;
    }
    public static String getSuite(){
        return suite;
    }
    public static String getReportPath(){
        return System.getProperty("user.dir")+ "\\"+URL_REPORT;
    }

    public static void updateDriver(WebDriver driv) {
        driver = driv;
    }

    private static String captureScreenShot(WebDriver driver) {
        String imageName = System.currentTimeMillis() + ".png";
        File screenshotFile = (File)((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File targetFile = new File(URL_REPORT + suite + "\\htmlScreenshots", imageName);

        try {
            FileUtils.copyFile(screenshotFile, targetFile);
        } catch (IOException var5) {
            test.log(Status.INFO, "No se pudo obtener el screenshot");
        }

        System.out.println("Screenshot: " + targetFile.getName());
        return targetFile.getName();
    }

    public static void addStep(Status status, String nombrePaso, String descripcion, Boolean screenshot) {
        test.log(Status.INFO, "PASO " + paso + ": " + nombrePaso);
        if (screenshot) {
            String ss = captureScreenShot(driver);
            test.log(status, descripcion, MediaEntityBuilder.createScreenCaptureFromPath("htmlScreenshots\\" + ss).build());
        } else {
            test.log(status, descripcion);
        }

        if (status.toString().equals("Fail")) {
            addErrorMessagesToReport();
        }

        ++paso;
    }

    public static void addInfo(String info) {
        String condition = "Build info";
        if (info.contains(condition)) {
            String[] info1 = info.split(condition);
            test.log(Status.INFO, info1[0]);
        } else {
            test.log(Status.INFO, info);
        }

    }
    public static void addInfoAssertion(String info) {
        String condition = "Build info";
        if (info.contains(condition)) {
            String[] info1 = info.split(condition);
            test.log(Status.INFO, info1[0]);
        } else {
            test.log(Status.INFO, MarkupHelper.createCodeBlock(info));
        }

    }
    public static void addWarning(String info) {
        String condition = "Error info:";
        if (info.contains(condition)) {
            test.log(Status.WARNING, MarkupHelper.createCodeBlock(info));
        } else {
            test.log(Status.WARNING, info);
        }
    }


    public static void addErrorMessage(String message) {
        errorMessages.add(message);
    }

    public static void addErrorMessagesToReport() {
        if (!errorMessages.isEmpty()) {
            Iterator var0 = errorMessages.iterator();

            while(var0.hasNext()) {
                String message = (String)var0.next();
                addInfo(message);
            }

            errorMessages.clear();
        }

    }

    public static void addSystemInfo(String info) {
        String[] infos = info.split(",");
        test.assignCategory(infos);
    }

    public static void endReport() {
        reports.flush();
    }

    public static void fail(Throwable t){
        test.fail(t);
    }
    public static void fail(String t){
        test.fail(t);
    }

    public static void reporteObjetoDesplegado(boolean estadoObjeto, String objeto, String vista, boolean fatal) {
        if (estadoObjeto) {
            addStep(Status.PASS, vista, "Elemento encontrado: " + objeto, true);
        } else {
            addStep(Status.FAIL, vista, "Elemento no encontrado: " + objeto, true);
            if (fatal) {
                Assert.fail("[ Elemento no encontrado: " + objeto + " ]");
            }
        }

    }
    public static void addWebReportImage(String nombre, String descripcion, Status status, boolean fatal){
        if (fatal){
            addStep(status,nombre,descripcion,true);
            Assert.fail();
        }
        else {
            addStep(status,nombre,descripcion,true);
        }
    }
    public static void addWebReport(String nombre, String descripcion, Status status, boolean fatal){
        if (fatal){
            addStep(status,nombre,descripcion,false);
            Assert.fail();
        }
        else {
            addStep(status,nombre,descripcion,false);
        }
    }
    public static void addWebReport(String nombre, Markup descripcion, Status status, boolean fatal){
        test.log(Status.INFO, "PASO " + paso + ": " + nombre);
        if (fatal){
            test.log(status, descripcion);
            Assert.fail();
        }
        else {
            test.log(status, descripcion);
        }
        ++paso;
    }
    public static void addWebReport(String nombre, List<String> descripcion, Status status, boolean fatal){
        test.log(Status.INFO, "PASO " + paso + ": " + nombre);
        if (fatal){
            test.log(status, MarkupHelper.createUnorderedList(descripcion));
            Assert.fail();
        }
        else {
            test.log(status, MarkupHelper.createUnorderedList(descripcion));
        }
        ++paso;
    }
    public static String[][] formatTable(List<String> resultado){
        List<String[]> arra = new ArrayList<>();
        int cont = 0;
        for (String fila: resultado){
            if (cont >= 10) break;
            arra.add(fila.split(","));
            cont++;
        }
        return arra.toArray(new String[0][]);
    }
    public static Markup insertarTabla(List<String> resultado){
        return MarkupHelper.createTable(formatTable(resultado),"table-sm");
    }
    public static Markup insertarBloqueJSON(JSONObject obj){
        return MarkupHelper.createJsonCodeBlock(obj);
    }
    public static Markup insertarConsulta(String consulta){
        return MarkupHelper.createCodeBlock(consulta);
    }


}