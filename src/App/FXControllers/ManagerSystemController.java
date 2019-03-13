package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Equipment;

import App.Classes.Rental;
import DatabaseConnector.InsertFailedException;
import DatabaseConnector.Results;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import javax.security.auth.callback.Callback;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Controller for the manager system
 * This class contains methods for all event handling on the manager system
 */
public class ManagerSystemController extends SystemController{
    @FXML private ToggleButton bikesTabButton; //Gets the bikes tab button object
    @FXML private ToggleButton penaltiesTabButton; //Gets the penalties tab button object
    @FXML private ToggleButton rentalsTabButton; //Gets the rentals tab button object
    @FXML private ToggleButton reportsTabButton; //Gets the reports tab button object
    @FXML private ToggleButton userTabButton; //Gets the user's account tab button object

    @FXML private AnchorPane bikesTab; //Gets the bikes tab object
    @FXML private AnchorPane penaltiesTab; //Gets the penalties tab object
    @FXML private AnchorPane rentalsTab; //Gets the rentals tab object
    @FXML private AnchorPane reportsTab; //Gets the reports tab object
    @FXML private AnchorPane userTab; //Gets the user tab object

    /** Penalties tab */
    @FXML private ComboBox penaltiesViewOption;
    @FXML private TextField penaltiesSearch;

    @FXML private TableView penaltiesTable;

    @FXML private Button solvePenalty;
    @FXML private Button viewPenalty;

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
    //Search
    @FXML private TextField rentalSearch;

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

    /** Reports Table **/
    @FXML private BarChart barChart;

    /** General Report **/
    @FXML private DatePicker generalDatePicker;
    @FXML private ComboBox generalReportFilter;

    @FXML private LineChart revenueGraph;
    @FXML private DatePicker revenueFromDate;
    @FXML private DatePicker revenueToDate;
    @FXML private ComboBox revenueReportFilter;
    @FXML private CategoryAxis rentalXAxis;
    @FXML private NumberAxis rentalYAxis;

    private static HashMap<String, String> equipmentTypes;
    private static HashMap<String, String> bikeTypes;
    private static HashMap<String, String> savedReports;

    /**
     * The initialise method is called when the form first loads
     */
    @SuppressWarnings("Duplicates")
    public void initialize(){
        /* Initialise the tabButtons list and add all tab buttons to it */
        super.tabButtons = new ArrayList<>();
        super.tabButtons.add(bikesTabButton);
        super.tabButtons.add(penaltiesTabButton);
        super.tabButtons.add(rentalsTabButton);
        super.tabButtons.add(reportsTabButton);
        super.tabButtons.add(userTabButton);

        /* Initialise the tabs list and add all tabs to it */
        super.tabs = new ArrayList<>();
        super.tabs.add(bikesTab);
        super.tabs.add(penaltiesTab);
        super.tabs.add(rentalsTab);
        super.tabs.add(reportsTab);
        super.tabs.add(userTab);

        //Set the first tab as active
        TabSwitcher.setToFirstTab(tabButtons, tabs);

        equipmentTypes = DataFetcher.getDropdownValues("equipmentTypes");
        bikeTypes = DataFetcher.getDropdownValues("bikeTypes");
        savedReports = DataFetcher.getFilenameDropdownValues();

        setDropdownOptions();
    }

    @Override
    public void setEmployee(EmployeeAccount e) {
        super.setEmployee(e);

        try {
            loadBikes("");
            loadRentals("");
        } catch(Exception exc) {
        }
    }

    @SuppressWarnings("Duplicates")
    private void setDropdownOptions(){
        //Set the equipment view dropdown
        ObservableList<String> equipmentViewOptions = FXCollections.observableArrayList("Bikes", "Other Equipment");
        equipmentView.setItems(equipmentViewOptions);
        equipmentView.getSelectionModel().selectFirst();

        //Set the equipment filter for bikes (default view option)
        ObservableList<String> equipmentFilterOptions = OptionsListCreator.createList(bikeTypes);
        equipmentFilterOptions.add(0, "All");
        equipmentFilter.setItems(equipmentFilterOptions);
        equipmentFilter.getSelectionModel().selectFirst();

        //Set the penalties view dropdown
        ObservableList<String> options = FXCollections.observableArrayList("Solved", "Unsolved");
        penaltiesViewOption.setItems(options);
        penaltiesViewOption.getSelectionModel().selectFirst();

        //Set saved reports dropdown
        ObservableList<String> reports = OptionsListCreator.createList(savedReports);
        generalReportFilter.setItems(reports);
        equipmentFilter.getSelectionModel().selectFirst();
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
        equipmentID.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("ID"));

