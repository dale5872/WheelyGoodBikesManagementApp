package App.FXControllers;

import App.Classes.Type;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @FXML private StackPane dragBox;
    @FXML private ImageView imageDisplay;
    @FXML private TextField imageUrlField;

    @FXML private TextField priceField;

    @FXML private Label priceWarning;

    @FXML private Button confirmBtn;

    /**
     * Set whether we're adding/updating a bike type or equipment type
     * @param isBikeType
     */
    public void setIsBike(Boolean isBikeType){
        this.isBikeType = isBikeType;
    }

    /**
     * Set the existing bike/equipment type
     * @param type
     */
    public void setExistingType(Type type){
        this.existingType = type;

        nameField.setText(this.existingType.getName());
        displayImage(this.existingType.getImage());
        priceField.setText(Double.toString(this.existingType.getPrice()));

        confirmBtn.setText("Update");
    }

    /**
     * Displays an image from a given path or URL
     * @param imagePath The path/URL to the image
     */
    private void displayImage(String imagePath){
        Image image;

        boolean isUrl = (imagePath.startsWith("http:")) || (imagePath.startsWith("https:"));

        if(isUrl){
            image = new Image(imagePath);
        }else{
            File file = new File(imagePath);
            image = new Image(file.toURI().toString());
        }

        imageDisplay.setImage(image);
        imageUrlField.setText(imagePath);
    }

    /**
     * Opens an image browser and displays the chosen image on the form
     */
    @FXML
    protected void chooseImage(){
        String title;
        if(existingType == null){
            title = "New Type Image";
        }else{
            title = "Edit Type Image";
        }

        ImageBrowser imageBrowser = new ImageBrowser(title);
        imageBrowser.showBrowser(stage);
        String imagePath = imageBrowser.getImagePath();

        if(imagePath != null) {
            displayImage(imagePath);
        }
    }

    /**
     * Creates/updates the type and passes it back to the parent controller
     */
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
     * Checks that all fields have a value and that the price is of a valid format
     * @return
     */
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

            displayImage(filename);
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
}
