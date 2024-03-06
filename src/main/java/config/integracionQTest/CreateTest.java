package config.integracionQTest;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CreateTest {
    private String ruta;
    private String testFolder;
    private String testPackage;
    private String className;
    private JSONArray modulos;
    private boolean rutaDinamica;
    public CreateTest(){
        this.testFolder = Constants.TEST_FOLDER;
        this.testPackage = "qTestCases";
        this.rutaDinamica = false;
    }
    public CreateTest(boolean rutaDinamica){
        this.rutaDinamica = rutaDinamica;
    }
    public void initPath(){
        if (rutaDinamica){
            // Crear ruta dinamica : PENDIENTE
            this.testFolder = System.getProperty("user.dir") + "\\src\\main\\java\\testCases\\";
            this.testPackage = "testCases";
            this.rutaDinamica = true;
        }
        else{
            this.testFolder = Constants.TEST_FOLDER;
            this.testPackage = "qTestCases";
            this.rutaDinamica = false;
        }
    }

    public void createTests(String tests){
        if (rutaDinamica){
            setModulos();
            for (String id:tests.split(",")){
                QTestCase qTestCase = new QTestCase(id);
                long startTime = System.currentTimeMillis();
                String path = Metodos.buscarId(modulos,qTestCase.getParentModule());
                System.out.println("Tiempo de busqueda recursiva: " + (System.currentTimeMillis() - startTime) + "ms");
                path = Metodos.upperCase(path);
                path = Metodos.limpiarDato(path).replaceAll(" ","");
                path = Metodos.lowerCaseFirstChar(path).replaceAll("\\(","").replaceAll("\\)","").replaceAll("-","").replace("{flag}","").replaceAll("/","");

                dummy(qTestCase,path);
            }
        }
        else {
            for (String id:tests.split(",")){
                QTestCase qTestCase = new QTestCase(id);
                dummy(qTestCase,"");
            }
        }

    }
    public void setModulos(){
        ConnectQtest connectQtest = new ConnectQtest();
        this.modulos = connectQtest.getAllModules();
    }

    public void dummy(QTestCase qTestCase, String rutaPadres){
        initPath(); // Se inicializan los path en caso de ser dinamico o estatico
        if (rutaDinamica){
            String parentsFolder = rutaPadres;
            this.testFolder = this.testFolder + parentsFolder;
            String parentFolders = parentsFolder.replaceAll("\\\\",".").replaceAll("/","");
            testPackage = testPackage + "." + parentFolders;
        }
        try {
            String cpa = "";
            Path carpeta = Paths.get(this.testFolder);
            if (!Files.exists(carpeta)){ // Si no existe la carpeta se crea, y ademas se inicializa el codigo CPA
                Files.createDirectories(carpeta);
                cpa = "CPA" + Constants.CPA_FORMAT;
            }
            else {
                cpa = "CPA" + Metodos.generarCPA(this.testFolder);// Realizar el autoincremento del CPA PENDIENTE <--------------------------------------------
            }
            String className = "TC"+qTestCase.getTc()+"_"+qTestCase.getTestCaseName(); // Se genera el nombre de la clase
            String classNameWithCPA = cpa + "_" + className; // Se le concatena el codigo CPA
            this.className = classNameWithCPA;
            if (rutaDinamica) ruta = this.testFolder + "\\" + classNameWithCPA +".java"; // Se genera la ruta de la clase
            else ruta = this.testFolder + "" + classNameWithCPA +".java"; // Se genera la ruta de la clase
            System.out.println(ruta);


            // Se crea el archivo java y se procede a editar las variables de la estructura
            File testCase = new File(ruta);
            if (testCase.createNewFile()) {
                System.out.println("File created: " + testCase.getName());
                FileWriter fileWriter = new FileWriter(ruta);

                // Se prepara la estructura de clase para insertarla en la clase java
                String insertarEnClase = Constants.CLASS_STRUCT.replace("${testDescription}",qTestCase.getTestCaseDescription());
                insertarEnClase = insertarEnClase.replace("${className}",classNameWithCPA);
                insertarEnClase = insertarEnClase.replace("${testGroup}","!");
                insertarEnClase = insertarEnClase.replace("${qTestLink}",Constants.URL_QTEST + qTestCase.getTestCaseId());
                insertarEnClase = insertarEnClase.replace("${methodName}",className);
                insertarEnClase = insertarEnClase.replace("${testSteps}",qTestCase.getTestSteps());
                insertarEnClase = insertarEnClase.replace("${package}",testPackage);
                fileWriter.write(insertarEnClase);
                fileWriter.close();
            } else {
                System.out.println("Ya existe el caso de prueba.");
            }
        } catch (IOException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
        createXML(qTestCase);
    }

    public void createTest(String testCaseID){
        initPath(); // Se inicializan los path en caso de ser dinamico o estatico
        QTestCase qTestCase = new QTestCase(testCaseID);
        if (rutaDinamica){ // En caso de ser dinamico se obtienen los nombres de los modulos padre del caso de prueba
            String parentsFolder = qTestCase.getParentFolders();
            this.testFolder = this.testFolder + parentsFolder;
            String parentFolders = parentsFolder.replaceAll("\\\\",".");
            testPackage = testPackage + "." + parentFolders;
        }
        try {
            String cpa = "";
            Path carpeta = Paths.get(this.testFolder);
            if (!Files.exists(carpeta)){ // Si no existe la carpeta se crea, y ademas se inicializa el codigo CPA
                Files.createDirectories(carpeta);
                cpa = "CPA" + Constants.CPA_FORMAT;
            }
            else {
                cpa = "CPA" + Metodos.generarCPA(this.testFolder);// Realizar el autoincremento del CPA PENDIENTE <--------------------------------------------
            }
            String className = "TC"+qTestCase.getTc()+"_"+qTestCase.getTestCaseName(); // Se genera el nombre de la clase
            String classNameWithCPA = cpa + "_" + className; // Se le concatena el codigo CPA
            this.className = classNameWithCPA;
            if (rutaDinamica) ruta = this.testFolder + "\\" + classNameWithCPA +".java"; // Se genera la ruta de la clase
            else ruta = this.testFolder + "" + classNameWithCPA +".java"; // Se genera la ruta de la clase
            System.out.println(ruta);


            // Se crea el archivo java y se procede a editar las variables de la estructura
            File testCase = new File(ruta);
            if (testCase.createNewFile()) {
                System.out.println("File created: " + testCase.getName());
                FileWriter fileWriter = new FileWriter(ruta);

                // Se prepara la estructura de clase para insertarla en la clase java
                String insertarEnClase = Constants.CLASS_STRUCT.replace("${testDescription}",qTestCase.getTestCaseDescription());
                insertarEnClase = insertarEnClase.replace("${className}",classNameWithCPA);
                insertarEnClase = insertarEnClase.replace("${testGroup}","!");
                insertarEnClase = insertarEnClase.replace("${qTestLink}",Constants.URL_QTEST + testCaseID);
                insertarEnClase = insertarEnClase.replace("${methodName}",className);
                insertarEnClase = insertarEnClase.replace("${testSteps}",qTestCase.getTestSteps());
                insertarEnClase = insertarEnClase.replace("${package}",testPackage);
                fileWriter.write(insertarEnClase);
                fileWriter.close();
            } else {
                System.out.println("Ya existe el caso de prueba.");
            }
        } catch (IOException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
        createXML(qTestCase);
    }

    public void createXML(QTestCase qTestCase){
        try {
            String ruta = Constants.XML_FOLDER + "suite.xml";
            System.out.println(ruta);
            Path carpeta = Paths.get(Constants.XML_FOLDER);
            if (!Files.exists(carpeta)){
                Files.createDirectories(carpeta);
            }
            File testCase = new File(ruta);
            if (testCase.createNewFile()) {
                System.out.println("File created: " + testCase.getName());
                FileWriter fileWriter = new FileWriter(ruta);

                fileWriter.write(Constants.SUITE_XML_STRUCT.replace("${suiteName}", qTestCase.getProductName())
                        .replace("${normalizedSuiteName}",qTestCase.getProductName())
                        .replace("${testStructure}",Constants.TEST_XML_STRUCT.replace("${qTestName}",qTestCase.getTestCaseNameRaw())
                                .replace("${testPackage}",testPackage + "." + this.className)));
                fileWriter.close();
            } else {
                try {
                    File myObj = new File(ruta);
                    Scanner myReader = new Scanner(myObj);
                    String xml = "";
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        xml = xml + data + "\n";
                    }
                    myReader.close();
                    testCase.delete();
                    if (testCase.createNewFile()){
                        System.out.println("File created: " + testCase.getName());
                        FileWriter fileWriter = new FileWriter(ruta);
                        fileWriter.write(xml.replaceAll("</suite>","    ?\n</suite>").replaceAll("\\?",Constants.TEST_XML_STRUCT.replaceAll("\\$\\{qTestName}",qTestCase.getTestCaseNameRaw())
                                .replace("${testPackage}",testPackage + "." + this.className)));
                        fileWriter.close();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }



}
