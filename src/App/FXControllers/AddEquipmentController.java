package App.FXControllers;

import App.Classes.Equipment;
import App.Classes.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    @FXML private TextField quantityField;
    @FXML private Label numberWarning;

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

            /* Get quantity */
            int quantity = Integer.parseInt(this.quantityField.getText());

            /* Create new equipment object */
            Equipment equipment = new Equipment();
            equipment.setCategory(categoryName);
            equipment.setTypeName(typeName);
            equipment.setTypeID(typeID);
            equipment.setLocation(loc);
            equipment.setStatus("Available");

            for(int counter = 0; counter < quantity; counter++) {
                /* Pass back to Operator system */
                OperatorSystemController controller = (OperatorSystemController) super.parentController;
                if (categoryName.equals("Bike")) {
                    controller.addBike(equipment);
                } else {
                    controller.addEquipment(equipment);
                }
            }

            super.close();
        }
    }

    /**
     * Checks that no fields are blank and that the quantity is numeric, highlight warning labels as appropriate
     * @return
     */
    @SuppressWarnings("Duplicates")
    private boolean validateInput(){
        boolean hasBlankFields = checkForBlankFields();
        boolean quantityIsNumeric = checkQuantityIsNumeric();

        /* Highlight "All fields required" label if there are blank fields, set to normal otherwise */
        if(hasBlankFields){
            allFieldsWarning.getStyleClass().add("warningLabel");
        }else{
            allFieldsWarning.getStyleClass().remove("warningLabel");
        }

        if(quantityIsNumeric){
            numberWarning.setVisible(false);
        }else{
            numberWarning.setVisible(true);
        }

        return !hasBlankFields && quantityIsNumeric;
    }

    /**
     * Checks for any blank fields
     * @return TRUE if there are blank fields, FALSE if there are none
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

        if(quantityField.getText().equals("")){
            return true;
        }

        return false;
    }

    /**
     * Checks that the value entered into the quantity field is numeric
     * @return TRUE if the quantity is numeric, FALSE if not
     */
    private boolean checkQuantityIsNumeric(){
        try{
            Integer.parseInt(this.quantityField.getText());
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}
