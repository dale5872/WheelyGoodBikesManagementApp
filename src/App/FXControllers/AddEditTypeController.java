package App.FXControllers;

import App.Classes.Type;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddEditTypeController extends PopupController{
    private boolean isBikeType;
    private Type existingType;

    /* Controls */
    @FXML private Label allFieldsWarning;

    @FXML private TextField nameField;
    @FXML private TextField imageUrlField;
    @FXML private TextField priceField;

    @FXML private Label priceWarning;

    @FXML private Button confirmBtn;

    public void setIsBike(Boolean isBikeType){
        this.isBikeType = isBikeType;
    }

    public void setExistingType(Type type){
        this.existingType = type;

        nameField.setText(this.existingType.getName());
        imageUrlField.setText(this.existingType.getImage());
        priceField.setText(Double.toString(this.existingType.getPrice()));

        confirmBtn.setText("Update");
    }

    @FXML
    protected void confirm(){
        if(validateInput() == true){
            Type type;

            /* Instantiate type */
            if(existingType == null){ //Creating a new type
                type = new Type();
            }else{
                type = existingType;
            }

            /* Set new values */
            type.setName(nameField.getText());
            type.setImage(imageUrlField.getText());
            type.setPrice(Double.parseDouble(priceField.getText()));

            /* Pass back to parent controller */
            OperatorSystemController controller = (OperatorSystemController) super.parentController;
            if(isBikeType){
                if(existingType == null){
                    controller.addBikeType(type);
                }else{
                    controller.updateBikeType(type);
                }
            }else{
                if(existingType == null) {
                    controller.addEquipmentType(type);
                }else{
                    controller.updateEquipmentType(type);
                }
            }

            super.close();
        }
    }

    private boolean validateInput(){
        boolean hasBlankFields = checkForBlankFields();
        boolean priceIsDouble = checkPriceIsDouble();

        /* Highlight "All fields required" label if there are blank fields, set to normal otherwise */
        if(hasBlankFields){
            allFieldsWarning.getStyleClass().add("warningLabel");
        }else{
            allFieldsWarning.getStyleClass().remove("warningLabel");
        }

        /* Show the "Price must be valid" warning if the quantity is not a number, hide otherwise */
        if(priceIsDouble){
            priceWarning.setVisible(false);
        }else{
            priceWarning.setVisible(true);
        }

        return !hasBlankFields && priceIsDouble;
    }

    /**
     * Checks for any blank fields
     * @return TRUE if there are blank fields, FALSE if there are none
     */
    @SuppressWarnings("Duplicates")
    private boolean checkForBlankFields(){
        if(nameField.getText().equals("")){
            return true;
        }

        if(imageUrlField.getText().equals("")){
            return true;
        }

        if(priceField.getText().equals("")){
            return true;
        }

        return false;
    }

    /**
     * Checks that the value entered into the price field can be converted to a double
     * @return TRUE if the quantity is numeric, FALSE if not
     */
    private boolean checkPriceIsDouble(){
        try{
            Double.parseDouble(this.priceField.getText());
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}
