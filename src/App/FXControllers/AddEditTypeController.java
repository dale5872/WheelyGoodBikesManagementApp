package App.FXControllers;

import App.Classes.Type;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;

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

    @FXML private StackPane dragBox;

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

    /**
     * Gets the filename from the dragged file and updates the filename box
     * @param e DragEvent
     */
    @FXML
    protected void getFilename(DragEvent e) {

        Boolean success = false;

        Dragboard board = e.getDragboard();
        if(board.hasFiles()) {
            File file = board.getFiles().get(0);
            String filename = file.getAbsolutePath();

            imageUrlField.setText(filename);

            success = true;

        } else {
            System.err.println("No files dropped!");
        }

        e.setDropCompleted(success);
        e.consume();
    }

    @FXML
    protected void dragOver(DragEvent e) {
        dragBox.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.WHITE,2,0,0,0));

        Dragboard board = e.getDragboard();

        boolean isAccepted = board.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || board.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || board.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

        if(board.hasFiles()) {
            if(isAccepted) {
                e.acceptTransferModes(TransferMode.LINK);
            }
        } else {
            e.consume();
        }
    }

    @FXML
    protected void dragExited(DragEvent e) {
        dragBox.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.WHITE,0,0,0,0));
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
