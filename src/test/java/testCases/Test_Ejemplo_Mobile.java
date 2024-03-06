package testCases;

import config.BaseMobile;
import page.GoogleMobile;

import org.testng.annotations.Test;
public class Test_Ejemplo_Mobile extends BaseMobile{
	
    @Test(groups = { "Ejemplo" },description = "https://tsoftlatam.qtestnet.com/p/96021/portal/project#tab=testdesign&object=1&id=49033722")
    public void validarPortalPaciente() {
    	GoogleMobile google = new GoogleMobile();
        google.buscar();
     	
    }   
}
