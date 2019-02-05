package DatabaseConnector;

import App.FXControllers.ShowMessageBox;

import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertFailedException extends Exception {

    public InsertFailedException(String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);

        //Show error to the user
        ShowMessageBox messageBox = new ShowMessageBox();
        messageBox.show(message);
    }

    public InsertFailedException() {
        super();
    }
}
