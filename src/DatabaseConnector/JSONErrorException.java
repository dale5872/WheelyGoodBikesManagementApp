package DatabaseConnector;

import App.FXControllers.ShowMessageBox;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONErrorException extends Exception{

    public JSONErrorException (String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);
    }

    public JSONErrorException (String message, String stackTrace) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        String printOut = message + "\nStack Trace:\n" + stackTrace;
        lgr.log(Level.SEVERE, printOut);

    }

}
