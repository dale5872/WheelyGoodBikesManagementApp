package App.FXControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageBoxController extends Controller{
    @FXML private Label messageLabel;

    private String message;

    public void setMessage(String message) {
        this.message = message;
        messageLabel.setText(message);
    }

    public void setAlwaysOnTop(boolean b){
        super.stage.setAlwaysOnTop(b);
    }

    public void closeMessageBox() {
        this.stage.close();
    }
}
