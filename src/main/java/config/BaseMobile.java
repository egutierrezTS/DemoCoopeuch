package config;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Parameters;
import com.google.common.base.Throwables;
import config.driver.DriverContext;
import config.reporteWeb.HtmlReportMobile;

import java.lang.reflect.Method;



public class BaseMobile{
	
    public static HtmlReportMobile reportes;
    public WebDriver driver;
	ITestAnnotation annotation;
	Method method;
	String rutPersona;
	

    @BeforeMethod
    @Parameters({"platformName","deviceName","platformVersion","url","navegador","username","key"})
    public void setUp(final ITestContext testContext,Method method, String platformName,String deviceName,String platformVersion,String url,String navegador,String username,String key) {
       String[] obtenerGrupo =  method.getAnnotation(org.testng.annotations.Test.class).groups();
       String grupo = obtenerGrupo[0];
       String testName = testContext.getCurrentXmlTest().getName();
       DriverContext.setUpMobile(platformName,deviceName,grupo,platformVersion, url,testName,navegador,username,key);
       this.method = method;
       String obtenerDescpr =  method.getAnnotation(org.testng.annotations.Test.class).description();
       HtmlReportMobile.newTest(testName,grupo,obtenerDescpr,platformName);
       HtmlReportMobile.updateDriver(DriverContext.getDriver());  
    }
    

	@AfterMethod
	public void tearDown(ITestResult result, ITestContext testContext) {
		JavascriptExecutor executor = (JavascriptExecutor)DriverContext.getDriver();
		  if (!result.isSuccess()){
	            String stackTrace = Throwables.getStackTraceAsString(result.getThrowable());
	        
	         if (stackTrace.contains("org.testng.Assert.fail")){
	            	executor.executeScript("lambda-status=failed");

	         }else {
	        	 HtmlReportMobile.addWarning("El test fallo inesperadamente.");
	        	 HtmlReportMobile.addWarning("Error info: "+ stackTrace);
	             executor.executeScript("lambda-status=failed");
	         } 
	            HtmlReportMobile.addInfo("Test finalizado con errores!");
		  }else {
			  executor.executeScript("lambda-status=passed");
			  HtmlReportMobile.addInfo("Test finalizado correctamente!");
		  } 
	
		  DriverContext.quitDriver();
	}
    
    @BeforeSuite
    public void beforeSuite(final ITestContext testContext){
        String suiteName = testContext.getCurrentXmlTest().getSuite().getName();
        String platformName = testContext.getCurrentXmlTest().getParameter("platformName");
        String deviceName = testContext.getCurrentXmlTest().getParameter("deviceName");
        String platformVersion = testContext.getCurrentXmlTest().getParameter("platformVersion");
        String navegador = testContext.getCurrentXmlTest().getParameter("navegador");
        reportes = new HtmlReportMobile(suiteName,platformName,deviceName,platformVersion,navegador);
        
        
    }
    @AfterSuite
    @Parameters({"carpetaReporte"})
    public void afterSuite(ITestContext testContext, String carpetaReporte) {
    	HtmlReportMobile.endReport();
    	ZipUtilisMobile zip = new ZipUtilisMobile(carpetaReporte);
        zip.generarReporte();
    }

}