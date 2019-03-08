package App.FXControllers;

import App.Classes.Equipment;
import App.Classes.Location;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.HashMap;

public class EditEquipmentController extends PopupController{
    private Equipment equipment;
    private HashMap<String, String> locations;

    /* Controls */
    @FXML private Label equipmentInfo;
    @FXML private Label allFieldsWarning;

    @FXML private ComboBox equipmentLocation;
    @FXML private ComboBox statusCombo;

    public void setDropdownValues(HashMap<String, String> locations){
        this.locations = locations;

        /* Set the location dropdown */
        ObservableList<String> locationOptions = OptionsListCreator.createList(this.locations);
        equipmentLocation.setItems(locationOptions);

        /* Set the status dropdown */
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Available", "Booked", "Damaged");
        statusCombo.setItems(statusOptions);
    }

    public void setEquipment(Equipment equipment){
        this.equipment = equipment;

        equipmentInfo.setText("ID: " + this.equipment.getID()
                + " | Category: " + this.equipment.getCategory()
                + " | Type: " + this.equipment.getTypeName());

        equipmentLocation.setValue(this.equipment.getLocationName());
        statusCombo.setValue(this.equipment.getStatus());
    }

    @FXML
    protected void confirm(){
        if(validateInput() == true){
            /* Set location */
            String locName = (String) equipmentLocation.getSelectionModel().getSelectedItem();
            int locID = Integer.parseInt(locations.get(locName));
            Location loc = new Location(locID, locName);
            equipment.setLocation(loc);

            /* Set status */
            String status = (String) statusCombo.getSelectionModel().getSelectedItem();
            equipment.setStatus(status);

            /* Pass back to Operator System */
            if(equipment.getCategory().equals("Bike")){
                ((OperatorSystemController) super.parentController).updateBike(equipment);
            }else{
                ((OperatorSystemController) super.parentController).updateEquipment(equipment);
            }

            super.close();
        }
    }

    /**
     * Checks that no fields are blank, and highlight warning label if there are
     * @return
     */
    @SuppressWarnings("Duplicates")
    private boolean validateInput(){
        boolean hasBlankFields = checkForBlankFields();

        /* Highlight "All fields required" label if there are blank fields, set to normal otherwise */
        if(hasBlankFields){
            allFieldsWarning.getStyleClass().add("warningLabel");
        }else{
            allFieldsWarning.getStyleClass().remove("warningLabel");
        }

        return !hasBlankFields;
    }

    /**
     * Checks for any blank fields
     * Returns TRUE if there are blank fields, FALSE if there are none
     * @return
     */
    @SuppressWarnings("Duplicates")
    private boolean checkForBlankFields(){
        if(equipmentLocation.getSelectionModel().isEmpty()){
            return true;
        }

        if(statusCombo.getSelectionModel().isEmpty()){
            return true;
        }

        return false;
    }
}
