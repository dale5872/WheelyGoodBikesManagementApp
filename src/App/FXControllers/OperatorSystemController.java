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

    //Filter and search
    @FXML private ComboBox equipmentView;
    @FXML private ComboBox equipmentFilter;
    @FXML private TextField equipmentSearch;

    //Edit and delete buttons
    @FXML private Button editEquip;
    @FXML private Button deleteEquip;

    /** Locations Tab **/
    //Table
    @FXML private TableColumn locationsID;
    @FXML private TableColumn locationsName;
    @FXML private TableView locationsTable;

    //Add, edit and delete buttons
    @FXML private Button editLocation;
    @FXML private Button deleteLocation;

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
    private static HashMap<String, String> bikeTypes;

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

        //Load data into tables
        loadEmployeeAccounts("");
        loadBikes("");
        loadLocations("");

        //Load in account types, locations and equipment types
        accountTypes = DataFetcher.getDropdownValues("accountTypes");
        locations = DataFetcher.getDropdownValues("getLocations");
        equipmentTypes = DataFetcher.getDropdownValues("equipmentTypes");
        bikeTypes = DataFetcher.getDropdownValues("bikeTypes");

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
     * Sets the values for the drop down menus
     */
    @SuppressWarnings("Duplicates")
    private void setDropdownOptions() {
        //Set the accounts table view option
        ObservableList<String> accountsTableViewOptions = FXCollections.observableArrayList("Employees", "Users");
        accountsView.setItems(accountsTableViewOptions);
        accountsView.getSelectionModel().selectFirst();

        //Set the accounts table employees filter
        ObservableList<String> employeesFilterOptions = FXCollections.observableArrayList("All", "Managers", "Operators");
        employeesFilter.setItems(employeesFilterOptions);
        employeesFilter.getSelectionModel().selectFirst();

        //Set the equipment view dropdown
        ObservableList<String> equipmentViewOptions = FXCollections.observableArrayList("Bikes", "Other Equipment");
        equipmentView.setItems(equipmentViewOptions);
        equipmentView.getSelectionModel().selectFirst();

        //Set the equipment filter for bikes (default view option)
        ObservableList<String> equipmentFilterOptions = OptionsListCreator.createList(bikeTypes);
        equipmentFilterOptions.add(0, "All");
        equipmentFilter.setItems(equipmentFilterOptions);
        equipmentFilter.getSelectionModel().selectFirst();
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
     * @param params Parameters to search by
     */
    protected void loadEmployeeAccounts(String params){
        try{
            ObservableList<EmployeeAccount> accounts = DataFetcher.getEmployeeAccounts(params);

            if(accounts == null){
                accountsTable.getItems().clear();
            }else{
                accountsID.setCellValueFactory(
                        new PropertyValueFactory<EmployeeAccount, String>("employeeID"));

                accountsUsername.setCellValueFactory(
                        new PropertyValueFactory<EmployeeAccount, String>("username"));

                accountsFirstName.setCellValueFactory(
                        new PropertyValueFactory<EmployeeAccount, String>("firstName"));

                accountsLastName.setCellValueFactory(
                        new PropertyValueFactory<EmployeeAccount, String>("lastName"));

                accountsEmail.setCellValueFactory(
                        new PropertyValueFactory<EmployeeAccount, String>("email"));

                accountsPhoneNumber.setCellValueFactory(
                        new PropertyValueFactory<EmployeeAccount, String>("phoneNumber"));

                accountsAccountType.setCellValueFactory(
                        new PropertyValueFactory<EmployeeAccount, String>("accType"));

                accountsLocation.setCellValueFactory(
                        new PropertyValueFactory<EmployeeAccount, String>("LocationName"));

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
                accountsID.setCellValueFactory(
                        new PropertyValueFactory<Account, String>("userID"));

                accountsUsername.setCellValueFactory(
                        new PropertyValueFactory<Account, String>("username"));

                accountsFirstName.setCellValueFactory(
                        new PropertyValueFactory<Account, String>("firstName"));

                accountsLastName.setCellValueFactory(
                        new PropertyValueFactory<Account, String>("lastName"));

                accountsTable.setItems(accounts);
            }
        }catch (Exception exc){
            return;
        }
    }

    /**
     * Creates and shows the dialog for adding an account, also disabling the operator system screen
     */
    @FXML
    protected void showAddAccountDialog() {
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
     */
    @FXML
    protected void showEditAccountDialog() {
        EmployeeAccount acc = (EmployeeAccount) getSelectedAccount();

        if(acc.getUserID() == employee.getUserID()){ //Check that we're not trying to edit the account that's logged in, as doing so could cause problems
            new ShowMessageBox().show("You cannot edit the active account.");
        }else{
            /* Load the popup */
            JavaFXLoader loader = new JavaFXLoader();
            loader.loadNewFXWindow("EditAccount", "Edit Account", false);

            this.disable();

            EditAccountController controller = (EditAccountController) loader.getController();
            controller.setParentController(this);
            controller.setOnCloseAction();
            controller.setDropdownValues(accountTypes, locations);

            /* Pass the selected account to edit dialog */
            controller.setExistingAccount(acc);
        }
    }

    /**
     * Creates and shows the dialog for deleting an account, also disabling the operator system screen
     */
    @FXML
    protected void showDeleteAccountDialog(){
        if(getSelectedAccount().getUserID() == employee.getUserID()){ //Check that we're not trying to delete the account that's logged in, for obvious reasons
            new ShowMessageBox().show("You cannot delete the active account.");
        }else {
            /* Load the popup */
            JavaFXLoader loader = new JavaFXLoader();
            loader.loadNewFXWindow("DeletionConfirmation", "Delete Account", false);

            this.disable();

            DeletionConfirmationController controller = (DeletionConfirmationController) loader.getController();
            controller.setParentController(this);
            controller.setOnCloseAction();
            controller.setThingToDelete("account");
        }
    }

    /**
     * Gets the account selected on the table
     * @return
     */
    private Account getSelectedAccount(){
        int rowIndex = accountsTable.getSelectionModel().selectedIndexProperty().get();
        return (Account) accountsTable.getItems().get(rowIndex);
    }

    /**
     * Add a new account
     * @param account The account to add
     * @param password The account password
     * @param accountType The account type
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
     * @param oldAccount The old account to update
     * @param newAccount The new account to replace it with
     * @param accountType The account type
     */
    public void updateAccount(EmployeeAccount oldAccount, EmployeeAccount newAccount, int accountType){
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
     */
    public void deleteAccount() {
        try{
            Account account = getSelectedAccount();

            if(account instanceof EmployeeAccount){
                DataFetcher.deleteAccount(account.getUserID(), ((EmployeeAccount) account).getEmployeeID());
            }else{//User account
                DataFetcher.deleteAccount(account.getUserID(), -1);
            }

            //Update table
            filterAndSearchAccounts();
        }catch(InsertFailedException ex){
            return;
        }
    }

    /**
     * When a user clicks on a row in the employees table, the edit and delete buttons are enabled
     */
    @FXML
    protected void setEditDeleteAccountButtons() {
        boolean accountsViewEmployees = accountsView.getSelectionModel().getSelectedItem().equals("Employees"); //If the table is set to show employees
        boolean itemSelected = !accountsTable.getSelectionModel().isEmpty(); //If an item is selected

        if(accountsViewEmployees && itemSelected){ //If something in the table is selected
            accountsEditAccount.setDisable(false);
            accountsDeleteAccount.setDisable(false);
        }else{ //If nothing in the table is selected
            accountsEditAccount.setDisable(true);
            accountsDeleteAccount.setDisable(true);
        }
    }

    /**
     * Handles selecting between "Bikes" and "Equipment" in the equipment view dropdown
     * The bikes types or equipment types (as appropriate) are put in the filter dropdown, along with an "All" option
     * The table is then updated accordingly
     */
    @FXML
    @SuppressWarnings("Duplicates")
    protected void changeEquipmentView(){
        boolean showingBikes = equipmentView.getSelectionModel().getSelectedItem().equals("Bikes");
        ObservableList<String> equipmentFilterOptions;

        /* Create options list for filter dropdown */
        if(showingBikes){
            equipmentFilterOptions = OptionsListCreator.createList(bikeTypes);
        }else{ //Showing equipment
            equipmentFilterOptions = OptionsListCreator.createList(equipmentTypes);
        }

        /* Add "All" option to list and add list to dropdown */
        equipmentFilterOptions.add(0, "All");
        equipmentFilter.setItems(equipmentFilterOptions);
        equipmentFilter.getSelectionModel().selectFirst();
    }

    /**
     * Gets the filter that has been set and loads the bikes or equipment into the table accordingly
     */
    @FXML
    @SuppressWarnings("Duplicates")
    protected void filterAndSearchEquipment(){
        /*
         * This method is called by the system every time the items in the combo box are changed.
         * This causes a NullPointerException to be thrown as no item is selected at that point, so only act if an item is selected.
         */
        if(!(equipmentFilter.getSelectionModel().isEmpty())){
            equipmentTable.getItems().clear();
            setEditDeleteEquipmentButtons();

            String typeParam = "type=";
            String searchParam = "&search=";
            String selectedItem = (String) equipmentFilter.getSelectionModel().getSelectedItem();

            //check type
            if(!selectedItem.equals("All")){
                typeParam += selectedItem;
            }

            //we need to get search parameter
            String searchString = equipmentSearch.getText();
            searchParam += searchString;

            boolean showingBikes = equipmentView.getSelectionModel().getSelectedItem().equals("Bikes");
            if(showingBikes){
                loadBikes(typeParam + searchParam);
            }else{ //Showing equipment
                loadEquipment(typeParam + searchParam);
            }
        }
    }

    /**
     * Loads bikes from the database, given a search parameter, and displays it in the equipment table
     * @param params Parameters to search by
     */
    private void loadBikes(String params) {
        try {
            ObservableList<Equipment> equipment = DataFetcher.getBikes(null, "search=" + params);
            fillEquipmentTable(equipment);
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    /**
     * Loads equipment from the database, given a search parameter, and displays it in the equipment table
     * @param params Parameters to search by
     */
    private void loadEquipment(String params) {
        try {
            ObservableList<Equipment> equipment = DataFetcher.getEquipment(null, params);
            fillEquipmentTable(equipment);
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    /**
     * Displays a list of bikes/equipment in the equipment table
     * @param equipment An ObservableList of Equipment objects to display
     */
    @SuppressWarnings("Duplicates")
    private void fillEquipmentTable(ObservableList<Equipment> equipment){
        equipmentID.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("ID"));

        equipmentType.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("TypeName"));

        equipmentLocation.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("LocationName"));

        equipmentPrice.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("Price"));

        equipmentStatus.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("Status"));

        equipmentTable.setItems(equipment);
    }

    /**
     * Creates and shows the dialog for adding new equipment/bikes, also disabling the operator system screen
     */
    @FXML
    protected void showAddEquipmentDialog(){
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("AddEquipment", "Add Equipment", false);

        this.disable();

        AddEquipmentController controller = (AddEquipmentController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
        controller.setDropdownValues(bikeTypes, equipmentTypes, locations);
    }

    /**
     * Creates and shows the dialog for editing new equipment/bikes, also disabling the operator system screen
     */
    @FXML
    protected void showEditEquipmentDialog(){
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("EditEquipment", "Edit Equipment", false);

        this.disable();

        EditEquipmentController controller = (EditEquipmentController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
        controller.setDropdownValues(locations);

        /* Get the selected equipment and pass it to the controller */
        Equipment equipment = getSelectedEquipment();
        controller.setEquipment(equipment);
    }

    /**
     * Creates and shows the dialog for deleting a piece of equipment, also disabling the operator system screen
     */
    @FXML
    protected void showDeleteEquipmentDialog(){
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("DeletionConfirmation", "Delete Equipment", false);

        this.disable();

        DeletionConfirmationController controller = (DeletionConfirmationController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();

        if(equipmentView.getSelectionModel().getSelectedItem().equals("Bikes")){
            controller.setThingToDelete("bike");
        }else{
            controller.setThingToDelete("equipment");
        }
    }

    /**
     * Gets the equipment selected in the table
     * @return
     */
    private Equipment getSelectedEquipment(){
        int rowIndex = equipmentTable.getSelectionModel().selectedIndexProperty().get();
        ObservableList<Equipment> row = equipmentTable.getItems();
        return row.get(rowIndex);
    }


    /**
     * Adds a new bike to the database
     */
    public void addBike(Equipment bike){
        try{
            DataFetcher.addBike(bike);

            //Update table
            filterAndSearchEquipment();
        }catch(InsertFailedException | EmptyDatasetException e) {
            return;
        }
    }

    /**
     * Adds a new piece of equipment to the database
     */
    public void addEquipment(Equipment equipment){
        try{
            DataFetcher.addEquipment(equipment);

            //Update table
            filterAndSearchEquipment();
        }catch(InsertFailedException | EmptyDatasetException e) {
            return;
        }
    }

    /**
     * Update the a piece of equipment in the database
     */
    public void updateBike(Equipment bike){
        try {
            DataFetcher.updateBike(bike);

            //Update table
            filterAndSearchEquipment();
        } catch (InsertFailedException exc) {
            return;
        }
    }

    /**
     * Update the a piece of equipment in the database
     */
    public void updateEquipment(Equipment equipment){
        try {
            DataFetcher.updateEquipment(equipment);

            //Update table
            filterAndSearchEquipment();
        } catch (InsertFailedException exc) {
            return;
        }
    }

    /**
     * Delete the selected bike
     */
    public void deleteBike() {
        try{
            Equipment bike = getSelectedEquipment();
            DataFetcher.deleteBike(bike);

            //Update table
            filterAndSearchEquipment();
        }catch(InsertFailedException exc){
            return;
        }
    }

    /**
     * Delete the selected equipment
     */
    public void deleteEquipment() {
        try{
            Equipment equipment = getSelectedEquipment();
            DataFetcher.deleteEquipment(equipment);

            //Update table
            filterAndSearchEquipment();
        }catch(InsertFailedException exc){
            return;
        }
    }

    /**
     * When a user clicks on a row in the equipment table, the edit and delete buttons are enabled
     */
    @FXML
    @SuppressWarnings("Duplicates")
    protected void setEditDeleteEquipmentButtons(){
        boolean equipmentSelected = !equipmentTable.getSelectionModel().isEmpty();

        if(equipmentSelected){
            editEquip.setDisable(false);
            deleteEquip.setDisable(false);
        }else{
            editEquip.setDisable(true);
            deleteEquip.setDisable(true);
        }
    }

    /**
     * Gets the search term that has been entered and loads the locations into the table accordingly
     */
    @FXML
    protected void filterAndSearchLocations(){
        locationsTable.getItems().clear();
        setEditDeleteLocationButtons();
        loadLocations(locationSearch.getText());
    }

    /**
     * Loads data from the SQL database into the Locations table
     * @param params Search parameters
     */
    private void loadLocations(String params){
        try{
            ObservableList<Location> locations = DataFetcher.getLocations("search=" + params);

            locationsID.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("locationID"));

            locationsName.setCellValueFactory(
                    new PropertyValueFactory<Equipment, String>("Name"));

            locationsTable.setItems(locations);
        }catch(EmptyDatasetException exc){
            return;
        }
    }

    /**
     * Creates and shows the dialog for adding a location, also disabling the operator system screen
     */
    @FXML
    protected void showAddLocationDialog(){
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("AddEditLocation", "Add Location", false);

        this.disable();

        AddEditLocationController controller = (AddEditLocationController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
    }

    /**
     * Creates and shows the dialog for editing a location, also disabling the operator system screen
     */
    @FXML
    protected void showEditLocationDialog(){
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("AddEditLocation", "Edit Location", false);

        this.disable();

        AddEditLocationController controller = (AddEditLocationController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
        controller.setLocation(getSelectedLocation());
    }

    /**
     * Creates and shows the dialog for deleting a location, also disabling the operator system screen
     */
    @FXML
    protected void showDeleteLocationDialog(){
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("DeletionConfirmation", "Delete Location", false);

        this.disable();

        DeletionConfirmationController controller = (DeletionConfirmationController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
        controller.setThingToDelete("location");
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
     * Adds a new location to the database and reloads the table
     * @param name The name of the new location to add
     */
    public void addLocation(String name) {
        try{
            DataFetcher.addLocation(name);

            //Update table
            filterAndSearchLocations();
        }catch(InsertFailedException exc){
            return;
        }
    }

    /**
     * Updates a location in the database
     * @param location The updated location
     */
    public void editLocation(Location location) {
        try{
            DataFetcher.editLocation(location);

            //Update table
            filterAndSearchLocations();
        }catch(InsertFailedException exc){
            return;
        }
    }

    /**
     * Deletes the selected location and reloads the locations table
     */
    public void deleteLocation() {
        try{
            DataFetcher.deleteLocation(getSelectedLocation());

            //Update table
            filterAndSearchLocations();
        } catch(InsertFailedException exc){
            return;
        }
    }

    /**
     * Enables the edit and delete location buttons if/when a location is selected in the table
     */
    @FXML
    @SuppressWarnings("Duplicates")
    protected void setEditDeleteLocationButtons(){
        boolean locationSelected = !locationsTable.getSelectionModel().isEmpty();

        if(locationSelected){
            editLocation.setDisable(false);
            deleteLocation.setDisable(false);
        }else{
            editLocation.setDisable(true);
            deleteLocation.setDisable(true);
        }
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
    @SuppressWarnings("Duplicates")
    protected void changeContactDetails(ActionEvent e){
        boolean phoneBlank = userAccountPhoneTextbox.getText().equals("");
        boolean emailBlank = userAccountEmailTextbox.getText().equals("");

        if(phoneBlank || emailBlank) { //Blank check
            new ShowMessageBox().show("You must enter an email address and phone number.");
        }else{ //No blanks, so change
            try{
                /* Create a new EmployeeAccount object, with all the unchanged values*/
                EmployeeAccount newAcc = new EmployeeAccount();
                newAcc.setUserID(employee.getUserID());
                newAcc.setEmployeeID(employee.getEmployeeID());
                newAcc.setUsername(employee.getUsername());
                newAcc.setFirstName(employee.getFirstName());
                newAcc.setLastName(employee.getLastName());
                newAcc.setLocation(employee.getLocation());
                newAcc.setAccType(employee.getAccType());

                /* Pass the new phone number and email to newAcc */
                String newPhone = userAccountPhoneTextbox.getText();
                String newEmail = userAccountEmailTextbox.getText();
                newAcc.setPhoneNumber(newPhone);
                newAcc.setEmail(newEmail);

                /* Get the account type index */
                int accountType = Integer.parseInt(accountTypes.get(employee.getAccType()));

                /* Update the account ion the database */
                DataFetcher.updateAccount(employee, newAcc, accountType);

                /* Update the account in this controller and the table */
                setEmployee(newAcc);
                filterAndSearchAccounts();

                /* Switch back to non-editable view */
                switchContactDetailsView(e);
            }catch(InsertFailedException e1){
                return;
            }
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
