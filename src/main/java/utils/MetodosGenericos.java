package utils;

import java.awt.*;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import config.driver.DriverContext;

import static config.reporteWeb.HtmlReport.reporteObjetoDesplegado;
import static utils.Robot.esperar;

public class MetodosGenericos {

	

	
	
    public static String tipoAmbiente() {
        if ("QA".equals("QA")) {
            return "Certificación";
        } else {
            return "QA".equals("INT") ? "Integración" : "Desarrollo";
        }
    }

    public static void tamanoPantalla()  {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int altura = screenSize.height;
        int ancho = screenSize.width;

        if(altura == 768 && ancho == 1366){
            clickPantalla14();
        }
        if(altura == 1080 && ancho == 1920){
            clickPantalla15();
        }

    }

    public static void clickPantalla14() {

        Robot bot = null;
        try {
            bot = new Robot();
            bot.mouseMove(906,702);
            esperar( 4 );
            bot.mousePress( InputEvent.BUTTON1_MASK);
            bot.mouseRelease( InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    public static void clickPantalla15() {

        Robot bot = null;
        try {
            bot = new Robot();
            bot.mouseMove(1180,1015);
            esperar( 4 );
            bot.mousePress( InputEvent.BUTTON1_MASK);
            bot.mouseRelease( InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        }  
    }
    
	public static List<LogEntry> retornaTrazaNetwork() {	
	List<LogEntry> entries = DriverContext.getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
	System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
	return entries;

	}
	public static String buscarDatoXHR(String[] parts,String datoBuscar) {
		String cadena = null;
		 for(String busqueda : parts) {
			 if (busqueda.contains(datoBuscar)) {
				 cadena=busqueda;
			 } 
		 }
		 return cadena;
		
	}

    public static List<String> consultaGenerica(String consulta, String urlDB, String userDB, String passDB){
        List<String> result = new ArrayList<>();
        Connection connection;
        try{
            connection = DriverManager.getConnection(urlDB,userDB,passDB);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberColumns = rsmd.getColumnCount();
            StringBuilder primeraRow = new StringBuilder();
            for(int i = 1;i<=numberColumns;i++){
                primeraRow.append(rsmd.getColumnLabel(i)).append(",");
            }
            result.add(primeraRow.deleteCharAt(primeraRow.length()-1).toString());
            while (rs.next()) {
                int cont = 1;
                StringBuilder rowBuilder = new StringBuilder(new StringBuilder());
                while (cont <= numberColumns){
                    try{
                        rowBuilder.append(rs.getString(cont)).append(",");
                        cont++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String row = rowBuilder.deleteCharAt(rowBuilder.length()-1).toString();
                result.add(row);
            }
            rs.close();
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
	
	
	
	
}
