package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Equipment;

import App.JavaFXLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * Controller for the manager system
 * This class contains methods for all event handling on the manager system
 */
public class ManagerSystemController extends Controller{
    @FXML private ToggleButton bikesTabButton; //Gets the bikes tab button object
    @FXML private ToggleButton penaltiesTabButton; //Gets the penalties tab button object
    @FXML private ToggleButton rentalsTabButton; //Gets the rentals tab button object
    @FXML private ToggleButton reportsTabButton; //Gets the reports tab button object
    @FXML private ToggleButton userTabButton; //Gets the user's account tab button object
    private List<ToggleButton> tabButtons; //List to store all tab buttons

    @FXML private AnchorPane bikesTab; //Gets the bikes tab object
    @FXML private AnchorPane penaltiesTab; //Gets the penalties tab object
    @FXML private AnchorPane rentalsTab; //Gets the rentals tab object
    @FXML private AnchorPane reportsTab; //Gets the reports tab object
    @FXML private AnchorPane userTab; //Gets the user tab object
    private List<AnchorPane> tabs; //List to store all tabs;

    @FXML private ComboBox penaltiesViewOption;
    @FXML private FlowPane unsolvedPenalties;
    @FXML private FlowPane solvedPenalties;

    /** Equipment Tab **/
    //Table
    @FXML private TableView equipmentTable;
    @FXML private TableColumn equipmentID;
    @FXML private TableColumn equipmentType;
    @FXML private TableColumn equipmentLocation;
    @FXML private TableColumn equipmentPrice;
    @FXML private TableColumn equipmentStatus;

    //Filter and search
    @FXML private ComboBox equipmentView;
    @FXML private ComboBox equipmentFilter;
    @FXML private TextField equipmentSearch;

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

    private static EmployeeAccount employee;

    private static HashMap<String, String> equipmentTypes;
    private static HashMap<String, String> bikeTypes;

    /**
     * The initialise method is called when the form first loads
     */
    @SuppressWarnings("Duplicates")
    public void initialize(){
        /* Initialise the tabButtons list and add all tab buttons to it */
        tabButtons = new ArrayList<>();
        tabButtons.add(bikesTabButton);
        tabButtons.add(penaltiesTabButton);
        tabButtons.add(rentalsTabButton);
        tabButtons.add(reportsTabButton);
        tabButtons.add(userTabButton);

        /* Initialise the tabs list and add all tabs to it */
        tabs = new ArrayList<>();
        tabs.add(bikesTab);
        tabs.add(penaltiesTab);
        tabs.add(rentalsTab);
        tabs.add(reportsTab);
        tabs.add(userTab);

        //Set the first tab as active
        TabSwitcher.setToFirstTab(tabButtons, tabs);

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

        loadEquipment("");
    }

    @SuppressWarnings("Duplicates")
    private void setDropdownOptions(){
        //Set the equipment view dropdown
        ObservableList<String> equipmentViewOptions = FXCollections.observableArrayList("Bikes", "Equipment");
        equipmentView.setItems(equipmentViewOptions);
        equipmentView.getSelectionModel().selectFirst();

        //Set the equipment filter for bikes (default view option)
        ObservableList<String> equipmentFilterOptions = OptionsListCreator.createList(bikeTypes);
        equipmentFilterOptions.add(0, "All");
        equipmentFilter.setItems(equipmentFilterOptions);
        equipmentFilter.getSelectionModel().selectFirst();

        //Set the penalties view dropdown
        ObservableList<String> options = FXCollections.observableArrayList("Unsolved Penalties", "Solved Penalties");
        penaltiesViewOption.setItems(options);
        penaltiesViewOption.getSelectionModel().selectFirst();
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

        filterAndSearchEquipment();
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
        if(!equipmentFilter.getSelectionModel().isEmpty()){
            equipmentTable.getItems().clear();

            String params;
            String selectedItem = (String) equipmentFilter.getSelectionModel().getSelectedItem();

            if(selectedItem.equals("All")){
                params = "";
            }else{
                params = selectedItem;
            }

            boolean showingBikes = equipmentView.getSelectionModel().getSelectedItem().equals("Bikes");
            if(showingBikes){
                loadBikes(params);
            }else{ //Showing equipment
                loadEquipment(params);
            }
        }
    }

    /**
     * Loads bikes from the database, given a search parameter, and displays it in the equipment table
     * @param params Parameters to search by
     */
    private void loadBikes(String params) {
        try {
            ObservableList<Equipment> equipment = DataFetcher.getBikes(this.employee.getLocation(), "search=" + params);
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
            ObservableList<Equipment> equipment = DataFetcher.getEquipment(this.employee.getLocation(), "search=" + params);
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
        equipmentID.setCellValueFactory(new PropertyValueFactory<Equipment, String>("ID"));
        equipmentType.setCellValueFactory(new PropertyValueFactory<Equipment, String>("TypeName"));
        equipmentLocation.setCellValueFactory(new PropertyValueFactory<Equipment, String>("LocationName"));
        equipmentPrice.setCellValueFactory(new PropertyValueFactory<Equipment, String>("Price"));
        equipmentStatus.setCellValueFactory(new PropertyValueFactory<Equipment, String>("Status"));

        equipmentTable.setItems(equipment);
    }

    /**
     * Handles switching the view of the user's co ntact details, between editable and non editable
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
    @FXML
    protected void logout(ActionEvent e) {
        new JavaFXLoader().loadNewFXWindow("LogIn", "Wheely Good Bikes", false);

        this.employee = null;
        this.stage.close();
    }
}
