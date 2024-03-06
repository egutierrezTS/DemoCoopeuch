package config.integracionQTest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Metodos {
    public static String limpiarDato(String string){
        return string.replaceAll("á","a")
                .replaceAll("é","e")
                .replaceAll("í","i")
                .replaceAll("ó","o")
                .replaceAll("ú","u")
                .replaceAll("Á","A")
                .replaceAll("É","E")
                .replaceAll("Í","I")
                .replaceAll("Ó","O")
                .replaceAll("Ú","U")
                .replaceAll("ñ","n")
                .replaceAll("Ñ","N")
                .replaceAll("&ntilde;","n")
                .replaceAll("&aacute;","a")
                .replaceAll("&eacute;","e")
                .replaceAll("&iacute;","i")
                .replaceAll("&oacute;","o")
                .replaceAll("&uacute;","u");
    }
    public static String limpiarPaso(String string){
        return string.replaceAll("<p>","")
                .replaceAll("</p>","")
                .replaceAll("<ul>","")
                .replaceAll("https://tsoftlatam.qtestnet.com/p/96021/portal/project#tab=testdesign&amp;object=1&amp;","TESTCASE ID: ")
                .replaceAll("<br>","")
                .replaceAll("<li>","")
                .replaceAll("</a>","")
                .replaceAll("</li>","").replaceAll("\n", " ");
    }
    public static String upperCase(String string){ // Convierte la primera letra de cada palabra en MAYUSCULA
        String result = string.substring(0,1).toUpperCase(); // Se convierte a uppercase el primer caracter
        for (int i = 1;i<string.length();i++) {
            if (String.valueOf(string.charAt(i-1)).equals(" ")){
                result = result + string.substring(i,i+1).toUpperCase();
            }
            else {
                result = result + string.substring(i,i+1);
            }
        }
        return result;
    }
    public static String lowerCaseFirstChar(String string){
        return string.substring(0,1).toLowerCase(Locale.ROOT) + string.substring(1);
    }
    public static String generarCPA(String carpeta){ // Escanea una ruta contenedora de casos de prueba y devuelve un numero CPA en formato STRING
        Path ruta = Paths.get(carpeta);
        String newCPA = "";
        if (Files.exists(ruta)){
            try {
                Pattern pattern = Pattern.compile("(?<=\\\\CPA)\\d{2,6}");
                Matcher matcher = null;
                Stream<Path> rutas = Files.list(ruta);
                int cpaMayorInt = -1;
                String cpaMayorString = "";
                int[] cpaArray;
                for(Object a:rutas.toArray()){
                    matcher = pattern.matcher(a.toString());
                    if (matcher.find()){
                        String cpa = matcher.group();
                        if (cpaMayorInt < Integer.parseInt(cpa)){
                            cpaMayorInt = Integer.parseInt(cpa);
                            cpaMayorString = cpa;
                        }
                    }
                }
                cpaArray = new int[cpaMayorString.length()];
                for (int i=0;i<cpaMayorString.length();i++){
                    cpaArray[i] = Integer.parseInt(cpaMayorString.substring(i,i+1));
                }
                cpaArray = sumarAUnArrayCPA(cpaArray,1);
                for (int i=0;i<cpaArray.length;i++){
                    newCPA = newCPA + cpaArray[i];
                }


        } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newCPA;
    }
    public static int[] sumarAUnArrayCPA(int[] cpa,int suma){
        cpa[cpa.length-1] = cpa[cpa.length-1] + suma;
        for (int i=cpa.length-1;i>=0;i--){
            if (cpa[i]>9){
                cpa[i-1] = cpa[i-1] + cpa[i]/10;
                cpa[i] = cpa[i]%10;
            }
        }
        return cpa;
    }
    public static String buscarId(JSONArray modulos, String idBuscado){
        String nombre;
        for (int i = 0;i<modulos.length();i++){
            JSONObject modulo = new JSONObject(modulos.get(i).toString());
            nombre = modulo.get("name").toString();
            if (modulo.get("id").toString().equals(idBuscado)){
                return modulo.get("name").toString() + "{flag}";
            }
            else {
                if (modulo.has("children")){
                    String nombreConcatenado = buscarId(new JSONArray(modulo.get("children").toString()),idBuscado);
                    if (nombreConcatenado.contains("{flag}")){
                        return nombre +"\\" + nombreConcatenado;
                    }
                }
            }
        }
        return "No se encontro el modulo buscado";
    }
}
