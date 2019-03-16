package DatabaseConnector;

import App.FXControllers.ShowMessageBox;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FileNotFoundException extends Exception {

    public FileNotFoundException(String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);

        //Show error to the user
        ShowMessageBox messageBox = new ShowMessageBox();
        messageBox.show(message);
    }

    public FileNotFoundException() {
        super();
    }
}
