package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Location;
import App.Classes.Type;
import App.JavaFXLoader;
import DatabaseConnector.InsertFailedException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class SystemController extends Controller{
    protected EmployeeAccount employee;

    protected static HashMap<String, String> accountTypes;

    protected static ObservableList<Type> bikeTypes;
    protected static ObservableList<Type> equipmentTypes;
    protected static ObservableList<Location> locations;

    /* Controls */
    protected List<ToggleButton> tabButtons; //List to store all tab buttons
    protected List<AnchorPane> tabs; //List to store all tabs;

    @FXML private AnchorPane parentPane;

    /** Account Profile Tab **/
    @FXML protected Label userAccountID;
    @FXML protected Label userAccountUsername;
    @FXML protected Label userAccountName;
    @FXML protected Label userAccountType;
    @FXML protected Label userAccountLocation;

    @FXML protected VBox contactDetailsNonEditable;
    @FXML protected Label userAccountEmail;
    @FXML protected Label userAccountPhone;

    @FXML protected VBox contactDetailsEditable;
    @FXML protected TextField userAccountPhoneTextbox;
    @FXML protected TextField userAccountEmailTextbox;

    @FXML protected HBox changeContactContainer;
    @FXML protected Button contactDetailsViewBtn;

    @FXML protected HBox confirmContactContainer;

    @FXML private Label dragDropLabel;
    @FXML private ImageView profileImageView;

    public SystemController(){
        super();

        accountTypes = DataFetcher.getAccountTypes();

        bikeTypes = loadBikeTypes();
        equipmentTypes = loadEquipmentTypes();
        locations = loadLocations();
    }

    /**
     * Loads the list of bike types from the database.
     * @return An ObservableList of Type objects
     */
    protected ObservableList<Type> loadBikeTypes(){
        try{
            return DataFetcher.getBikeTypes("");
        }catch(EmptyDatasetException e){
            return null;
        }
    }

    /**
     * Loads the list of equipment types from the database.
     * @return An ObservableList of Type objects
     */
    protected ObservableList<Type> loadEquipmentTypes() {
        try{
            return DataFetcher.getEquipmentTypes("");
        }catch(EmptyDatasetException e){
            return null;
        }
    }

    protected ObservableList<Location> loadLocations(){
        try{
            return DataFetcher.getLocations("search=");
        }catch(EmptyDatasetException e){
            return null;
        }
    }

    public void setEmployee(EmployeeAccount e) {
        this.employee = e;

        //set account labels
        userAccountID.setText("" + employee.getEmployeeID());
        userAccountUsername.setText(employee.getUsername());
        userAccountName.setText(employee.getFirstName() + " " + employee.getLastName());
        userAccountEmail.setText(employee.getEmail());
        userAccountEmailTextbox.setText(employee.getEmail());
        userAccountPhone.setText(employee.getPhoneNumber());
        userAccountPhoneTextbox.setText(employee.getPhoneNumber());
        userAccountType.setText(employee.getAccType());
        userAccountLocation.setText(employee.getLocation().getName());

        //set profile picture
        changeImageView();
    }

    /**
     * Enable the window
     */
    public void enable(){
        parentPane.setDisable(false);
    }

    /**
     * Disable the window
     */
    public void disable(){
        parentPane.setDisable(true);
    }

    /**
     * Handles switching the view of the user's contact details, between editable and non editable
     * @param e
     */
    @FXML
    protected void switchContactDetailsView(ActionEvent e){
        if(e.getSource() == contactDetailsViewBtn){
            contactDetailsNonEditable.setVisible(false);
            changeContactContainer.setVisible(false);

            contactDetailsEditable.setVisible(true);
            confirmContactContainer.setVisible(true);
        }else{
            contactDetailsNonEditable.setVisible(true);
            changeContactContainer.setVisible(true);

            contactDetailsEditable.setVisible(false);
            confirmContactContainer.setVisible(false);
        }
    }

    /**
     * Cancels changing the logged in user's contact details, setting the textboxes back to the stored value and switching back to non-editable view
     * @param e
     */
    @FXML
    protected void cancelChangeContact(ActionEvent e){
        userAccountEmailTextbox.setText(employee.getEmail());
        userAccountPhoneTextbox.setText(employee.getPhoneNumber());

        switchContactDetailsView(e);
    }

    /**
     * Shows the dialog to change the password of the active account
     */
    @FXML
    protected void showChangePasswordDialog(){
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("ChangePassword", "Change Password", false);

        disable();

        ChangePasswordController controller = (ChangePasswordController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
        controller.setAlwaysOnTop(true);
        controller.setAccount(this.employee);
    }

    /**
     * Handles switching between tabs
     * @param e the ActionEvent from the button click
     */
    @FXML
    protected void switchTab(ActionEvent e){
        ToggleButton clickedButton = (ToggleButton) e.getSource();
        TabSwitcher.switchTab(tabButtons, tabs, clickedButton);
    }

    /**
     * Logs the user out
     */
    @FXML
    protected void logout(ActionEvent e) {
        new JavaFXLoader().loadNewFXWindow("LogIn", "Wheely Good Bikes", false);

        employee = null;
        super.stage.close();
    }

    @FXML
    @SuppressWarnings("Duplicates")
    protected void dragOver(DragEvent e) {
        showDragDropMessage();

        Dragboard board = e.getDragboard();

        boolean isAccepted = board.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || board.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || board.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

        if(board.hasFiles()) {
            if(isAccepted) {
                e.acceptTransferModes(TransferMode.LINK);
            }
        }

        e.consume();
    }

    @FXML
    protected void droppedImage(DragEvent e) {
        profileImageView.setOpacity(0.00);
        Boolean success = false;
        System.out.println("Dropped");

        Dragboard board = e.getDragboard();
        if(board.hasFiles()) {
            File file = board.getFiles().get(0);
            String filename = file.getAbsolutePath();

            success = updateProfilePicture(filename);
        } else {
            System.err.println("No files dropped!");
        }

        e.setDropCompleted(success);
        e.consume();
        profileImageView.setOpacity(1.00);
    }

    @FXML
    protected void showDragDropMessage() {
        dragDropLabel.setVisible(true);
        profileImageView.setOpacity(0.25);
    }

    @FXML
    protected void removeDragDropMessage() {
        dragDropLabel.setVisible(false);
        profileImageView.setOpacity(1.00);
    }

    private void changeImageView() {
        String imagePath = this.employee.getProfilePicture();

        if(imagePath != null){
            Image image = new Image(imagePath);

            profileImageView.setFitHeight(200);
            profileImageView.setFitWidth(200);
            profileImageView.setImage(image);
        }
    }

    /**
     * Shows an image browser to select a new profile picture
     */
    @FXML
    protected void selectNewProfilePicture(){
        ImageBrowser imageBrowser = new ImageBrowser("Change Profile Picture");
        imageBrowser.showBrowser(stage);
        String filePath = imageBrowser.getImagePath();

        if(filePath != null) {
            updateProfilePicture(filePath);
        }
    }

    /**
     * Updates the profile picture from a given file path
     * @param filePath The file path of the new image
     */
    private boolean updateProfilePicture(String filePath){
        String imageUrl;

        /* Attempt to upload image file - show error and cancel if it can't be uploaded */
        try{
            imageUrl = DataFetcher.uploadFile(filePath);
        }catch(InsertFailedException ex){
            return false;
        }

        /* Attempt to update account - show error and cancel if it can't be updated */
        try {
            /* Get the account type and map it to its system index */
            String accountType = employee.getAccType();
            int accountTypeIndex = Integer.parseInt(accountTypes.get(accountType));

            /* Update account */
            this.employee.setProfilePicture(imageUrl);
            DataFetcher.updateAccount(employee, employee, accountTypeIndex);
        }catch(InsertFailedException ex){
            return false;
        }

        changeImageView();
        return true;
    }
}
