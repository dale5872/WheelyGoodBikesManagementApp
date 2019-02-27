package App;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class for starting the management app with JavaFX interfaces
 */
public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        new JavaFXLoader().loadNewFXWindow(primaryStage,"LogIn","Wheely Good Bikes",false);
    }
}
