package App.FXControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageBoxController extends Controller{
    @FXML private Label messageLabel;

    public void setMessage(String message) {
        messageLabel.setText(message);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    public void setAlwaysOnTop(boolean b){
        super.stage.setAlwaysOnTop(b);
    }

    public void closeMessageBox() {
        this.stage.close();
    }
}
