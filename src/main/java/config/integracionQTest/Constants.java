package config.integracionQTest;

public class Constants {
    public static String PROYECT_ID = "96021";
    public static String URL_API = "https://tsoftlatam.qtestnet.com";
    public static String URL_QTEST = URL_API + "/p/" + PROYECT_ID + "/portal/project#tab=testdesign&object=1&id=";
    public static String API_GET_TESTCASE = URL_API + "/api/v3/projects/"+ PROYECT_ID +"/test-cases/{testID}";
    public static String API_GET_TESTSTEPS = URL_API + "/api/v3/projects/"+ PROYECT_ID +"/test-cases/{testID}/test-steps";
    public static String API_GET_MODULE = URL_API + "/api/v3/projects/"+ PROYECT_ID +"/modules/{module}";
    public static String AUTHORIZATION = "dHNvZnRsYXRhbXxqb2hubnkuZ3JlbmV0dEB0c29mdGxhdGFtLmNvbToxNjYzOTU1MTMwMjM1OjM1N2M1ZWI0Nzc0YmU1M2YyNTU1MzVhYzU0OTA3NTEx";
    public static String TEST_FOLDER = System.getProperty("user.dir") + "\\src\\main\\java\\qTestCases\\";
    public static String XML_FOLDER = System.getProperty("user.dir") + "\\src\\main\\java\\suiteXML\\";
    public static String CPA_FORMAT = "0001";
    public static String CLASS_STRUCT = "package ${package};\n" +
            "\n" +
            "import org.testng.annotations.Test;\n" +
            "import config.BaseWeb;\n" +
            "\n" +
            "/*\n" +
            "${testDescription}\n" +
            "*/\n" +
            "\n" +
            "public class ${className} extends BaseWeb {\n" +
            "    @Test(groups = {\"${testGroup}\"}, description = \"${qTestLink}\")\n" +
            "    public void ${methodName}() {\n" +
            "${testSteps}\n" +
            "    }\n" +
            "}";
    public static String SUITE_XML_STRUCT = "<!DOCTYPE suite SYSTEM \"https://testng.org/testng-1.0.dtd\" >\n" +
            "\n" +
            "<suite name=\"${suiteName}\">\n" +
            "    <parameter name=\"browser\" value=\"\"></parameter>  <!-- Se define el navegador -->\n" +
            "    <parameter name=\"carpetaReporte\" value=\"C:\\Jenkins\\${normalizedSuiteName}\"></parameter> <!-- Se define la carpeta en donde se guardarán los reportes HTML en forma de .zip -->\n" +
            "    <parameter name=\"url\" value=\"\"></parameter> <!-- Se define la página de inicio -->\n" +
            "\n" +
            "    <!-- Se inicializan los test -->\n" +
            "    \n" +
            "    ${testStructure}\n" +
            "</suite>";
    public static String TEST_XML_STRUCT = "<test name=\"${qTestName}\" enabled=\"false\">\n" +
            "        <classes>\n" +
            "            <class name=\"${testPackage}\"></class>\n" +
            "        </classes>\n" +
            "    </test>";
}
