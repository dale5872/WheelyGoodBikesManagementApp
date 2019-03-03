package App.FXControllers;

import App.Classes.Location;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class AddEditLocationController extends PopupController{
    private Location location;

    /* Controls */
    @FXML private Label warning;
    @FXML private TextField name;
    @FXML private Button confirmBtn;

    /**
     * Passes in an existing location to be edited, putting this location's name in the text box
     * Also sets the text of the confirmation button
     * @param location
     */
    public void setLocation(Location location){
        this.location = location;

        name.setText(this.location.getName());
        confirmBtn.setText("Update");
    }

    /**
     * Adds a new location or updates an existing one
     */
    @FXML
    protected void confirm(){
        if(validateInput() == true){
            if (location == null) { //No existing location, so we're adding a new one
                super.parentController.addLocation(name.getText());
            } else { //Updating an existing location
                location.setName(name.getText());

                super.parentController.editLocation(location);
            }

            super.close();
        }
    }

    /**
     * Checks that the location name field has not been blank, highlights a warning label if it has
     * @return
     */
    private boolean validateInput(){
        if(name.getText().equals("")){
            warning.setVisible(true);
            return false;
        }else{
            warning.setVisible(false);
            return true;
        }
    }
}
