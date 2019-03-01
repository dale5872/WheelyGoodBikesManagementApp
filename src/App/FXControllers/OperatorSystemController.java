package App.FXControllers;

import App.Classes.Account;
import App.Classes.EmployeeAccount;
import App.Classes.Equipment;
import App.Classes.Location;

import App.JavaFXLoader;
import DatabaseConnector.InsertFailedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * Controller for the operator system
 * This class contains methods for all event handling on the operator system
 */
public class OperatorSystemController extends Controller{
    @FXML private AnchorPane parentPane;

    @FXML private ToggleButton accountsTabButton; //Gets the accounts tab button object
    @FXML private ToggleButton bikesTabButton; //Gets the bikes tab button object
    @FXML private ToggleButton locationsTabButton; //Gets locations yellow tab button object
    @FXML private ToggleButton userTabButton; //Gets the user's account tab button
    private List<ToggleButton> tabButtons; //List to store all tab buttons

    @FXML private AnchorPane accountsTab; //Gets the accounts tab object
    @FXML private AnchorPane bikesTab; //Gets the bikes tab object
    @FXML private AnchorPane locationsTab; //Gets the locations tab object
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

    //Filter and search
    @FXML private ComboBox accountsView;
    @FXML private ComboBox employeesFilter;
    @FXML private Label searchUsersWarning;
    @FXML private TextField accountsSearch;

    //Edit and delete buttons
    @FXML private Button accountsEditAccount;
    @FXML private Button accountsDeleteAccount;


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

    //Filter and search
    @FXML private ComboBox equipmentFilter;
    @FXML private TextField equipmentSearch;

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

    //Filter and Search
    @FXML private TextField locationSearch;

    /** Account Tab **/
    @FXML private Label userAccountID;
    @FXML private Label userAccountUsername;
    @FXML private Label userAccountName;
    @FXML private Label userAccountType;
    @FXML private Label userAccountLocation;

    @FXML private VBox contactDetailsNonEditable;
    @FXML private Label userAccountEmail;
    @FXML private Label userAccountPhone;

    @FXML private VBox contactDetailsEditable;
    @FXML private TextField userAccountPhoneTextbox;
    @FXML private TextField userAccountEmailTextbox;

    @FXML private HBox changeContactContainer;
    @FXML private Button contactDetailsViewBtn;

    @FXML private HBox confirmContactContainer;

    //fields
    private static EmployeeAccount employee;
    private static HashMap<String, String> accountTypes;
    private static HashMap<String, String> locations;
    private static HashMap<String, String> equipmentTypes;
    private static HashMap<String, String> bike_type;

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
        tabButtons.add(userTabButton);

        /* Initialise the tabs list and add all tabs to it */
        tabs = new ArrayList<>();
        tabs.add(accountsTab);
        tabs.add(bikesTab);
        tabs.add(locationsTab);
        tabs.add(userTab);

        //Set the first tab as active
        TabSwitcher.setToFirstTab(tabButtons, tabs);

        //Load in data for adding / editing accounts
        loadEmployeeAccounts("");
        loadEquipment(null);
        loadLocations(null);

        //Load in account types, locations and equipment types
        accountTypes = DataFetcher.getDropdownValues("accountTypes");
        locations = DataFetcher.getDropdownValues("getLocations");
        equipmentTypes = DataFetcher.getDropdownValues("equipmentTypes");
        //   bike_type = DataFetcher.getDropdownValues("bikeTypes");

