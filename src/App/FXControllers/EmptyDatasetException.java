package App.FXControllers;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EmptyDatasetException extends Exception {

    public EmptyDatasetException(String message, boolean showMessageBox) {
        super(message);
        Logger lgr = Logger.getLogger(this.getClass().getName());
        lgr.log(Level.SEVERE, message);
        //show to user

        if(showMessageBox) {
            ShowMessageBox messageBox = new ShowMessageBox();
            messageBox.show(message);
        }
    }

    public EmptyDatasetException() {
        super();
    }
}
