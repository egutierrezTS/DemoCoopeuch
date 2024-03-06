package testCases;

import config.BaseWeb;
import org.testng.annotations.Test;
import page.Google;

public class TestBusqueda extends BaseWeb {

    @Test(groups = {"TestWeb", "BusquedaEnGoogle"}, description = "test de busqueda")
    public void busqueda(){
        Google google = new Google();
        google.buscar();
    }

}
