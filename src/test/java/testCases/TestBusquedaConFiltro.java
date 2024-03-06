package testCases;

import config.BaseWeb;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import page.Google;

public class TestBusquedaConFiltro extends BaseWeb {
    @Test(groups = {"google"}, description = "https://imed.atlassian.net/browse/QA-3994")
    @Parameters({"datoBusqueda"})
    public void busquedaFiltro(String datoBusqueda){
        Google google = new Google();
        google.buscarConParametro(datoBusqueda);
    }
}
