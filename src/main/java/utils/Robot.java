package utils;


import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Robot {

    public static void tamanoPantalla() throws AWTException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int altura = screenSize.height;
        int ancho = screenSize.width;

        if(altura == 768 && ancho == 1366){
            clickPantalla14();
        }
        if(altura == 1080 && ancho == 1920){
            clickPantalla15();
        }

    }

    public static void clickPantalla14() throws AWTException{

        java.awt.Robot bot = new java.awt.Robot();
        bot.mouseMove(906,702);
        esperar( 4 );
        bot.mousePress( InputEvent.BUTTON1_MASK);
        bot.mouseRelease( InputEvent.BUTTON1_MASK);
    }
    public static void clickPantalla15() throws AWTException{

        java.awt.Robot bot = new java.awt.Robot();
        bot.mouseMove(1180,1015);
        esperar( 4 );
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease( InputEvent.BUTTON1_MASK);
    }

    public static void robotEnter()  {
        java.awt.Robot r = null;
        try {
            r = new java.awt.Robot();
            r.keyPress(10);
            r.keyRelease(10);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void presionaTabWindows()  {
        java.awt.Robot r = null;
        try {
            r = new java.awt.Robot();
            r.keyPress( KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void presionaGuardarWindows() {
        try {
            java.awt.Robot robot = new java.awt.Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_S);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_S);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void presionaCancelarWindows() {
        esperar( 2 );
        java.awt.Robot r = null;
        try {
            r = new java.awt.Robot();
            r.keyPress( KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            esperar( 1 );
            r.keyPress( KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            esperar( 1 );
            r.keyPress( KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            esperar( 1 );
            r.keyPress( KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            esperar( 1 );
            r.keyPress( KeyEvent.VK_ENTER);
            r.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void esperar(int tiempo){
        try{
            Thread.sleep( tiempo * 1000 );
        }catch (InterruptedException io){
            System.out.println(">>> "+io);
        }
    }

    public static void arrowDown(){
        esperar( 1 );
        java.awt.Robot r = null;
        try {
            r = new java.awt.Robot();
            r.keyPress( KeyEvent.VK_DOWN);
            r.keyRelease(KeyEvent.VK_DOWN);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        robotEnter();
    }

    public static void robotDisco()  {
        java.awt.Robot robot = null;
        try {
            robot = new java.awt.Robot();
            robot.delay(20);
            robot.keyPress(16);
            robot.keyPress(67);
            robot.keyRelease(67);
            robot.keyRelease(16);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void robotSlash()  {
        java.awt.Robot robot = null;
        try {
            robot = new java.awt.Robot();
            robot.delay(100);
            robot.keyPress(18);
            robot.keyPress(105);
            robot.keyRelease(105);
            robot.delay(30);
            robot.keyPress(98);
            robot.keyRelease(98);
            robot.keyRelease(18);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    public static void robotComma()  {
        java.awt.Robot robot = null;
        try {
            robot = new java.awt.Robot();
            robot.delay(100);
            robot.keyPress(18);
            robot.keyPress(101);
            robot.keyRelease(101);
            robot.delay(30);
            robot.keyPress(104);
            robot.keyRelease(104);
            robot.keyRelease(18);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    public static void robotType(String s)  {
        java.awt.Robot robot = null;
        try {
            robot = new java.awt.Robot();
            byte[] bytes = s.getBytes();
            for (byte b : bytes)
            {
                int code = b;
                // keycode only handles [A-Z] (which is ASCII decimal [65-90])
                if (code > 96 && code < 123) code = code - 32;
                if (code == 95){
                    // imprime underScore
                    robotUnderScore();
                    continue;
                }
                robot.delay(40);
                robot.keyPress(code);
                robot.keyRelease(code);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void robotUnderScore()  {
        java.awt.Robot robot = null;
        try {
            robot = new java.awt.Robot();
            robot.delay(30);
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_NUMPAD9);
            robot.keyRelease(KeyEvent.VK_NUMPAD9);
            robot.delay(30);
            robot.keyPress(KeyEvent.VK_NUMPAD5);
            robot.keyRelease(KeyEvent.VK_NUMPAD5);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }


    public static void robotPath(String ruta)  {
        String[] parts = ruta.split("\\\\");
        robotDisco();
        robotComma();
        robotSlash();
        //Thread.sleep( 200 );
        for(int i=1; i < parts.length; i++){
            System.out.println(parts[i]);
            try {
                Thread.sleep( 300 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robotType( parts[i] );
            if (!parts[i].contains( "jpg" )) {
                robotSlash();
            }
        }
    }

    public static void robotRuta(String ruta, String tipoArchivo){
        String[] parts = ruta.split("\\\\");
        robotDisco();
        robotComma();
        robotSlash();
        for(int i=1; i < parts.length; i++){
            System.out.println(parts[i]);
            esperar( 1 );
            robotType( parts[i] );
            if( (!parts[i].contains( tipoArchivo )) ) {
                robotSlash();
            }
        }
    }

}
