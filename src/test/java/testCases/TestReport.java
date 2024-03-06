package testCases;

import com.aventstack.extentreports.Status;
import config.BaseWeb;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static config.reporteWeb.HtmlReport.*;

public class TestReport extends BaseWeb {

    @Test(groups = {"TestWeb", "ReporteWeb"}, description = "#")
    public void metodosHtmlReport() {

        // Listado Automatizadores Imed
        List<String> contenidoTabla = Arrays.asList("Nombre, Correo, Supervisor",
                "Estefanía Victoriano, evictoriano@imed.cl, Luis Ibañez",
                "Pedro Flores, pflores@imed.cl, Luis Ibañez",
                "Daniel Labra, dlabra@imed.cl, Luis Ibañez",
                "Hugo Luis, hchavez@imed.cl, Luis Ibañez");

        String ejemploRequestJSON = "{\n" +
                "  \"CodUsuario\": \"2016-8\",\n" +
                "  \"LisPrestador\": [\n" +
                "    {\n" +
                "      \"RutTratante\": \"0076098454-K\",\n" +
                "      \"CorrConvenio\": \"1\",\n" +
                "      \"RutBenef\": \"0014263069-9\",\n" +
                "      \"RutSolic\": \"0000000001-9\",\n" +
                "      \"NomSolic\": \"Prueba QA\",\n" +
                "      \"LisPrestacion\": [\n" +
                "        {\n" +
                "          \"CodPrestacion\": \"0301045\",\n" +
                "        },\n" +
                "        {\n" +
                "          \"CodPrestacion\": \"0404005\",\n" +
                "        }\n" +
                "      ],\n" +
                "      \"CodEspecia\": \"0\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"CodFinanciador\": \"1\",\n" +
                "}";

        String ejemploResponseJSON = "{\n" +
                "  \"estado\": 0,\n" +
                "  \"datos\": {\n" +
                "    \"DireccionCotizante\": \"SARA DEL CAMPO 5700 DEPTO 1004\",\n" +
                "    \"SexoBeneficiario\": \"F\",\n" +
                "    \"NumActoVenta\": 457047942,\n" +
                "    \"RutCotizante\": \"0014263069-9\",\n" +
                "    \"EdadBeneficiario\": 48,\n" +
                "    \"PlanCotizante\": \"D\",\n" +
                "      {\n" +
                "        \"NumPrebono\": 128095,\n" +
                "        \"FolioBono\": 840014165\n" +
                "      }\n" +
                "    ],\n" +
                "    \"CodError\": 0,\n" +
                "    \"NombreBeneficiario\": \"MARITZA DEL CARMEN PARRAGUEZ VERGARA\",\n" +
                "    \"GloError\": \"OK\",\n" +
                "    \"RutBeneficiario\": \"0014263069-9\",\n" +
                "  },\n" +
                "  \"mensaje\": \"La operación ha finalizado correctamente\"\n" +
                "}";

        // addWebReport(String, Markup, Status)
        addWebReport("Ejemplo uso de tablas/listas", insertarTabla(contenidoTabla), Status.INFO);
        addWebReport("Ejemplo uso de 'Bloque Comparación' - Request y Response", insertarBloqueComparacion(ejemploRequestJSON, ejemploResponseJSON), Status.INFO);

        // addWebReportImage(String, String, Status)
        addWebReportImage("Ejemplo uso captura web", "Captura de pantalla realizada", Status.INFO);
    }
}
