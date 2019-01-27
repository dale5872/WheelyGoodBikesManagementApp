package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        try {
            /* Create and show the log in window */
            Parent root = FXMLLoader.load(getClass().getResource("FXML/LogIn.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Wheely Good Bikes");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e){
            System.out.println(e);
            System.exit(2);
        }
    }
}