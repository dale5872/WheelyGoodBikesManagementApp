package App.FXControllers;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Controller {
    protected Stage stage;

    /**
     * Sets the stage associated with the controller
     * This MUST be done anytime a controller is created, as some methods may rely on knowing this (close, logout, etc)
     * @param stage
     */
    public void setStage(Stage stage){
        this.stage = stage;
        stage.getIcons().add(new Image(Controller.class.getResourceAsStream("Logo.png")));
    }
}
