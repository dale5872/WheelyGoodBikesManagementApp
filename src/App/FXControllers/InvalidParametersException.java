//TODO: Show error to user

package App.FXControllers;

import java.util.logging.Level;
import java.util.logging.Logger;


public class InvalidParametersException extends Exception {

    public InvalidParametersException(String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);
        //Show error to the user

    }

    public InvalidParametersException(String className, String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, "Class: " + className + ": " + message);
        //Show error to the user

    }
}
