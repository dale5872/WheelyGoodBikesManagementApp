package App.FXControllers;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorException extends Exception {

    public ErrorException(String message, boolean showMessageBox) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);
        //show to user

        if(showMessageBox) {
            ShowMessageBox messageBox = new ShowMessageBox();
            messageBox.show(message);
        }
    }

    public ErrorException(String message, boolean showMessageBox, Exception e) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);
        e.printStackTrace();
        //show to user

        if(showMessageBox) {
            ShowMessageBox messageBox = new ShowMessageBox();
            messageBox.show(message);
        }
    }
}
