package config.integracionQTest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QTestCase {

    private JSONObject testCaseInfo;
    private JSONObject parentModule;
    private String productName;
    private String testCaseId;
    private ConnectQtest connectQtest = new ConnectQtest();


    public QTestCase(String testCaseID){
        this.testCaseId = testCaseID;
        // Se obtiene info del caso de prueba como el TC y el versionId (necesario para consultar los pasos del caso de prueba)
        testCaseInfo = connectQtest.getTestCase(testCaseID);
        if (testCaseInfo.length() == 0){
            System.out.println("No se obtuvo respuesta desde QTest");
            System.exit(0);
        }
        productName = getProductName();
    }
    public String getTestCaseId(){
        return this.testCaseId;
    }
    public void setParentModule(){
        this.parentModule = connectQtest.getModule(getParentModule());
    }
    public String getTc(){
        // El pid es equivalente al codigo "TC" ej: TC-3233
        return testCaseInfo.get("pid").toString().replaceAll("TC-","");
    }
    public String getTestCaseName(){
        String name = testCaseInfo.get("name").toString();
        name = Metodos.upperCase(name);
        name = name.replaceAll(" ","");
        name = name.replaceAll("-","_");
        name = name.replaceAll("\\(","");
        name = name.replaceAll(",","");
        name = name.replaceAll("\\)","");
        name = name.replaceAll("\\.","");
        name = name.replaceAll(":","");
        name = name.replaceAll("/","");
        return Metodos.limpiarDato(name);
    }
    public String getParentModule(){
        return testCaseInfo.get("parent_id").toString();
    }
    public String getTestCaseNameRaw(){
        return Metodos.limpiarDato("TC-" + getTc() + " " + testCaseInfo.get("name"));
    }
    public String getTestCaseDescription(){
        String description = testCaseInfo.get("description").toString();
        // Se limpia un poco la descripcion
        description = description.replaceAll("<p>","");
        description = description.replaceAll("</p>","\n\n");
        description = description.replaceAll("<br />","\n");
        description = description.replaceAll("<strong>","");
        description = description.replaceAll("</strong>","");
        description = description.replaceAll("<ol>","");
        description = description.replaceAll("</ol>","");
        description = description.replaceAll("<li>","");
        description = description.replaceAll("</li>","");
        description = description.replaceAll("&nbsp;","");
        description = description.replaceAll("\n\n","");
        description = description.replaceAll("<p style=\"text-align: justify;\">","");
        description = description.replaceAll("/>","");
        description = description.replaceAll("<input id=\"has-qtest-add-on\" type=\"hidden\"","");
        description = Metodos.limpiarDato(description);
        return description;
    }


    public String getTestSteps(){
        // Se construye el string que contiene los pasos del caso de prueba
        JSONArray jsonArray = new JSONArray(testCaseInfo.get("test_steps").toString());
        List<Map<String,String>> pasos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            Map<String,String> paso = new HashMap<>();
            paso.put("description",jsonobject.getString("description"));
            paso.put("expectedResult",jsonobject.getString("expected"));
            pasos.add(paso);
        }

        String testStepsString = "";
        String precondition = "\t\t// Pre-condicion: " + Metodos.limpiarPaso(testCaseInfo.get("precondition").toString()) + "\n";
        if (precondition.length() > 180) testStepsString = testStepsString + precondition.substring(0,180) + "\n";
        else testStepsString = testStepsString + precondition;
        int contador = 1;
        for (Map<String,String> map : pasos){
            testStepsString = testStepsString + "\t\t// Paso " + contador + " : \n";
            String descripcion = "\t\t// Descripcion: " + Metodos.limpiarPaso(map.get("description")) + "\n";
            if (descripcion.length() > 180) testStepsString = testStepsString + descripcion.substring(0,180) + "\n";
            else testStepsString = testStepsString + descripcion;
            String resultadoEsperado = "\t\t// Resultado esperado: " + Metodos.limpiarPaso(map.get("expectedResult")) + "\n";
            if (resultadoEsperado.length() > 180) testStepsString = testStepsString + resultadoEsperado.substring(0,180) + "\n";
            else testStepsString = testStepsString + resultadoEsperado;
            contador++;
        }
        return testStepsString;
    }


    public String getParentFolders(){
        setParentModule();
        System.out.println("Obtencion de modulos padre del caso de prueba: ");
        long startTime = System.currentTimeMillis();
        String currentFolder = "";
        String parentFolders = Metodos.lowerCaseFirstChar(parentModule.get("name").toString());
        String parentId = parentModule.get("parent_id").toString();
        while (!currentFolder.equals(productName)){
            if (parentId.equals("6989741")){
                break;
            }
            JSONObject jsonObject = connectQtest.getModule(parentId);

            parentFolders = Metodos.lowerCaseFirstChar(jsonObject.get("name").toString()) + "\\" + parentFolders;
            currentFolder = jsonObject.get("name").toString();
            parentId = jsonObject.get("parent_id").toString();
        }
        String path = parentFolders;
        path = Metodos.upperCase(path);
        path = Metodos.limpiarDato(path).replaceAll(" ","");
        path = Metodos.lowerCaseFirstChar(path);

        path = path.replaceAll(":","");
        path = path.replaceAll("/","");
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion en Obtencion de modulos padre del caso de prueba: " + (endTime - startTime) + "ms");
        return path.replaceAll("\\(","").replaceAll("\\)","").replaceAll("-","");
    }

    public String getProductName(){
        JSONArray jsonArray = new JSONArray(testCaseInfo.get("properties").toString());
        String producto = jsonArray.getJSONObject(9).get("field_value_name").toString().replaceAll("\\)","")
                .replaceAll("\\(","")
                .replaceAll(" ","");
        return Metodos.limpiarDato(producto);
    }



}
