package App.FXControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DeletionConfirmationController extends PopupController{
    private String thingToDelete;

    /* Controls */
    @FXML private Label messageLabel;

    public void setThingToDelete(String thingToDelete){
        this.thingToDelete = thingToDelete;

        messageLabel.setText("Are you sure you want to delete this " + thingToDelete + "? This cannot be undone.");
    }

    @FXML
    protected void confirm(){
        switch(thingToDelete){
            case "account":
                super.parentController.deleteAccount();
                break;
            case "bike":
                super.parentController.deleteBike();
                break;
            case "equipment":
                super.parentController.deleteEquipment();
                break;
            case "location":
                super.parentController.deleteLocation();
                break;
            default:
                break;
        }

        super.close();
    }
}
