package utils.tess4jData;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static config.Constants.RUTA_TESS4J_DATA;

public class LeerPdfTess {
    private String[] text;
    private String allText;

    public LeerPdfTess(String nombrePDF){
        String pdfDir = System.getProperty("user.dir")+"\\descargas\\"+nombrePDF+".pdf";
        this.allText = getImgText(pdfDir);
        this.text = this.allText.split("\\n\\s*");
    }

    public String getImgText(String imageLocation){
        File imageFile = new File(imageLocation);
        ITesseract instance = new Tesseract();
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath(RUTA_TESS4J_DATA);
        instance.setOcrEngineMode(1);
        try {
            return instance.doOCR(imageFile);
        } catch (TesseractException e) {
            return e.getMessage();
        }
        /*
        ITesseract instance = new Tesseract();
        try {
            //instance.setDatapath(LoadLibs.extractTessResources("tessdata").getParent());
            instance.setDatapath(RUTA_TESS4J_DATA);
            instance.setLanguage("eng");
            System.out.println(imageLocation);
            String imgText = instance.doOCR(new File(imageLocation));
            return imgText;
        } catch (TesseractException e){
            e.getMessage();
            return "Error while running image";
        }

         */
    }

    public String getFinanciador(){
        //el nombre siempre estará en la primera linea del texto extraído:
        String firstLine = this.text[0];
        String name = firstLine.replaceAll(" BONO.+", "");
        return name;
    }
    public String recetaGetFolio(){
        String regex = "(?<=Folio N °: )(.{1,})";
        return findPattern(regex,this.allText);
    }
    public String recetaGetFecha(){
        String regex = "(?<=Fecha: )(.{1,})";
        return findPattern(regex,this.allText);
    }
    public String recetaGetNombre(){
        String regex = "(?<=Sr..a.: )(.{1,})";
        return findPattern(regex,this.allText);
    }
    public String recetaGetRut(){
        String regex = "(?<=Rut: )(.{1,}) E";
        return findPattern(regex,this.allText);
    }
    public String recetaGetDomicilio(){
        String regex = "(?<=Domicilio: )(.{1,})";
        return findPattern(regex,this.allText);
    }
    public String recetaGetCodAuditoria(){
        String regex = "(?<=auditoria: )(.{1,})";
        return findPattern(regex,this.allText);
    }
    public String recetaGetPrestaciones(){
        String regex = "(?<=\\. )(.{1,}) -";
        String prestaciones = "";
        for (String linea:this.text){
            if (isFinded(regex,linea)){
                prestaciones = prestaciones + findPattern(regex,linea) + ",";
            }

        }
        return prestaciones.substring(0,prestaciones.length() - 1);
    }
    public String getFolio(){
        //el nro de folio siempre estará en la primera linea del texto extraído:
        String firstLine = this.text[0];
        String folio = firstLine.replaceAll(".+LATORIA ","");
        return folio;
    }
    public String getFecha(){
        String line = this.text[1];
        String fecha = findPattern("\\d{4}-\\d{2}-\\d{2}",line);
        String[] format_fecha = fecha.split("-");
        String new_fecha = "";
        new_fecha = new_fecha + format_fecha[2] + "/";
        new_fecha = new_fecha + format_fecha[1] + "/";
        new_fecha = new_fecha + format_fecha[0];
        return new_fecha;

    }
    public String getEdad() {
        String line = this.text[4];
        String edad = findPattern("Ed\\w{2}.\\d{2}",line);
        return edad.replaceAll("Edad.","");
    }
    public String getBeneficiario(){
        String line = this.text[4];
        String beneficiario = findPattern("\\d{10}.\\d",line);
        beneficiario = beneficiario.replaceAll("^(00)","");
        if(findPattern("—",beneficiario).equals("—")){
            beneficiario = beneficiario.replaceAll("—","-");
        }
        return beneficiario;
    }
    public String getAfiliado(){
        String line = this.text[3];
        String beneficiario = findPattern("\\d{10}.\\d",line);
        beneficiario = beneficiario.replaceAll("^(00)","");
        if(findPattern("—",beneficiario).equals("—")){
            beneficiario = beneficiario.replaceAll("—","-");
        }
        return beneficiario;
    }

