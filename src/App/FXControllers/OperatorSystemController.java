//TODO: Refactor to allow EmployeeAccount and Accounts
//TODO: Refactor to avoid duplicate code for inputting data into tables on different views

package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Equipment;
import App.Classes.Location;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the operator system
 * This class contains methods for all event handling on the operator system
 */
public class OperatorSystemController {
    @FXML private ToggleButton accountsTabButton; //Gets the accounts tab button object
    @FXML private ToggleButton bikesTabButton; //Gets the bikes tab button object
    @FXML private ToggleButton locationsTabButton; //Gets locations yellow tab button object
    @FXML private ToggleButton rentalsTabButton; //Gets the rentals tab button object
    @FXML private ToggleButton userTabButton; //Gets the user's account tab button
    private List<ToggleButton> tabButtons; //List to store all tab buttons

    @FXML private AnchorPane accountsTab; //Gets the accounts tab object
    @FXML private AnchorPane bikesTab; //Gets the bikes tab object
    @FXML private AnchorPane locationsTab; //Gets the locations tab object
    @FXML private AnchorPane rentalsTab; //Gets the rentals tab object
    @FXML private AnchorPane userTab; //Gets the user's account tab object
    private List<AnchorPane> tabs; //List to store all tabs;

    private static EmployeeAccount employee;

    /** ACCOUNTS TAB */
    //Table
    @FXML private TableColumn accountsID;
    @FXML private TableColumn accountsUsername;
    @FXML private TableColumn accountsFirstName;
    @FXML private TableColumn accountsLastName;
    @FXML private TableColumn accountsEmail;
    @FXML private TableColumn accountsPhoneNumber;
    @FXML private TableColumn accountsAccountType;
    @FXML private TableColumn accountsLocation;
    @FXML private TableView accountsTable;

    //Search Properties
    @FXML private RadioButton managersRadio;
    @FXML private RadioButton operatorsRadio;
    @FXML private RadioButton allRadio;

    /** Equipment Tab **/
    //Table
    @FXML private TableColumn equipmentID;
    @FXML private TableColumn equipmentName;
    @FXML private TableColumn equipmentLocation;
    @FXML private TableColumn equipmentType;
    @FXML private TableColumn equipmentPrice;
    @FXML private TableColumn equipmentStatus;
    @FXML private TableView equipmentTable;

    /** Locations Tab **/
    //Table
    @FXML private TableColumn locationsID;
    @FXML private TableColumn locationsName;
    @FXML private TableView locationsTable;

    /** Rentals Tab **/
    //Table
    @FXML private TableColumn rentalsID;
    @FXML private TableColumn rentalsEquipmentID;
    @FXML private TableColumn rentalsEquipmentName;
    @FXML private TableColumn rentalsEquipmentPrice;
    @FXML private TableColumn rentalsStartTime;
    @FXML private TableColumn rentalsReturnTime;
    @FXML private TableColumn rentalsTotalPrice;
    @FXML private TableColumn rentalsLocation;
    @FXML private TableColumn rentalsStatus;
    @FXML private TableView rentalsTable;


    public void setEmployee(EmployeeAccount e) {
        this.employee = e;
    }

    /**
     * The initialise method is called when the form first loads
     */
    @SuppressWarnings("Duplicates")
    public void initialize(){
        /* Initialise the tabButtons list and add all tab buttons to it */
        tabButtons = new ArrayList<>();
        tabButtons.add(accountsTabButton);
        tabButtons.add(bikesTabButton);
        tabButtons.add(locationsTabButton);
        tabButtons.add(rentalsTabButton);
        tabButtons.add(userTabButton);

        /* Initialise the tabs list and add all tabs to it */
        tabs = new ArrayList<>();
        tabs.add(accountsTab);
        tabs.add(bikesTab);
        tabs.add(locationsTab);
        tabs.add(rentalsTab);
        tabs.add(userTab);
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
     * Finds the parameters selected by the user and loads the data
     * based on those parameters. Parameters are selected through
     * Radio Buttons
     * @param e
     */
    @FXML
    protected void loadAccounts(ActionEvent e) throws InvalidParametersException{
        String queryString = "SELECT employees.employeeID, user.username,  employee_info.firstName, employee_info.lastName, \n" +
                "       employee_info.workEmail, employee_info.workTel, account_types.type, location.locationID, location.name AS 'location'\n" +
                "FROM user\n" +
                "INNER JOIN employees ON user.userID = employees.userID\n" +
                "INNER JOIN employee_info ON employees.employeeID = employee_info.employeeID\n" +
                "INNER JOIN account_types ON user.accountTypeID = account_types.accountTypeID\n" +
                "INNER JOIN location ON employees.location = location.locationID\n";

        if(managersRadio.isSelected()) {
            queryString = queryString + "WHERE account_types.type = 'Manager';";
            System.out.println(queryString);
        } else if(operatorsRadio.isSelected()) {
            queryString = queryString + "WHERE account_types.type = 'Operator';";
        } else if(allRadio.isSelected()) {
            //Nothing
        } else {
            throw new InvalidParametersException("One parameter must be selected!");
        }

        ObservableList<EmployeeAccount> accounts = DataFetcher.accounts(queryString);

        accountsID.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("employeeID")
        );
        accountsUsername.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("username")
        );
        accountsFirstName.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("firstName")
        );
        accountsLastName.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("lastName")
        );
        accountsEmail.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("email")
        );
        accountsPhoneNumber.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("phoneNumber")
        );
        accountsAccountType.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("accType")
        );
        accountsLocation.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("LocationName")
        );

        accountsTable.setItems(accounts);

    }

    /**
     * Loads data from the SQL database into the equipment table
     * @param e
     */
    @FXML
    protected void loadEquipment(ActionEvent e) {
        String queryString = "SELECT equipment_stock.equipmentID, equipment_stock.equipmentType, equipment_stock.location, location.name,\n" +
                "       equipment_stock.equipmentStatus, equipment_type.pricePerHour\n" +
                "FROM equipment_stock\n" +
                "INNER JOIN location ON equipment_stock.location = location.locationID\n" +
                "INNER JOIN equipment_type ON equipment_stock.equipmentType = equipment_type.equipmentType;";

        ObservableList<Equipment> equipment = DataFetcher.equipment(queryString);

        equipmentID.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("ID")
        );
        equipmentName.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("Type")
        );
        equipmentLocation.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("")
        );
        equipmentType.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("")
        );
        equipmentPrice.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("Price")
        );
        equipmentStatus.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("Status")
        );

        equipmentTable.setItems(equipment);
    }

    @FXML
    protected void loadLocations(ActionEvent e) {
        String queryString = "";

        ObservableList<Location> locations = DataFetcher.locations(queryString);

        locationsID.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("locationID")
        );
        locationsName.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("Name")
        );

        locationsTable.setItems(locations);
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
