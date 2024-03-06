package testCases;

import com.github.sarxos.webcam.Webcam;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestCamaraWeb {

    public Boolean camaraWeb(){
        boolean existe = false;
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            System.out.println("Webcam: " + webcam.getName());
            try {
                webcam.open();
                BufferedImage image = webcam.getImage();
                ImageIO.write(image, "PNG", new File("images\\test.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            existe = true;
        } else {
            existe = false;
        }
        return existe;
    }

    @Test
    public void testCamara() {

        if( !camaraWeb() ){
            Assert.fail( "Camara no detectada" );
        }else{
            Assert.assertTrue( true,"Camara detectada, se obtiene foto" );
        }
    }


}
