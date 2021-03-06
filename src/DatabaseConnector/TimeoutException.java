package DatabaseConnector;

import App.FXControllers.ShowMessageBox;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeoutException extends Exception {

    public TimeoutException(String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);

        //show to user
        ShowMessageBox messageBox = new ShowMessageBox();
        messageBox.show(message);
    }
}