        setDropdownOptions();
    }

    @SuppressWarnings("Duplicates")
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
        userAccountLocation.setText(employee.getLocationName());
    }

    /**
     * Disable the window
     */
    public void disable(){
        parentPane.setDisable(true);
    }

    /**
     * Enable the window
     */
    public void enable(){
        parentPane.setDisable(false);
    }

    /**
     * Handles switching between "Users" and "Employees" on accounts table
     * Employee-only columns are shown or hidden, then table is updated according to options chosen
     */
    @FXML
    protected void changeAccountsView(){
        boolean showingEmployees = accountsView.getSelectionModel().getSelectedItem().equals("Employees");
        employeeInfoVisible(showingEmployees);
        filterAndSearchAccounts();
    }

    /**
     * If showing employees, show the employees filter and columns, hide the "Search to view users" warning
     * If showing users, hide the employees filter and columns, show the "Search to view users" warning
     * @param showingEmployees
     */
    private void employeeInfoVisible(boolean showingEmployees){
        if(showingEmployees){
            employeesFilter.setVisible(true);
            searchUsersWarning.setVisible(false);

            accountsEmail.setVisible(true);
            accountsPhoneNumber.setVisible(true);
            accountsAccountType.setVisible(true);
            accountsLocation.setVisible(true);
        }else{ //Showing users
            employeesFilter.setVisible(false);
            searchUsersWarning.setVisible(true);

            accountsEmail.setVisible(false);
            accountsPhoneNumber.setVisible(false);
            accountsAccountType.setVisible(false);
            accountsLocation.setVisible(false);
        }
    }

    /**
     * Gets the filters set or search terms entered by the user displays the appropriate data in the accounts table
     */
    @FXML
    protected void filterAndSearchAccounts(){
        accountsTable.getItems().clear(); //Clear all previous results
        setEditDeleteAccountButtons();

        String params;
        String searchTerm = accountsSearch.getText();
        boolean showingEmployees = accountsView.getSelectionModel().getSelectedItem().equals("Employees");

        if(showingEmployees){
            String filter = (String) employeesFilter.getSelectionModel().getSelectedItem();
            switch(filter){
                case "All":
                    if(searchTerm.equals("")){ //If no search term has been entered, show all
                        loadEmployeeAccounts("");
                    }else{
                        params = "search=" + searchTerm;
                        loadEmployeeAccounts(params);
                    }
                    break;
                case "Managers":
                    params = "account_type=Manager";

                    if (!searchTerm.equals("")) {
                        params += "&search=" + searchTerm;
                    }

                    loadEmployeeAccounts(params);
                    break;
                case "Operators":
                    params = "account_type=Operator";

                    if (!searchTerm.equals("")) {
                        params += "&search=" + searchTerm;
                    }

                    loadEmployeeAccounts(params);
                    break;
                default:
                    break;
            }
        }else{ //Showing users
            if(!searchTerm.equals("")){
                loadUserAccounts("search=" + searchTerm);
            }
        }
    }

    /**
     * Fetches employee accounts from the database using given parameters, and displays them on the accounts table
     * Pass blank parameters to show all
     * @param params
     */
    protected void loadEmployeeAccounts(String params){
        try{
            ObservableList<EmployeeAccount> accounts = DataFetcher.getEmployeeAccounts(params);

            if(accounts == null){
                accountsTable.getItems().clear();
            }else{
                accountsID.setCellValueFactory(new PropertyValueFactory<EmployeeAccount, String>("employeeID"));
                accountsUsername.setCellValueFactory(new PropertyValueFactory<EmployeeAccount, String>("username"));
                accountsFirstName.setCellValueFactory(new PropertyValueFactory<EmployeeAccount, String>("firstName"));
                accountsLastName.setCellValueFactory(new PropertyValueFactory<EmployeeAccount, String>("lastName"));
                accountsEmail.setCellValueFactory(new PropertyValueFactory<EmployeeAccount, String>("email"));
                accountsPhoneNumber.setCellValueFactory(new PropertyValueFactory<EmployeeAccount, String>("phoneNumber"));
                accountsAccountType.setCellValueFactory(new PropertyValueFactory<EmployeeAccount, String>("accType"));
                accountsLocation.setCellValueFactory(new PropertyValueFactory<EmployeeAccount, String>("LocationName"));

                accountsTable.setItems(accounts);
            }
        } catch (Exception exc) {
            return;
        }
    }

    /**
     * Fetches user accounts from the database using given parameters, and displays them on the accounts table
     * Pass blank parameters to show all
     * @param params the string of search terms
     */
    @FXML
    protected void loadUserAccounts(String params) {
        try{
            ObservableList<Account> accounts = DataFetcher.getUserAccounts(params);

            if(accounts == null){
                accountsTable.getItems().clear();
            }else{
                accountsID.setCellValueFactory(new PropertyValueFactory<Account, String>("userID"));
                accountsUsername.setCellValueFactory(new PropertyValueFactory<Account, String>("username"));
                accountsFirstName.setCellValueFactory(new PropertyValueFactory<Account, String>("firstName"));
                accountsLastName.setCellValueFactory(new PropertyValueFactory<Account, String>("lastName"));

                accountsTable.setItems(accounts);
            }
        }catch (Exception exc){
            return;
        }
    }

    /**
     * Creates and shows the dialog for adding an account, also disabling the operator system screen
     * @param e ActionEvent object
     */
    @FXML
    protected void showAddAccountDialog(ActionEvent e) {
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("AddAccount", "Add Account", false);

        this.disable();

        AddAccountController controller = (AddAccountController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
        controller.setDropdownValues(accountTypes, locations);
    }

    /**
     * Creates and shows the dialog for editing an account, also disabling the operator system screen
     * @param e ActionEvent object
     */
    @FXML
    protected void showEditAccountDialog(ActionEvent e) {
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("EditAccount", "Edit Account", false);

        this.disable();

        EditAccountController controller = (EditAccountController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
        controller.setDropdownValues(accountTypes, locations);

        /* Get the selected account and pass to edit dialog */
        int rowIndex = accountsTable.getSelectionModel().selectedIndexProperty().get();
        EmployeeAccount acc = (EmployeeAccount) accountsTable.getItems().get(rowIndex);
        controller.setExistingAccount(acc);
    }

    /**
     * Add a new account
     * @param account
     * @param password
     * @param accountType
     */
    public void addAccount(Account account, String password, int accountType){
        try{
            DataFetcher.addAccount(account, password, accountType);

            //Update table
            filterAndSearchAccounts();
        }catch(Exception ex){
            return;
        }
    }

    /**
     * Update an existing account
     * @param oldAccount
     * @param newAccount
     * @param accountType
     */
    public void updateAccount(Account oldAccount, Account newAccount, int accountType){
        try{
            DataFetcher.updateAccount(oldAccount, newAccount, accountType);

            //Update table
            filterAndSearchAccounts();
        }catch(InsertFailedException ex){
            return;
        }catch(Exception ex){
            //Something else has happened, print to stderr and show error
            //Show error to the user
            ShowMessageBox messageBox = new ShowMessageBox();
            messageBox.show("An unknown error has occured adding this account.");
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Deletes the selected account from the table
     * @param e ActionEvent object
     */
    @FXML
    protected void deleteAccount(ActionEvent e) {
        int rowIndex = accountsTable.getSelectionModel().selectedIndexProperty().get();
        ObservableList<EmployeeAccount> row = accountsTable.getItems();
        Account account = row.get(rowIndex);

        try {
            if(account instanceof EmployeeAccount) {
                DataFetcher.deleteAccount(account.getUserID(), ((EmployeeAccount) account).getEmployeeID());
            } else {
                //user account
                DataFetcher.deleteAccount(account.getUserID(), -1);
            }
        } catch (InsertFailedException ex) {
            return;
        }

        //remove from table
        accountsTable.getItems().remove(rowIndex);
    }

    /**
     * When a user clicks on a row in the employees table, the edit and delete buttons are enabled
     */
    @FXML
    protected void setEditDeleteAccountButtons() {
        boolean accountsViewEmployees = accountsView.getSelectionModel().getSelectedItem().equals("Employees"); //If the table is set to show employees
        boolean itemSelected = (accountsTable.getSelectionModel().getSelectedIndex() >= 0); //If an item is selected

        if(accountsViewEmployees && itemSelected){ //If something in the table is selected
            accountsEditAccount.setDisable(false);
            accountsDeleteAccount.setDisable(false);
        }else{ //If nothing in the table is selected
            accountsEditAccount.setDisable(true);
            accountsDeleteAccount.setDisable(true);
        }
    }

    /**
     * Loads data from the SQL database into the equipment table
     * @param e ActionEvent object
     */
    @FXML
    protected void loadEquipment(Event e) {
        try {
            String searchParameter = equipmentSearch.getText();

            /**
            if((searchParameter == null || searchParameter.equals("")) && e instanceof KeyEvent) {
                //empty search string
                return;
            } else
                */

            if(e instanceof ActionEvent) {
                //dropdown menu
                searchParameter = (String) equipmentFilter.getSelectionModel().getSelectedItem();
                if(searchParameter.equals("All")) {
                    searchParameter = "";
                }
            }

            ObservableList<Equipment> equipment = DataFetcher.getEquipment(null, "search=" + searchParameter);

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

        String locationName = (String)newEquipLocation.getValue();
        Location loc = new Location(Integer.parseInt(this.locations.get(locationName)), locationName);

        eq.setLocation(loc);
        eq.setTypeName((String)newEquipType.getValue());
        eq.setTypeID(Integer.parseInt(equipmentTypes.get((String)newEquipType.getValue())));
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

        String locationName = (String)editEquipLocation.getValue();
        Location loc = new Location(Integer.parseInt(this.locations.get(locationName)), locationName);

        tmp.setLocation(loc);
        tmp.setStatus((String)editEquipStatus.getValue());
        tmp.setTypeID(Integer.parseInt(equipmentTypes.get(editEquipType.getValue())));

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
            loadEquipment(null);
        } catch (InsertFailedException exc) {
            return;
        }
    }


    /**
     * Loads data from the SQL database into the Locations table
     * @param e ActionEvent object
     */
    @FXML
    protected void loadLocations(Event e) {
        try {
            String searchParameters = locationSearch.getText();

            /**
            if((searchParameters == null || searchParameters.equals("")) && e instanceof KeyEvent) {
                //empty seatch string, dont query
                return;
            }
            **/

            ObservableList<Location> locations = DataFetcher.getLocations("search=" + searchParameters);

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
     * Sets the values for the drop down menus
     */
    @SuppressWarnings("Duplicates")
    private void setDropdownOptions() {
        //Set the location dropdowns
        ObservableList<String> locationOptions = OptionsListCreator.createList(locations);
        newEquipLocation.setItems(locationOptions);
        editEquipLocation.setItems(locationOptions);

        //Set the equipment type dropdowns
        ObservableList<String> equipmentTypeOptions = OptionsListCreator.createList(equipmentTypes);
        newEquipType.setItems(equipmentTypeOptions);
        editEquipType.setItems(equipmentTypeOptions);

        //Set the equipment filter dropdown
        equipmentTypeOptions.add(0, "All");
        equipmentFilter.setItems(equipmentTypeOptions);
        equipmentFilter.getSelectionModel().selectFirst();

        //Set the equipment status dropdown
        ObservableList<String> equipmentStatusOptions = FXCollections.observableArrayList("Available", "Booked", "Damaged");
        editEquipStatus.setItems(equipmentStatusOptions);

        //Set the accounts table view option
        ObservableList<String> accountsTableViewOptions = FXCollections.observableArrayList("Employees", "Users");
        accountsView.setItems(accountsTableViewOptions);
        accountsView.getSelectionModel().selectFirst();

        //Set the accounts table view option
        ObservableList<String> employeesFilterOptions = FXCollections.observableArrayList("All", "Managers", "Operators");
        employeesFilter.setItems(employeesFilterOptions);
        employeesFilter.getSelectionModel().selectFirst();
    }

    /**
     * Handles switching the view of the user's contact details, between editable and non editable
     * @param e
     */
    @FXML
    @SuppressWarnings("Duplicates")
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
     * Changes the logged in user's contact details, then switches back to non-editable view
     * @param e
     */
    @FXML
    protected void changeContactDetails(ActionEvent e){
        /** TODO: change the logged in user's contact details */

        switchContactDetailsView(e);
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

        this.employee = null;
        this.stage.close();
    }
}
