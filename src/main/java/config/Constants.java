package config;

import java.io.File;

public class Constants {

    public static String browser = "";
    public static String URL_QA = "https://www.google.com/";
    /* variables de ambiente */
    public static final String pathArchivo = new File("").getAbsolutePath();
    public static String pathArchivoZip = System.getProperty("user.dir") + "\\tmp";
    public static final String pathPdf = System.getProperty("user.dir") + "\\tmp";
    public static final String RUTA_TESS4J_DATA = System.getProperty("user.dir") + "\\";
    public static final String RUTA_CONFIG_HTML_REPORT = System.getProperty("user.dir");
    //public static final String RUTA_CARPETA_IMAGENES = System.getProperty("user.dir") + "\\src\\test\\resources\\reports\\";
    public static final String RUTA_CARPETA_IMAGENES = System.getProperty("user.dir") + "\\target\\reporte\\";

}
