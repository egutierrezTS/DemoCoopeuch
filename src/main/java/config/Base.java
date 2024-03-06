package config;

import config.driver.DriverContext;
import org.testng.annotations.*;
import static config.Constants.URL_QA;
import static config.reportepdf.ImedReports.*;

@Deprecated
public abstract class Base {

    @BeforeClass
    @Parameters({"browser"})
    public void setUp(@Optional("Chrome") String browser){
        creaPDF( Constants.pathPdf );
        DriverContext.setUp(browser, URL_QA);
        nombreClase(this.getClass().getSimpleName());
    }

    @AfterClass
    public void tearDown()
    {
        pdfClose();
        DriverContext.quitDriver();
    }
}
