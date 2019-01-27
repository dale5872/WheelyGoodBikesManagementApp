package DatabaseConnector;

public class NotManagerException extends Exception {

    /**
     * Class just extends Exception, used to determine which type
     * of exception is thrown
     */
    public NotManagerException() {
        super();
    }

    public NotManagerException(String message) {
        super(message);
    }


}
