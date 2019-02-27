package App.FXControllers;

import javafx.stage.Stage;

public class Controller {
    protected Stage stage;

    /**
     * Sets the stage associated with the controller
     * This MUST be done anytime a controller is created, as the some methods may rely on knowing this (close, logout, etc)
     * @param stage
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }
}
