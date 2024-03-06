package config.integracionQTest;

public class GenerarCaso {
    public static void rutaDinamica(String idQTest){
        CreateTest createTest = new CreateTest(true);
        createTest.createTests(idQTest);
    }
    public static void rutaEstatica(String idQTest){
        CreateTest createTest = new CreateTest();
        createTest.createTests(idQTest);
    }
}
