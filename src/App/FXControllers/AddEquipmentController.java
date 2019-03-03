package App.FXControllers;

import App.Classes.Equipment;
import App.Classes.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.HashMap;

public class AddEquipmentController extends PopupController{
    private static HashMap<String, String> bikeTypes;
    private static HashMap<String, String> equipmentTypes;
    private HashMap<String, String> locations;

    /* Controls */
    @FXML private Label allFieldsWarning;

    @FXML private ComboBox category;
    @FXML private ComboBox type;
    @FXML private ComboBox equipmentLocation;

    public void setDropdownValues(HashMap<String, String> bikeTypes, HashMap<String, String> equipmentTypes,
                                  HashMap<String, String> locations){
        this.bikeTypes = bikeTypes;
        this.equipmentTypes = equipmentTypes;
        this.locations = locations;

        /* Set category dropdown values */
        ObservableList<String> categoryOptions = FXCollections.observableArrayList("Bike", "Other Equipment");
        category.setItems(categoryOptions);
        category.getSelectionModel().selectFirst();

        /* Set the location dropdown */
        ObservableList<String> locationOptions = OptionsListCreator.createList(this.locations);
        equipmentLocation.setItems(locationOptions);
    }

    @FXML
    @SuppressWarnings("Duplicates")
    protected void changeCategory(){
        boolean showingBikes = category.getSelectionModel().getSelectedItem().equals("Bike");
        ObservableList<String> typeOptions;

        if(showingBikes){
            typeOptions = OptionsListCreator.createList(bikeTypes);
        }else { //Showing other equipment
            typeOptions = OptionsListCreator.createList(equipmentTypes);
        }

        type.setItems(typeOptions);
        type.getSelectionModel().selectFirst();
    }

    @FXML
    protected void confirm(){
        if(validateInput() == true){
            /* Get category */
            String categoryName = (String) category.getSelectionModel().getSelectedItem();
            if(categoryName.equals("Other Equipment")){
                categoryName = "Equipment";
            }

            /* Get type name and ID */
            String typeName = (String) type.getSelectionModel().getSelectedItem();
            int typeID;
            if(categoryName.equals("Bike")){
                typeID = Integer.parseInt(bikeTypes.get(type.getValue()));
            }else{
                typeID = Integer.parseInt(equipmentTypes.get(type.getValue()));
            }

            /* Get location */
            String locName = (String) equipmentLocation.getSelectionModel().getSelectedItem();
            int locID = Integer.parseInt(locations.get(locName));
            Location loc = new Location(locID, locName);

            /* Create new equipment object */
            Equipment equipment = new Equipment();
            equipment.setCategory(categoryName);
            equipment.setTypeName(typeName);
            equipment.setTypeID(typeID);
            equipment.setLocation(loc);
            equipment.setStatus("Available");

            /* Pass back to Operator system */
            if(categoryName.equals("Bike")) {
                super.parentController.addBike(equipment);
            }else{
                super.parentController.addEquipment(equipment);
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
        if(category.getSelectionModel().isEmpty()){
            return true;
        }

        if(type.getSelectionModel().isEmpty()){
            return true;
        }

        if(equipmentLocation.getSelectionModel().isEmpty()){
            return true;
        }

        return false;
    }
}
