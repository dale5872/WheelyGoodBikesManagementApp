package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Main class for starting the management app with JavaFX interfaces
 * **/

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage){
        try{
            /* Create and show the log in window */
            Parent root = FXMLLoader.load(getClass().getResource("FXML/LogIn.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Wheely Good Bikes");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
            System.out.print(e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

}
