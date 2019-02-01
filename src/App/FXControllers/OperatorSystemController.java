//TODO: Refactor to allow EmployeeAccount and Accounts
//TODO: Refactor to avoid duplicate code for inputting data into tables on different views

package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Equipment;
import App.Classes.Location;
import DatabaseConnector.InsertFailedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

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

    //Add Account Properties
    @FXML private TextField accountsNewAccountUsername;
    @FXML private TextField accountsNewAccountPassword;
    @FXML private TextField accountsNewAccountFirstName;
    @FXML private TextField accountsNewAccountLastName;
    @FXML private TextField accountsNewAccountEmail;
    @FXML private TextField accountsNewAccountPhone;
    @FXML private ComboBox accountsNewAccountType;
    @FXML private ComboBox accountsNewAccountLocation;

    //Edit Account Properties
    @FXML private VBox editAccountVBox;
    @FXML private TextField accountsEditAccountUsername;
    @FXML private TextField accountsEditAccountFirstName;
    @FXML private TextField accountsEditAccountLastName;
    @FXML private TextField accountsEditAccountEmail;
    @FXML private TextField accountsEditAccountPhone;
    @FXML private ComboBox accountsEditAccountType;
    @FXML private ComboBox accountsEditAccountLocation;

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

    //fields
    private static EmployeeAccount employee;
    private static HashMap<String, String> accounts;
    private static HashMap<String, String> locations;

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

        //Load in data for adding / editing accounts
        try {
            loadAccounts(null);
        } catch (InvalidParametersException e) {
            //Some real issue here...
            //TODO: MessageBox. FATAL ERROR AND CLOSES.
        }

        //Load in account types and location dropdown boxes
        accounts = DataFetcher.getDropdownValues("accountTypes");
        locations = DataFetcher.getDropdownValues("locations");
        setValues();

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
                "       employee_info.workEmail, employee_info.workTel, account_types.type, location.locationID, location.name AS 'location', user.userID\n" +
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
     * Method for adding a new account with the data specified by the user
     * @param e ActionEvent object
     */
    @FXML
    protected void addAccount(ActionEvent e) {
        String value = (String)accountsNewAccountType.getValue();
        int accountType = Integer.parseInt(accounts.get(value));

        EmployeeAccount acc = new EmployeeAccount();
        acc.setUsername(accountsNewAccountUsername.getText());
        acc.setFirstName(accountsNewAccountFirstName.getText());
        acc.setLastName(accountsNewAccountLastName.getText());
        acc.setEmail(accountsNewAccountEmail.getText());
        acc.setPhoneNumber(accountsNewAccountPhone.getText());
        acc.setAccType((String)accountsNewAccountType.getValue());
        acc.setLocation((String)accountsNewAccountLocation.getValue());

        try {
            DataFetcher.addAccount(acc, accountsNewAccountPassword.getText(), accountType);
        } catch (InsertFailedException ex) {
            //TODO: MessageBox
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            //TODO: Exception handling
            System.out.println(ex.getMessage());
        }

        //update table
        accountsTable.getItems().add(acc);
    }

    /**
     * Updates the account selected in the table with the information
     * specified by the user in the input form
     * @param e ActionEvent object
     */
    @FXML
    protected void updateAccount(ActionEvent e) {
        int rowIndex = accountsTable.getSelectionModel().selectedIndexProperty().get();
        ObservableList<EmployeeAccount> row = accountsTable.getItems();
        EmployeeAccount oldAccount = row.get(rowIndex);

        //get new account value
        EmployeeAccount newAccount = new EmployeeAccount();
        newAccount.setUserID(oldAccount.getUserID());
        newAccount.setEmployeeID(oldAccount.getEmployeeID());
        newAccount.setUsername(accountsEditAccountUsername.getText());
        newAccount.setFirstName(accountsEditAccountFirstName.getText());
        newAccount.setLastName(accountsEditAccountLastName.getText());
        newAccount.setEmail(accountsEditAccountEmail.getText());
        newAccount.setPhoneNumber(accountsEditAccountPhone.getText());
        newAccount.setAccType((String)accountsEditAccountType.getValue());
        Location loc = new Location((String)accountsEditAccountLocation.getValue());
        newAccount.setLocation(loc);


        String accountValue = (String)accountsEditAccountType.getValue();
        int accountType = Integer.parseInt(accounts.get(accountValue));

        try {
            DataFetcher.updateAccount(oldAccount, newAccount, accountType);
        } catch (InsertFailedException ex) {
            //TODO: MessageBox
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            //TODO: TOTAL FAILURE MESSAGE BOX
            System.out.println(ex.getMessage());
        }

        //update table
        accountsTable.getItems().remove(rowIndex);
        accountsTable.getItems().add(rowIndex, newAccount);
    }

    /**
     * Deletes the selected account from the table
     * @param e ActionEvent object
     */
    @FXML
    protected void deleteAccount(ActionEvent e) {
        int rowIndex = accountsTable.getSelectionModel().selectedIndexProperty().get();
        ObservableList<EmployeeAccount> row = accountsTable.getItems();
        EmployeeAccount account = row.get(rowIndex);

        try {
            DataFetcher.deleteAccount(account.getUserID(), ((EmployeeAccount) account).getEmployeeID());
        } catch (InsertFailedException ex) {
            //TODO: EXCEPTION
        }

        //remove from table
        accountsTable.getItems().remove(rowIndex);
    }

    /**
     * Loads data from the SQL database into the equipment table
     * @param e ActionEvent object
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

    /**
     * Loads data from the SQL database into the Locations table
     * @param e ActionEvent object
     */
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
     * When a user clicks on a row in the table, the data is automatically
     * entered into the Edit Account section
     * @param e MouseEvent object
     */
    @FXML
    protected void updateEditBox(MouseEvent e) {
        //enable the box
        editAccountVBox.setDisable(false);

        //fill the data
        int rowIndex = accountsTable.getSelectionModel().selectedIndexProperty().get();
        ObservableList<EmployeeAccount> row = accountsTable.getItems();
        EmployeeAccount tmp = row.get(rowIndex);

        accountsEditAccountUsername.setText(tmp.getUsername());
        accountsEditAccountFirstName.setText(tmp.getFirstName());
        //accountsEditAccountLastName.setText(tmp.getLastName());
        accountsEditAccountEmail.setText(tmp.getEmail());
        accountsEditAccountPhone.setText(tmp.getPhoneNumber());
        accountsEditAccountType.setValue(tmp.getAccType());
        accountsEditAccountLocation.setValue(tmp.getLocationName());

    }

    /**
     * Sets the values for the drop down menus
     */
    private void setValues() {
        //set accountTypes
        Set s = accounts.keySet();
        Iterator it = s.iterator();
        ObservableList<String> options = FXCollections.observableArrayList();

        while(it.hasNext()) {
            options.add((String)it.next());
        }

        accountsNewAccountType.setItems(options);
        accountsEditAccountType.setItems(options);

        //set location
        s = locations.keySet();
        it = s.iterator();
        options = FXCollections.observableArrayList();

        while(it.hasNext()) {
            options.add((String)it.next());
        }

        accountsNewAccountLocation.setItems(options);
        accountsEditAccountLocation.setItems(options);
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
