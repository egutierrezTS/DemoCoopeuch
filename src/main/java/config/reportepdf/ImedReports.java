package config.reportepdf;


import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import config.driver.DriverContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import static config.Constants.browser;
import static config.Constants.pathArchivo;
import static config.driver.DriverContext.quitDriver;
import static config.reportepdf.EstadoPrueba.FAILED;
import static config.reportepdf.EstadoPrueba.PASSED;
import static utils.MetodosGenericos.tipoAmbiente;


public class ImedReports {
    private static PdfDocument pdfDocument;
    private static Document document;
    private static PdfFont pdfFont;
    private static boolean estadoFinalPrueba;
    private static Date tiempoInicial;
    private static Long tinicial;
    private static Long tfinal;
    private static Date tiempoFinal;
    private static String nombreRandom;
    public static String testName = "";
    private static String urlPDF;
    private static int numeroPasos;

    public ImedReports() {

            try {
                numeroPasos = 1;
                estadoFinalPrueba = false;
                pdfFont = PdfFontFactory.createFont( StandardFonts.COURIER);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //Establece el tamanio carta del documento PDF
    public static PageSize establecerSizeCarta() {
            float anchoLetter = PageSize.LETTER.getWidth();
            float altoLetter = PageSize.LETTER.getHeight();
            return new PageSize(anchoLetter, altoLetter);
    }

    //Establece el color del texto de los estados final de la ejecucion
    public static Text obtenerEstado() {
            Text text;
            if (estadoFinalPrueba) {
            text = new Text("PASSED");
            text.setStrokeColor(new DeviceRgb(0, 120, 0));
            } else {
                text = new Text("FAILED");
                text.setFontColor( new DeviceRgb(255, 0, 0));
            }
            text.setFont(pdfFont);
            return text;
    }

    //Obtener instancia del tiempo de ejecucion
    public static Date getTiempoEjecucion() {
            return new Date(System.currentTimeMillis());
    }

    public static Long getTiempoEjecucionLong() {
            return System.currentTimeMillis();
    }

    //Obtener una cadena de String aleatoria
    public static String generadorTexto(int count) {
            return new BigInteger(130, new SecureRandom()).toString(count).substring(0, count);
    }

    //crea directorio /tmp
    public static void crearDirectorio(){

        File file = new File(pathArchivo.concat( "\\tmp" ));
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }


    //iniciar construccion de pdf
    public static void creaPDF(String destino) {
        crearDirectorio();
        tiempoInicial = getTiempoEjecucion();
        tinicial = getTiempoEjecucionLong();
        urlPDF = destino;
        nombreRandom = urlPDF + "/" + generadorTexto(15) + ".pdf";
        numeroPasos = 1;
        estadoFinalPrueba = false;
        testName = obtenerNombre();
        try {
        PdfWriter pdfWriter = new PdfWriter(nombreRandom);
        pdfDocument = new PdfDocument(pdfWriter);
        pdfFont = PdfFontFactory.createFont(StandardFonts.COURIER);

        pdfDocument.setDefaultPageSize(establecerSizeCarta());
        //pdfDocument.addNewPage();
        //form = PdfAcroForm.getAcroForm(pdfDocument, true);
        document = new Document(pdfDocument);
        // document.setMargins(150, 60, 250, 60);

        } catch (FileNotFoundException e) {
            System.out.println("Pdf no pudo crearse, falta de espacio o ubicacion incorrecta");
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //obtiene el tamaño de la celda en porcentaje, se ingresa paragraph y negrita
    public static Cell obtenerCelda(int porcentaje, Paragraph p) {
            Cell cell = new Cell(1, porcentaje);
            cell.setVerticalAlignment( ( VerticalAlignment.MIDDLE ) );
            cell.add(p);
            return cell;
    }

    //obtener parrafo con String simple
    public static Paragraph obtenerParrafo(String texto, int size, boolean bold) {
            Paragraph p = new Paragraph();
            p.setFontSize(size);
            p.setTextAlignment( TextAlignment.LEFT);//orientacion texto izquierda
            p.setMultipliedLeading(1.1F);//espaciado entre linea
            p.setMarginTop(1.1F);//margen superior del texto
            p.setMarginBottom(1.1F); //margen inferior del texto
            p.setMarginLeft(1.1F);  //margen texto izquierda
            //p.setFixedLeading(10F); //tamanio de alto estatico
            if (bold) {
            p.setBold();//negrita
            }
            p.add(texto);
            return p;
    }

    //obtener parrafo con dos texto una negrita y la otra no
    public static Paragraph obtenerParrafo(Text texto1, Text texto2, int size) {
            Paragraph p = new Paragraph();
            p.setFontSize(size);
            //p.setTextAlignment(TextAlignment.LEFT);//orientacion texto izquierda
            p.setMultipliedLeading(1F);//espaciado entre linea
            //p.setMarginTop(0.5F);//margen superior del texto
            p.setMarginBottom(1F); //margen inferior del texto
            //p.setMarginLeft(1.1F);  //margen texto izquierda
            texto1.setBold();
            p.add(texto1);
            p.add(texto2);
            return p;
    }

    //obtener parrafo con Clase Text simple
    public static Paragraph obtenerParrafo(Text texto, int size, boolean bold) {
            Paragraph p = new Paragraph();
            p.setFontSize(size);
            p.setTextAlignment(TextAlignment.LEFT);//orientacion texto izquierda
            p.setMultipliedLeading(1.1F);//espaciado entre linea
            p.setMarginTop(1.1F);//margen superior del texto
            p.setMarginBottom(1.1F); //margen inferior del texto
            p.setMarginLeft(1.1F);  //margen texto izquierda
            //p.setFixedLeading(10F); //tamanio de alto estatico
            if (bold) {
            p.setBold();//negrita
            }
            p.add(texto);
            return p;
    }

    //ingresa una imagen en una celda
    public static Paragraph ingresarImagenCelda(Image imagen) {
            imagen.setAutoScale(true);//ajusta al tamaño de la celda
            Paragraph p = new Paragraph();
            p.add(imagen);
            return p;
            }

    //obtener captura de pantalla
    private static byte[] getScreenshotWeb() {
            return ((TakesScreenshot) DriverContext.getDriver()).getScreenshotAs( OutputType.BYTES);
    }

    public static void nombreClase(String nombre){
            testName = nombre;
    }
    public static String obtenerNombre() {
            return testName;
    }


    public static void crearPortada() {
            pdfDocument.addNewPage(1);
            pdfDocument.getFirstPage();
            Document portada = new Document(pdfDocument);
            portada.setMargins(60, 60, 60, 60);
            Image imagen;
            try {
                imagen = new Image(ImageDataFactory.create(System.getProperty("user.dir") + "\\src\\main\\java\\config\\reportepdf\\imed.png"));
                imagen.scaleAbsolute(400.0F, 250.0F);
                float marginTop = ((PageSize.LETTER.getHeight() / 2) - (imagen.getImageScaledHeight() / 2) - 60) / 4;
                float marginLeft = (PageSize.LETTER.getWidth() / 2) - (imagen.getImageScaledWidth() / 2) - 60;
                imagen.setMargins(marginTop, 0, 0, marginLeft);
                portada.add(imagen);

                Text texto = new Text("Pruebas Automatizadas");
                texto.setCharacterSpacing(3F);
                Paragraph p = obtenerParrafo(texto, 26, true);
                p.setTextAlignment(TextAlignment.CENTER);
                //p.setSpacingRatio();
                portada.add(p);

                SimpleDateFormat formatterDay = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat formatterEjecucion = new SimpleDateFormat("mm:ss");

                texto = new Text("Fecha de ejecuci\u00f3n: ");
                Text text2 = new Text(formatterDay.format(tiempoInicial));
                p = obtenerParrafo(texto, text2, 12);
                p.setMarginTop(75);
                p.setMarginLeft(300);
                portada.add(p);


                texto = new Text("Hora de inicio: ");
                text2 = new Text(formatterTime.format(tiempoInicial));
                p = obtenerParrafo(texto, text2, 12);
                p.setMarginLeft(300);
                portada.add(p);

                texto = new Text("Hora de termino: ");
                text2 = new Text(formatterTime.format(tiempoFinal));
                p = obtenerParrafo(texto, text2, 12);
                p.setMarginLeft(300);
                portada.add(p);

                Date tEjecucion = new Date(tfinal - tinicial + 1000L);
                texto = new Text("Tiempo de ejecuci\u00f3n: ");
                text2 = new Text(formatterEjecucion.format(tEjecucion));
                p = obtenerParrafo(texto, text2, 12);
                p.setMarginLeft(300);
                portada.add(p);

                texto = new Text("Nombre de la prueba: ");
                text2 = new Text(testName);
                p = obtenerParrafo(texto, text2, 12);
                p.setMarginTop(50);
                p.setTextAlignment(TextAlignment.LEFT);
                portada.add(p);

                texto = new Text("Nombre del browser: ");
                text2 = new Text( browser);
                p = obtenerParrafo(texto, text2, 12);
                p.setTextAlignment(TextAlignment.LEFT);
                portada.add(p);

                texto = new Text("Ambiente: ");
                text2 = new Text(tipoAmbiente());
                p = obtenerParrafo(texto, text2, 12);
                p.setTextAlignment(TextAlignment.LEFT);
                portada.add(p);

                texto = new Text("Estado de la ejecuci\u00f3n: ");
                text2 = obtenerEstado().setBold();
                p = obtenerParrafo(texto, text2, 12);
                p.setTextAlignment(TextAlignment.LEFT);
                portada.add(p);
                portada.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
    }


    public static void ingresarTablaReport(String nombre, String descripcion) {
            document.setMargins(150, 60, 300, 60);
            Table tabla = new Table(UnitValue.createPercentArray(10)).useAllAvailableWidth().setFixedLayout();
            tabla.setWidth(UnitValue.createPercentValue(100));
            tabla.setKeepTogether(true); //evitar que la tabla se separe si sobrepasa el tamanio de la pagina
            //tabla.setVerticalAlignment(VerticalAlignment.MIDDLE);

            String texto = "Paso " + (numeroPasos++) + ":";
            Paragraph p = obtenerParrafo(texto, 12, true);
            tabla.addCell(obtenerCelda(3, p));
            p = obtenerParrafo(nombre, 12, false);
            tabla.addCell(obtenerCelda(7, p));

            texto = "Descripci\u00f3n:";
            p = obtenerParrafo(texto, 12, true);
            tabla.addCell(obtenerCelda(3, p));
            p = obtenerParrafo(descripcion, 12, false);
            tabla.addCell(obtenerCelda(7, p));

            texto = "Estado obtenido:";
            p = obtenerParrafo(texto, 12, true);
            tabla.addCell(obtenerCelda(3, p));
            p = obtenerParrafo(obtenerEstado(), 12, true);
            tabla.addCell(obtenerCelda(7, p));
            //DriverContext.setUp(Chrome, licenciaQa);
            Image imagen = new Image(ImageDataFactory.create(getScreenshotWeb()));
            p = ingresarImagenCelda(imagen);
            tabla.addCell(obtenerCelda(10, p));

            document.add(tabla);
            //document.add(new AreaBreak());
    }

    public static void ingresarTablaReportSoloTexto(String nombre, String descripcion) {
            document.setMargins(62, 60, 300, 60);
            Table tabla = new Table(UnitValue.createPercentArray(10)).useAllAvailableWidth().setFixedLayout();
            tabla.setWidth(UnitValue.createPercentValue(100));
            tabla.setKeepTogether(true); //evitar que la tabla se separe si sobrepasa el tamanio de la pagina
            //tabla.setVerticalAlignment(VerticalAlignment.MIDDLE);

            String texto = "Paso " + (numeroPasos++) + ":";
            Paragraph p = obtenerParrafo(texto, 12, true);
            tabla.addCell(obtenerCelda(3, p));
            p = obtenerParrafo(nombre, 12, false);
            tabla.addCell(obtenerCelda(7, p));

            texto = "Descripci\u00f3n:";
            p = obtenerParrafo(texto, 12, true);
            tabla.addCell(obtenerCelda(3, p));
            p = obtenerParrafo(descripcion, 12, false);
            tabla.addCell(obtenerCelda(7, p));

            texto = "Estado obtenido:";
            p = obtenerParrafo(texto, 12, true);
            tabla.addCell(obtenerCelda(3, p));
            p = obtenerParrafo(obtenerEstado(), 12, true);
            tabla.addCell(obtenerCelda(7, p));
            //DriverContext.setUp(Chrome, licenciaQa);
    //        Image imagen = new Image(ImageDataFactory.create(getScreenshotWeb()));
    //        p = ingresarImagenCelda(imagen);
    //        tabla.addCell(obtenerCelda(10, p));

            document.add(tabla);
            //document.add(new AreaBreak());
    }

    private static String obtenerNombrePdf() {
            return estadoFinalPrueba ? testName + " -  Passed" : testName + " - Failed";
    }

    public static void addWebReportImage(String nombrePaso, String descripcion, EstadoPrueba estadoPrueba, boolean fatal) {
        switch (estadoPrueba) {
            case FAILED:
            estadoFinalPrueba = false;
            break;
            case PASSED:
            estadoFinalPrueba = !fatal;
            break;
            }
            ingresarTablaReport(nombrePaso, descripcion);
            if (fatal) {
            pdfClose();
            DriverContext.quitDriver();
            System.exit(0);
            }
    }

    public static void addWebReport(String nombrePaso, String descripcion, EstadoPrueba estadoPrueba, boolean fatal) {
        switch (estadoPrueba) {
            case FAILED:
            estadoFinalPrueba = false;
            break;
            case PASSED:
            estadoFinalPrueba = !fatal;
            break;
            }
            ingresarTablaReportSoloTexto(nombrePaso, descripcion);
            if (fatal) {
            pdfClose();
            quitDriver();
            System.exit(0);
            }
    }

    public static void pdfClose() {
        System.out.println("[PdfImedReport] closePDF");
        tiempoFinal = getTiempoEjecucion();
        tfinal = getTiempoEjecucionLong();
        crearPortada();
        document.close();
        pdfDocument.close();

        try {
            PdfReader reader = new PdfReader(nombreRandom);
            int n = reader.getNumberOfPages();
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(urlPDF + "/" + obtenerNombrePdf() + ".pdf"));
            int i = 0;

            while (i < n) {
            ++i;
            PdfContentByte pagecontent = stamper.getOverContent(i);
            ColumnText.showTextAligned(pagecontent, 2, new Phrase(String.format("P\u00e1gina %s de %s", i, n)), PageSize.LETTER.getRight() - 20, PageSize.LETTER.getTop() - 20, 0.0F);
            }
            stamper.close();
            reader.close();
            File file2 = new File(nombreRandom);
            if (file2.delete()) {
            System.out.println("Temporal File eliminado: " + nombreRandom);
            } else {
            System.out.println("Temporal File no encontrado");
            }
        } catch (IOException e ) {
            e.printStackTrace(System.out);
        }catch(DocumentException e){
            e.printStackTrace(System.out);
        }
    }

    public static void reporteObjetoDesplegado(boolean estadoObjeto, String objeto, String vista, boolean fatal) {

        if (estadoObjeto) {
            addWebReportImage( "Elemento encontrado: " + objeto, "El objeto:" + objeto + ", se visualiza correctamente en la vista de " + vista + ".", PASSED, fatal );
        } else {
            addWebReportImage( "Elemento no encontrado: " + objeto, "El objeto:" + objeto + ", no se visualiza  en la vista de " + vista + ".", FAILED, fatal );
            Assert.fail( "Elemento no encontrado: " + objeto );
        }

    }

}