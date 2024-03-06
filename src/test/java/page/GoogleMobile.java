package page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import static config.driver.DriverContext.getDriver;
import static config.reporteWeb.HtmlReportMobile.reporteObjetoDesplegado;
import static utils.Web.esperaElementoImplicita;
import static utils.Web.esperarElemento;

public class GoogleMobile {

    private WebDriver driver;

    public GoogleMobile() {
        this.driver = getDriver ( );
        PageFactory.initElements (this.driver, this);
    }

    @FindBy(xpath = "//*[@name=\"q\"]")
    private WebElement inputBusqueda;
    @FindBy(xpath = "//*[@name='btnK']")
    private WebElement btnBuscar;

    public void buscar(){
        boolean elemento = esperarElemento(inputBusqueda);
        if(elemento){
            inputBusqueda.sendKeys( "hola" );
            reporteObjetoDesplegado(true, "input busqueda", "Home", false);
        }else {
            reporteObjetoDesplegado(false, "Error, no se encuentra elemento input busqueda", "Home", false);
        }
        /*
        boolean btn = esperarElemento(btnBuscar);
        if(btn){
            btnBuscar.click();
            reporteObjetoDesplegado(true, "btn Buscar", "Home", false);
        }else {
            reporteObjetoDesplegado(false, "Error, no se encuentra elemento btn Buscar", "Home", false);
        }
*/
    }

    public void buscarConParametro(String dato){
        boolean elemento = esperaElementoImplicita(inputBusqueda);
        if(elemento){
            inputBusqueda.sendKeys( dato + Keys.ENTER );
            reporteObjetoDesplegado(true, "input busqueda", "Home", false);
        }else {
            reporteObjetoDesplegado(false, "Error, no se encuentra elemento input busqueda", "Home", false);
        }

    }

}
