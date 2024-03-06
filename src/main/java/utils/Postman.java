package utils;

import org.testng.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static config.Constants.pathArchivo;
import static utils.bbdd.ConstanstBD.*;

public class Postman {

    public static String borrarUltimoCaracter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    public static String borrarUltimoCaracter(String str, Character character) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == character) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String createQueryFolios( int[] folios){
        int cantidadFolios = folios.length;
        String lista = "";
        System.out.println("Comienza el proceso de validación...");
        if(cantidadFolios > 1){
            for(int i = 0; i < cantidadFolios; i++){
                lista = lista + String.valueOf(folios[i]).concat( "," );
            }
        }
        String listaFolios = borrarUltimoCaracter( lista );
        String query = "select * from folios.folios f where folio in (" +( listaFolios )+")";
        System.out.println(query);
        return query;
    }
    public static int ejecutarQuery(String query) throws ClassNotFoundException {

        Class.forName("org.postgresql.Driver");
        int cantidad = 0,columnsNumber=0;
        Connection connection;
        {
            try {
                connection = DriverManager.getConnection( URL_DB_liquidador_qa, USER_BD_liquidador_qa, PASS_BD_liquidador_qa );
                Statement st=connection.createStatement();
                ResultSet rs=st.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                columnsNumber = rsmd.getColumnCount();
                System.out.println("Ejecutando query en BBDD -->");
                while(rs.next())
                {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print( rsmd.getColumnName(i)+ " " +columnValue );
                        cantidad ++;
                    }
                    System.out.println("");
                }
                //System.out.println("folios en bbdd: "+cantidad/columnsNumber);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return (cantidad/columnsNumber);
    }

    public static int ejecutarQueryAPI(String parametro) throws ClassNotFoundException {

        Class.forName("org.postgresql.Driver");
        int cantidad = 0,columnsNumber=0;
        String cabeceras="",columnValue="";
        String query ="select p.person_id, p.holder_id ,pt.name as tipo_benef\n" +
                "from patients.patients p\n" +
                "join patients.patient_types pt\n" +
                "on pt.id=p.patient_holder_relation_id\n" +
                "where p.policy_id= " +parametro;
        Connection connection;
        {
            try {
                connection = DriverManager.getConnection( URL_DB_liquidador_qa, USER_BD_liquidador_qa, PASS_BD_liquidador_qa );
                Statement st=connection.createStatement();
                ResultSet rs=st.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                columnsNumber = rsmd.getColumnCount();
                System.out.println("Ejecutando query en BBDD con parametro [" + parametro +"] dado por API -->");
                System.out.println(query+"\n");
                System.out.println("Resultado:\n");
                while(rs.next())
                {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(" | ");
                        cabeceras = rsmd.getColumnName(i);
                        columnValue = rs.getString(i);
                        System.out.print( columnValue);
                        cantidad ++;
                    }
                    System.out.println("");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return (cantidad/columnsNumber);
    }

    public static String extraerResponse(String cp, int usarPatron){

        File archivo = new File(pathArchivo.concat("\\outputfile.json"));
        String cadena = "\"assertion\": \"".concat(cp);
        String linea = "", response="",limpiar="", patron="";
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains(cadena)) {
                    linea = line;
                }
            }
            if(linea.isEmpty()){
                Assert.fail("CP no encontrado en archivo json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(usarPatron == 1){
            patron = "\\{(.*?)\\}\",";
        }else if(usarPatron == 2){
            patron = "\\{(.*?),$";
        }else if(usarPatron == 3){
            patron = "\\{(.*?)}(.*?)}";
        }

        //String fecha = "\\d{4}-\\d{2}-\\d{2}(.*?):\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(linea);

        if(matcher.find()){
            limpiar =matcher.group();
            response = limpiar.replace("\\","");
        }
        return response;
    }

    public static String responseFecha(String response){
        String fecha = "\\d{4}-\\d{2}-\\d{2}(.*?):\\d{2}:\\d{2}";
        String r="", rr="";
        Pattern pattern = Pattern.compile(fecha);
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()){
            r = matcher.group();
            System.out.println("entro");
            rr = "\"" + r + "\"";
        }
        System.out.println("> "+rr);
        return r;
    }
    public static String extraerResponse(String cp, int usarPatron, String archivoEntrada){

        File archivo = new File(pathArchivo + "\\" + archivoEntrada + ".json");
        String cadena = "\"assertion\": \"".concat(cp);
        String linea = "", response="",limpiar="", patron="";
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains(cadena)) {
                    linea = line;
                }
            }
            if(linea.isEmpty()){
                Assert.fail("CP no encontrado en archivo json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(usarPatron == 1){
            patron = "\\{(.*?)\\}\",";
        }else if(usarPatron == 2){
            patron = "\\{(.*?),$";
        }else if(usarPatron == 3){
            patron = "\\{(.*?)}(.*?)}";
        }

        //String fecha = "\\d{4}-\\d{2}-\\d{2}(.*?):\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(linea);

        if(matcher.find()){
            limpiar =matcher.group();
            response = limpiar.replace("\\","");
        }
        return response;
    }
}