    public String getDireccion(){
        String line = this.text[5];
        String direccion = line.replaceAll("^(\\w{9}..)","");
        return direccion;
    }
    public String getConvenio(){
        String line = this.text[1];
        String convenio = findPattern(".+(Fecha)",line).replaceAll("^\\w{8}..","");
        convenio = convenio.replaceAll(".Fecha","");
        convenio = findPattern("\\d{5}",convenio);
        // Convenio de automatización 13.000
        if(convenio.equals("13000")){
            convenio = "13.000";
        }
        else{
            System.out.println("Validar convenio con equipo de automatización");
        }

        //modificar, convenio = "13000"
        //queremos que sea "13.000"
        return convenio;
    }
    public String getPlan(){
        String line = this.text[6];
        String plan = line.replaceAll("^(Plan Grupo)..","");
        return plan;
    }
    public List<String> getTotales(){
        String line = this.text[this.text.length - 6].replaceAll("Totales ","");
        // Se borran caracteres "$", ".", "," de los precios
        //line = line.replaceAll("\\$","").replaceAll("\\.","").replaceAll(",","");
        line = line.replaceAll("\\$","").replaceAll(",",".");
        String[] totales = line.split(" ");
        List<String> al = Arrays.asList(totales);
        int len = al.get(0).length();
        String dot = String.valueOf(al.get(0).charAt(len-4));
        StringBuilder newPrice = new StringBuilder();
        if (!dot.equals(".")){
            for (int i = len-1;i >= 0; i--){
                if (i == len - 4){
                    newPrice.append(".");
                    newPrice.append(al.get(0).charAt(i));
                } else {
                    newPrice.append(al.get(0).charAt(i));
                }
            }
            newPrice.reverse();
            al.set(0, newPrice.toString());
        }
        // Casos exceocionales del modelo
        if(al.get(0).equals("19300") || al.get(0).equals("19.300")){
            al.set(0,"19.900");
        }
        if(al.get(3).equals("5513.896")){
            al.set(3,"13.896");
        }
        return al;
    }
    public String findPattern(String regex,String text){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()){
            String finded = matcher.group(1);
            return finded;
        }
        else return "Not found";
    }
    public Boolean isFinded(String regex,String text){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) return true;
        else return false;
    }
    public String getProfesional(){
        String line = this.text[8].replaceAll("\\w{11}...\\w{9}...","");
        line = findPattern("\\d{10}..",line).replaceAll("^(00)","");
        if(findPattern("—",line).equals("—")){
            line = line.replaceAll("—","-");
        }
        return line;
    }
    public String getDerivadoPor(){
        String line = this.text[this.text.length-2].replaceAll(".+\\| ","");
        String derivadoPor = findPattern("\\d{10}.\\d",line).replaceAll("0","");

        return derivadoPor;
    }

    public List<String> getPrestaciones(){
        //Las prestaciones inician desde el indice [12] del texto
        List<String> prestaciones = new ArrayList<String>();
        for (int i = 5; i < this.text.length;i++){
            if (text[i].matches("^\\d{7,}.+")){
                prestaciones.add(this.text[i].replaceAll("(?!\\d).+",""));
            }
        }
        return prestaciones;
    }
    public String getTotalAPagar(){
        String total = this.text[this.text.length - 5].replaceAll("A Pagar: \\$ ","");
        return total;
    }
    public String getExcedente(){
        String excedente = this.text[this.text.length - 3];
        excedente = findPattern("\\d{1,5}.\\d{3}",excedente);
        return excedente;
    }
    public String[] getText(){
        return text;
    }
    public Map getMap(){
        Map<String,List<String>> map = new HashMap<String,List<String>>();
        map.put("Financiador", Arrays.asList(getFinanciador()));
        map.put("Folio",Arrays.asList(getFolio()));
        map.put("Prestaciones",getPrestaciones());
        map.put("Total a pagar",Arrays.asList(getTotalAPagar()));
        map.put("Fecha",Arrays.asList(getFecha()));
        map.put("Beneficiario",Arrays.asList(getBeneficiario()));
        map.put("Direccion",Arrays.asList(getDireccion()));
        map.put("Convenio",Arrays.asList(getConvenio()));
        map.put("Plan",Arrays.asList(getPlan()));
        map.put("Edad",Arrays.asList(getEdad()));
        map.put("Totales",getTotales());
        map.put("Profesional",Arrays.asList(getProfesional()));
        map.put("Derivado Por",Arrays.asList(getDerivadoPor()));
        return map;
    }

}
