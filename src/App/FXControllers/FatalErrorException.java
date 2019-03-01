package App.FXControllers;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FatalErrorException extends Exception {

    public FatalErrorException(String message) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, "FATAL ERROR: " + message);
        //Show error to the user

        ShowMessageBox messageBox = new ShowMessageBox();
        messageBox.show("FATAL ERROR HAS OCCURED: " + message + "\nPlease reference logfile.");

        //exit
        System.exit(1);
    }

    public FatalErrorException(String message, Exception ex) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, "FATAL ERROR: " + message);
        lgr.log(Level.SEVERE, "Stack Trace:\n");
        ex.printStackTrace();
        //Show error to the user

        ShowMessageBox messageBox = new ShowMessageBox();
        messageBox.show("FATAL ERROR HAS OCCURED: " + message + "\nPlease reference logfile.");
        System.exit(1);
    }


}
