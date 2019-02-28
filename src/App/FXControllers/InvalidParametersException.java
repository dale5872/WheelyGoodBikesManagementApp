package App.FXControllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;


public class InvalidParametersException extends Exception {

    public InvalidParametersException(String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);
        //Show error to the user

        ShowMessageBox messageBox = new ShowMessageBox();
        messageBox.show(message);

    }

    public InvalidParametersException(String className, String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, "Class: " + className + ": " + message);
        //Show error to the user

        ShowMessageBox messageBox = new ShowMessageBox();
        messageBox.show(message);
    }
}
