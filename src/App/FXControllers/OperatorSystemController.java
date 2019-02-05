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
    @FXML private TableColumn equipmentLocation;
    @FXML private TableColumn equipmentType;
    @FXML private TableColumn equipmentPrice;
    @FXML private TableColumn equipmentStatus;
    @FXML private TableView equipmentTable;

    //Add equipment
    @FXML private ComboBox newEquipLocation;
    @FXML private ComboBox newEquipType;

    //Edit equipment
    @FXML private VBox editEquipmentVBox;
    @FXML private ComboBox editEquipLocation;
    @FXML private ComboBox editEquipType;
    @FXML private ComboBox editEquipStatus;

    /** Locations Tab **/
    //Table
    @FXML private TableColumn locationsID;
    @FXML private TableColumn locationsName;
    @FXML private TableView locationsTable;

    //Add Location
    @FXML private TextField newLocationName;

    //Edit Location
    @FXML private VBox editLocationVBox;
    @FXML private TextField editLocationName;

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
    private static HashMap<String, String> equipment_type;
    private static HashMap<String, String> bike_type;

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
            loadEquipment(null);
            loadLocations(null);
        } catch (InvalidParametersException e) {
            //Some real issue here...
            ShowMessageBox messageBox = new ShowMessageBox();
            messageBox.show("FATAL ERROR HAS OCCURED. ERROR CODE: OSC01");
            System.exit(100);
        }

        //Load in account types and location dropdown boxes
        accounts = DataFetcher.getDropdownValues("accountTypes");
        locations = DataFetcher.getDropdownValues("locations");
        equipment_type = DataFetcher.getDropdownValues("equipmentTypes");
     //   bike_type = DataFetcher.getDropdownValues("bikeTypes");

        setValues();

    }

    /**
     * Finds the parameters selected by the user and loads the data
     * based on those parameters. Parameters are selected through
     * Radio Buttons
     * @param e ActionEvent object
     * @throws InvalidParametersException if no accounts to load
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

        try {
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
        } catch (EmptyDatasetException exc) {
            return;
        }
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
            //failed
            return;
        } catch (Exception ex) {
            //Something else has happened, print to stderr and show error
            //Show error to the user
            ShowMessageBox messageBox = new ShowMessageBox();
            messageBox.show("An unknown error has occured adding this account.");
            ex.printStackTrace();
            return;
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
            return;
        } catch (Exception ex) {
            //Something else has happened, print to stderr and show error
            //Show error to the user
            ShowMessageBox messageBox = new ShowMessageBox();
            messageBox.show("An unknown error has occured adding this account.");
            ex.printStackTrace();
            return;
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
            return;
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
        try {
        ObservableList<Equipment> equipment = DataFetcher.equipment();

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
     * Adds a new piece of equipment to the database
     * @param e ActionEvent object
     */
    @FXML
    protected void addEquipment(ActionEvent e) {
        Equipment eq = new Equipment();
        Location loc = new Location((String)newEquipLocation.getValue());

        eq.setLocation(loc);
        eq.setTypeName((String)newEquipType.getValue());
        eq.setTypeID(Integer.parseInt(equipment_type.get((String)newEquipType.getValue())));
        eq.setStatus("Available");

        try {
            DataFetcher.addEquipment(eq);
        } catch (InsertFailedException exc) {
            return;
        } catch (EmptyDatasetException exc) {
            return;
        }

        //inefficent but necessary to reload data
        loadEquipment(null);
    }

    /**
     * Adds the information to the edit box for editing equipment
     * @param e MouseEvent object
     */
    @FXML
    protected void updateEquipmentEditBox(MouseEvent e) {
        //enable the box
        editEquipmentVBox.setDisable(false);

        //fil the data
        Equipment tmp = getSelectedEquipment();

        editEquipLocation.setValue(tmp.getLocationName());
        editEquipType.setValue(tmp.getTypeName());
        editEquipStatus.setValue(tmp.getStatus());
    }

    private Equipment getSelectedEquipment() {
        int rowIndex = equipmentTable.getSelectionModel().selectedIndexProperty().get();
        ObservableList<Equipment> row = equipmentTable.getItems();
        return row.get(rowIndex);
    }

    /**
     * Update the selected equipment with the selected parameters
     * @param e ActionEvent object
     */
    @FXML
    protected void updateEquipment(ActionEvent e) {
        Equipment tmp = getSelectedEquipment();


        Location loc = new Location((String)editEquipLocation.getValue());
        tmp.setLocation(loc);
        tmp.setStatus((String)editEquipStatus.getValue());
        tmp.setTypeName(equipment_type.get((String)editEquipType.getValue()));

        try {
            DataFetcher.updateEquipment(tmp);
        } catch (InsertFailedException exc) {
            return;
        }

        loadEquipment(null);
    }

    /**
     * Delete the selected Equipment object
     * @param e ActionEvent object
     */
    @FXML
    protected void deleteEquipment(ActionEvent e) {
        Equipment tmp = getSelectedEquipment();
        try {
            DataFetcher.deleteEquipment(tmp);
        } catch (InsertFailedException exc) {
            return;
        }
    }


    /**
     * Loads data from the SQL database into the Locations table
     * @param e ActionEvent object
     */
    @FXML
    protected void loadLocations(ActionEvent e) {

        try {
            ObservableList<Location> locations = DataFetcher.locations();

            locationsID.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("locationID")
            );
            locationsName.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("Name")
            );

            locationsTable.setItems(locations);
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    /**
     * Adds a new location to the database and reloads the table
     * @param e ActionEvent object
     */
    @FXML
    protected void addLocation(ActionEvent e) {
        String locationName = newLocationName.getText();

        try {
            DataFetcher.addLocation(locationName);
        } catch (InsertFailedException exc) {
            //message box already shows...
            return;
        }

        //reload locations as only a few so wont take long to execute
        loadLocations(null);

    }

    /**
     * Edits the selected locations name
     * @param e ActionEvent object
     */
    @FXML
    protected void editLocation(ActionEvent e) {
        Location tmp = getSelectedLocation();
        tmp.setName(editLocationName.getText());
        try {
            DataFetcher.editLocation(tmp);
        } catch (InsertFailedException exc) {
            return;
        }

        //update table
        loadLocations(null);
    }

    /**
     *
     * @param e MouseEvent object
     */
    @FXML
    protected void updateLocationsEditBox(MouseEvent e) {
        //enable the edit box
        editLocationVBox.setDisable(false);
        Location tmp = getSelectedLocation();
        editLocationName.setText(tmp.getName());
    }

    /**
     * Returns the selected location object in the table
     * @return
     */
    private Location getSelectedLocation() {
        int rowIndex = locationsTable.getSelectionModel().selectedIndexProperty().get();
        ObservableList<Location> row = locationsTable.getItems();
        return row.get(rowIndex);
    }

    /**
     * Deletes the selected location and reloads the locations
     * @param e ActionEvent object
     */
    @FXML
    protected void deleteLocation(ActionEvent e) {
        try {
            DataFetcher.deleteLocation(getSelectedLocation());
        } catch (InsertFailedException exc) {
            return;
        }

        loadLocations(null);
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
        accountsEditAccountLastName.setText(tmp.getLastName());
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
        newEquipLocation.setItems(options);
        editEquipLocation.setItems(options);

        //Set equipment Types
        s = equipment_type.keySet();
        it = s.iterator();
        options = FXCollections.observableArrayList();

        while(it.hasNext()) {
            options.add((String)it.next());
        }

        newEquipType.setItems(options);
        editEquipType.setItems(options);

        //set status box
        editEquipStatus.getItems().add(0, "Available");
        editEquipStatus.getItems().add(1, "Booked");
        editEquipStatus.getItems().add(2, "Damaged");

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
