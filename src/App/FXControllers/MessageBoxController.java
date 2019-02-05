package App.FXControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MessageBoxController {
    @FXML private Label messageLabel;


    private String message;

    public void setMessage(String message) {
        this.message = message;
        messageLabel.setText(message);
    }

    public void initialize() {

    }

    public void closeMessageBox() {
        Stage messageBox = (Stage)messageLabel.getScene().getWindow();
        messageBox.close();
    }

}
