package App.FXControllers;

import App.Classes.Equipment;
import App.Classes.Location;
import App.Classes.Type;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class AddEquipmentController extends PopupController{
    private ObservableList<Type> bikeTypes;
    private ObservableList<Type> equipmentTypes;
    private ObservableList<Location> locations;

    /* Controls */
    @FXML private Label allFieldsWarning;

    @FXML private ComboBox category;
    @FXML private ComboBox type;
    @FXML private ComboBox equipmentLocation;

    @FXML private TextField quantityField;
    @FXML private Label numberWarning;

    public void setDropdownValues(ObservableList<Type> bikeTypes, ObservableList<Type> equipmentTypes,
                                  ObservableList<Location> locations){
        this.bikeTypes = bikeTypes;
        this.equipmentTypes = equipmentTypes;
        this.locations = locations;

        /* Set category dropdown values */
        ObservableList<String> categoryOptions = FXCollections.observableArrayList("Bike", "Other Equipment");
        category.setItems(categoryOptions);
        category.getSelectionModel().selectFirst();

        /* Set the location dropdown */
        ObservableList<String> locationOptions = OptionsList.createLocationNameList(this.locations);
        equipmentLocation.setItems(locationOptions);
    }

    @FXML
    @SuppressWarnings("Duplicates")
    protected void changeCategory(){
        boolean showingBikes = category.getSelectionModel().getSelectedItem().equals("Bike");
        ObservableList<String> typeOptions;

        if(showingBikes){
            typeOptions = OptionsList.createTypeNameList(bikeTypes);
        }else { //Showing other equipment
            typeOptions = OptionsList.createTypeNameList(equipmentTypes);
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

            /* Get type */
            String typeName = (String) type.getSelectionModel().getSelectedItem();
            Type type;
            if(categoryName.equals("Bike")){
                type = OptionsList.findTypeByName(bikeTypes, typeName);
            }else{
                type = OptionsList.findTypeByName(equipmentTypes, typeName);
            }

            /* Get location */
            String locName = (String) equipmentLocation.getSelectionModel().getSelectedItem();
            Location loc = OptionsList.findLocationByName(locations, locName);

            /* Get quantity */
            int quantity = Integer.parseInt(this.quantityField.getText());

            /* Create new equipment object */
            Equipment equipment = new Equipment();
            equipment.setCategory(categoryName);
            equipment.setType(type);
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

        /* Show the "Quantity must be a number" warning if the quantity is not a number, hide otherwise */
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
