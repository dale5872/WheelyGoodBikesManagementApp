package DatabaseConnector;

import java.util.logging.Logger;

public class InsertFailedException extends Exception {

    public InsertFailedException(String message) {
        //TODO: Show messagebox
        super(message);
        this.printStackTrace();
    }

    public InsertFailedException() {
        super();
    }
}
