package page;

import config.driver.DriverContext;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static config.reporteWeb.HtmlReport.reporteObjetoDesplegado;
import static utils.Web.esperaElementoImplicita;
import static utils.Web.esperarElemento;

public class Google {

    private WebDriver driver;

    public Google() {
        this.driver = DriverContext.getDriver();
        PageFactory.initElements (this.driver, this);
    }

    @FindBy(xpath = "//*[@name=\"q\"]")
    private WebElement inputBusqueda;
    @FindBy(xpath = "//*[@name='btnK']")
    private WebElement btnBuscar;

    public void buscar(){
        boolean elemento = esperarElemento(inputBusqueda);
        if(elemento){
            inputBusqueda.sendKeys( "Buscando algo..." + Keys.ENTER);
            reporteObjetoDesplegado(true, "Barra de busqueda", "Home", false);
        }else {
            reporteObjetoDesplegado(false, "Error, no se encuentra elemento input busqueda", "Home", false);
        }

        /*boolean btn = esperarElemento(btnBuscar);
        if(btn){
            btnBuscar.click();
            reporteObjetoDesplegado(true, "Boton Buscar", "Home", false);
        }else {
            reporteObjetoDesplegado(false, "Error, no se encuentra el boton 'Buscar'", "Home", false);
        }*/
    }

    public void buscarConParametro(String dato){
        boolean elemento = esperaElementoImplicita(inputBusqueda);
        if(elemento){
            inputBusqueda.sendKeys( dato + Keys.ENTER );
            reporteObjetoDesplegado(true, "Barra de busqueda", "Home", false);
        }else {
            reporteObjetoDesplegado(false, "Error, no se encuentra elemento input busqueda", "Home", false);
        }

    }

}
