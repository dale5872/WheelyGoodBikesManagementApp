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
        OperatorSystemController controller = (OperatorSystemController) super.parentController;

        switch(thingToDelete){
            case "account":
                controller.deleteAccount();
                break;
            case "bike":
                controller.deleteBike();
                break;
            case "equipment":
                controller.deleteEquipment();
                break;
            case "location":
                controller.deleteLocation();
                break;
            case "bike type":
                controller.deleteBikeType();
                break;
            case "equipment type":
                controller.deleteEquipmentType();
                break;
            default:
                break;
        }

        super.close();
    }
}
