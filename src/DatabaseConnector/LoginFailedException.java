package DatabaseConnector;

import App.FXControllers.ShowMessageBox;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginFailedException extends Exception {

    /**
     * Class just extends Exception, used to determine which type
     * of exception is thrown
     */
    public LoginFailedException() {
        super();
    }

    public LoginFailedException(String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);
    }


}
