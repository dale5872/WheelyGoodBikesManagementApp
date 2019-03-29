package App;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Main class for starting the management app with JavaFX interfaces
 */
public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        //set taskbar / dock image and text
        try {
            Taskbar t = Taskbar.getTaskbar();
            BufferedImage image = ImageIO.read(getClass().getResource("FXControllers/Logo.png"));
            t.setIconImage(image);

            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WheelyGoodBikes Management");
        } catch (Exception e) {

        }

        new JavaFXLoader().loadNewFXWindow(primaryStage,"LogIn","Wheely Good Bikes",false);
    }
}