        equipmentType.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("TypeName"));

        equipmentLocation.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("LocationName"));

        equipmentPrice.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("FormattedPrice"));

        equipmentStatus.setCellValueFactory(
                new PropertyValueFactory<Equipment, String>("Status"));

        equipmentTable.setItems(equipment);
    }

    protected void loadRentals(String params) throws ErrorException {
        try {
            ObservableList<Rental> rental = DataFetcher.getRentals(this.employee.getLocation(), "search=" + params);

            //fill table
            rentalsID.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("ID")
            );

            rentalsEquipmentID.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("equipmentID")
            );

            rentalsEquipmentName.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("equipmentName")
            );

            rentalsEquipmentPrice.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("equipmentPrice")
            );

            rentalsStartTime.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("startTime")
            );

            rentalsReturnTime.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("returnTime")
            );

            rentalsTotalPrice.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("formattedCost")
            );

            rentalsLocation.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("locationName")
            );

            rentalsStatus.setCellValueFactory(
                    new PropertyValueFactory<Rental, String>("status")
            );

            rentalsTable.setItems(rental);

        } catch (EmptyDatasetException | InvalidParametersException exc) {
            return;
        } catch (Exception e) {
            throw new ErrorException("An error has occured, check the log for details", true, e);
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
                HashMap<String, String> accountTypes = DataFetcher.getDropdownValues("accountTypes");
                int accountType = Integer.parseInt(accountTypes.get(newAcc.getAccType()));

                /* Update the account ion the database */
                DataFetcher.updateAccount(employee, newAcc, accountType);

                /* Update the account in this controller */
                setEmployee(newAcc);

                /* Switch back to non-editable view */
                super.switchContactDetailsView(e);
            }catch(InsertFailedException ex){
                return;
            }
        }
    }

    /**
     * Handles filtering a searching penalties in the table
     */
    @FXML
    protected void filterAndSearchPenalties(){
        setSolveViewPenaltyButtons();

        boolean showingSolved = penaltiesViewOption.getSelectionModel().getSelectedItem().equals("Solved");
        String searchParams = penaltiesSearch.getText();

        /* Show appropriate button */
        if(showingSolved){
            solvePenalty.setVisible(false);
            viewPenalty.setVisible(true);
        }else{
            solvePenalty.setVisible(true);
            viewPenalty.setVisible(false);
        }

        /**
         * TODO: Implement loading/searching penalties
         */
    }

    /**
     * Load penalties from the database according to some parameters and display the results in the table
     * @param params Parameters to load by
     */
    private void loadPenalties(String params){
        /**
         * TODO: Implement loading/searching penalties
         */
    }

    @FXML
    protected void showSolvePenaltyDialog(){

    }

    @FXML
    protected void showViewPenaltyDialog(){

    }

    /**
     * Enable the solve/view penalty buttons if an item in the table is selected, disable otherwise
     */
    @FXML
    protected void setSolveViewPenaltyButtons(){
        boolean itemSelected = !penaltiesTable.getSelectionModel().isEmpty();
        boolean showingSolved = penaltiesViewOption.getSelectionModel().getSelectedItem().equals("Solved");

        if(itemSelected){
            if(showingSolved){
                viewPenalty.setDisable(false);
            }else{
                solvePenalty.setDisable(false);
            }
        }else{
            viewPenalty.setDisable(true);
            solvePenalty.setDisable(true);
        }
    }

    /** REPORTS METHODS **/

    @FXML
    protected void loadGeneralReport(Event e) throws ErrorException {
        barChart.getData().clear();

        String dateString = convertDate(generalDatePicker.getValue());

        //get the data
        String report = "generateDailyGeneralReport";
        String params = "location=1&date=" + dateString;

        try {
            insertDataToBarChart(DataFetcher.getReport(report, params));
            setDropdownOptions();
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    @FXML
    protected void loadSavedGeneralReport(Event e) throws ErrorException {
        barChart.getData().clear();

        String filename = (String) generalReportFilter.getSelectionModel().getSelectedItem();

        try {
            insertDataToBarChart(DataFetcher.getSavedReport(filename));
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    private void insertDataToBarChart(Results res) {
        String[] headers;
        headers = res.getHeaders();

        //input data into chart
        for(int i = 0; i < res.getCols(); i++) {
            XYChart.Series series = new XYChart.Series();
            series.setName(headers[i]);
            series.getData().add(new XYChart.Data(headers[i], Integer.parseInt((String)res.getElement(0, i))));
            barChart.getData().add(series);
        }
    }

    @FXML
    protected void loadRevenueReport(Event e) throws ErrorException {
        revenueGraph.getData().clear();

        //get From and To dates
        String fromDate = convertDate(revenueFromDate.getValue());
        String toDate = convertDate(revenueToDate.getValue());

        //get the data
        String report = "generateRevenueReport";
        String params = "location=1&fromDate=" + fromDate + "&toDate=" + toDate;

        try {
            insertDataToLineGraph(DataFetcher.getReport(report, params));
            setDropdownOptions();
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    @FXML
    protected void loadSavedRevenueReport(Event e) throws ErrorException {
        revenueGraph.getData().clear();

        String filename = (String) generalReportFilter.getSelectionModel().getSelectedItem();

        try {
            insertDataToLineGraph(DataFetcher.getSavedReport(filename));
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    private void insertDataToLineGraph(Results res) {
        String[] headers;
        headers = res.getHeaders();

        rentalXAxis.setLabel("Dates");
        rentalYAxis.setLabel("Revenue in £");

        XYChart.Series series = new XYChart.Series();
        series.setName("Revenue");

        //input data into chart
        for(int i = 0; i < res.getCols(); i++) {
            series.getData().add(new XYChart.Data((String)res.getElement(i, 1), Double.parseDouble((String)res.getElement(i, 0))));
        }
        revenueGraph.getData().add(series);

    }

    private String convertDate(LocalDate d) throws ErrorException {
        //get the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(d.toString());
        } catch (ParseException exc) {
            throw new ErrorException("Could not parse date!", true);
        }
        return dateFormat.format(convertedDate);
    }

}
