package App.FXControllers;

public class EmptyDatasetException extends Exception {

    public EmptyDatasetException(String message) {
        super(message);
        //TODO: MessageBox to user
    }

    public EmptyDatasetException() {
        super();
    }
}
