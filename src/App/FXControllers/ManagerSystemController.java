package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the manager system
 * This class contains methods for all event handling on the manager system
 */
public class ManagerSystemController {
    @FXML private ToggleButton bikesTabButton; //Gets the bikes tab button object
    @FXML private ToggleButton penaltiesTabButton; //Gets the penalties tab button object
    @FXML private ToggleButton reportsTabButton; //Gets the reports tab button object
    @FXML private ToggleButton userTabButton; //Gets the user's account tab button object
    private List<ToggleButton> tabButtons; //List to store all tab buttons

    @FXML private AnchorPane bikesTab; //Gets the bikes tab object
    @FXML private AnchorPane penaltiesTab; //Gets the penalties tab object
    @FXML private AnchorPane reportsTab; //Gets the reports tab object
    @FXML private AnchorPane userTab; //Gets the user tab object
    private List<AnchorPane> tabs; //List to store all tabs;

    @FXML private ComboBox penaltiesViewOption;
    @FXML private FlowPane unsolvedPenalties;
    @FXML private FlowPane solvedPenalties;

    /** Equipment Table **/
    @FXML private TableView equipmentTable;
    @FXML private TableColumn equipmentID;
    @FXML private TableColumn equipmentType;
    @FXML private TableColumn equipmentLocation;
    @FXML private TableColumn equipmentPrice;
    @FXML private TableColumn equipmentStatus;

    /** Account Tab **/
    @FXML private Label userAccountID;
    @FXML private Label userAccountUsername;
    @FXML private Label userAccountName;
    @FXML private Label userAccountEmail;
    @FXML private Label userAccountPhone;
    @FXML private Label userAccountType;
    @FXML private Label userAccountLocation;

    private static EmployeeAccount employee;

    public void setEmployee(EmployeeAccount e) {
        this.employee = e;

        //set account labels
        userAccountID.setText("" + employee.getEmployeeID());
        userAccountUsername.setText(employee.getUsername());
        userAccountName.setText(employee.getFirstName() + " " + employee.getLastName());
        userAccountEmail.setText(employee.getEmail());
        userAccountPhone.setText(employee.getPhoneNumber());
        userAccountType.setText(employee.getAccType());
        userAccountLocation.setText(employee.getLocationName());

        /**
         * TODO Manager Automatic Loading
         * BODY Implement the automatic loading for Manager view with PHP script
         */
        /** Run these after employee set
        loadEquipment(null);
         */
    }

    /**
     * The initialise method is called when the form first loads
     */
    @SuppressWarnings("Duplicates")
    public void initialize(){
        /* Initialise the tabButtons list and add all tab buttons to it */
        tabButtons = new ArrayList<>();
        tabButtons.add(bikesTabButton);
        tabButtons.add(penaltiesTabButton);
        tabButtons.add(reportsTabButton);
        tabButtons.add(userTabButton);

        /* Initialise the tabs list and add all tabs to it */
        tabs = new ArrayList<>();
        tabs.add(bikesTab);
        tabs.add(penaltiesTab);
        tabs.add(reportsTab);
        tabs.add(userTab);

        //Set the first tab as active
        TabSwitcher.setToFirstTab(tabButtons, tabs);

        //TEMP? Add options to penalties view drop down
        ObservableList<String> options = FXCollections.observableArrayList("Unsolved Penalties", "Solved Penalties");
        penaltiesViewOption.setItems(options);
        penaltiesViewOption.getSelectionModel().selectFirst();
    }

    /**
     * Loads in all the equipment in the current managers location
     * @param e ActionEvent object
     */
    @FXML
    protected void loadEquipment(ActionEvent e) {
        try {
            ObservableList<Equipment> equipment = DataFetcher.equipment(this.employee.getLocation());

            equipmentID.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("ID")
            );
            equipmentType.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("TypeName")
            );
            equipmentLocation.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("LocationName")
            );
            equipmentPrice.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("Price")
            );
            equipmentStatus.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("Status")
            );

            equipmentTable.setItems(equipment);
        } catch (EmptyDatasetException exc) {
            return;
        }

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

    @FXML
    protected void switchPenaltiesView(){
        if(penaltiesViewOption.getSelectionModel().getSelectedIndex() == 0){
            unsolvedPenalties.setVisible(true);
            solvedPenalties.setVisible(false);
        }else{
            unsolvedPenalties.setVisible(false);
            solvedPenalties.setVisible(true);
        }
    }

    /**
     * Logs the user out
     */
    @SuppressWarnings("Duplicates")
    @FXML
    protected void logout(ActionEvent e) {
        this.employee = null;
        try {
            /* Create and show the log in window */
            Parent root = FXMLLoader.load(getClass().getResource("../FXML/LogIn.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Wheely Good Bikes");
            primaryStage.setScene(scene);
            primaryStage.show();

            Stage thisScreen = (Stage) userTabButton.getScene().getWindow();
            thisScreen.close();

        }catch (IOException ex){
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            System.exit(2);
        }
    }
}
