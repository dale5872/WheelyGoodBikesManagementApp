package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Equipment;

import App.Classes.Rental;
import DatabaseConnector.InsertFailedException;
import DatabaseConnector.Results;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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
    @FXML private ToggleButton rentalsTabButton; //Gets the rentals tab button object
    @FXML private ToggleButton reportsTabButton; //Gets the reports tab button object
    @FXML private ToggleButton userTabButton; //Gets the user's account tab button object

    @FXML private AnchorPane bikesTab; //Gets the bikes tab object
    @FXML private AnchorPane rentalsTab; //Gets the rentals tab object
    @FXML private AnchorPane reportsTab; //Gets the reports tab object
    @FXML private AnchorPane userTab; //Gets the user tab object

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
    //Filter and search
    @FXML private ComboBox rentalsFilter;
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
    @FXML private CategoryAxis barXAxis;
    @FXML private NumberAxis barYAxis;
    @FXML private LineChart lineChart;
    @FXML private CategoryAxis lineXAxis;
    @FXML private NumberAxis lineYAxis;

    /** Generate Report **/
    @FXML private DatePicker generateStartDate;
    @FXML private DatePicker generateEndDate;
    @FXML private ComboBox generateReportType;
    @FXML private CheckBox generateCheckbox1;
    @FXML private CheckBox generateCheckbox2;
    @FXML private CheckBox generateCheckbox3;
    @FXML private CheckBox generateCheckbox4;
    @FXML private CheckBox generateCheckbox5;
    @FXML private Button generateReportBtn;

    /** Load Saved Report **/
    @FXML private ComboBox savedReportType;
    @FXML private ComboBox savedReportName;
    @FXML private Button loadReportBtn;

    /**
     * The initialise method is called when the form first loads
     */
    @SuppressWarnings("Duplicates")
    public void initialize(){
        /* Initialise the tab switcher and set to first tab */
        List<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab(bikesTab, bikesTabButton));
        tabs.add(new Tab(rentalsTab, rentalsTabButton));
        tabs.add(new Tab(reportsTab, reportsTabButton));
        tabs.add(new Tab(userTab, userTabButton));

        super.tabSwitcher = new TabSwitcher(tabs);
        super.tabSwitcher.switchToFirstTab();

        setDropdownOptions();

        //hide the graphs
        //barChart.setVisible(false);
        lineChart.setVisible(false);
    }

    @Override
    public void setEmployee(EmployeeAccount e) {
        super.setEmployee(e);
        getReportNames();

        try {
            loadBikes("", "");
            filterAndSearchRentals(null);
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
        ObservableList<String> equipmentFilterOptions = OptionsList.createTypeNameList(bikeTypes);
        equipmentFilterOptions.add(0, "All");
        equipmentFilter.setItems(equipmentFilterOptions);
        equipmentFilter.getSelectionModel().selectFirst();

        //Set the rentals filter dropdown
        ObservableList<String> rentalsOptions = FXCollections.observableArrayList("Bikes", "Other Equipment");
        rentalsFilter.setItems(rentalsOptions);
        rentalsFilter.getSelectionModel().selectFirst();

        //Set report type dropdowns
        ObservableList<String> reportTypes = FXCollections.observableArrayList("General", "Revenue");
        generateReportType.setItems(reportTypes);
        savedReportType.setItems(reportTypes);
        generateReportType.getSelectionModel().selectFirst();
        savedReportType.getSelectionModel().selectFirst();
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
            equipmentFilterOptions = OptionsList.createTypeNameList(bikeTypes);
        }else{ //Showing equipment
            equipmentFilterOptions = OptionsList.createTypeNameList(equipmentTypes);
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

            String params = equipmentSearch.getText();
            String type;
            String selectedItem = (String) equipmentFilter.getSelectionModel().getSelectedItem();

            if(selectedItem.equals("All")){
                type = "";
            }else{
                type = selectedItem;
            }

            boolean showingBikes = equipmentView.getSelectionModel().getSelectedItem().equals("Bikes");
            if(showingBikes){
                loadBikes(type, params);
            }else{ //Showing equipment
                loadEquipment(type, params);
            }
        }
    }

    /**
     * Loads bikes from the database, given a search parameter, and displays it in the equipment table
     * @param type Bike tyoe tp search by
     * @param search Parameters to search by
     */
    private void loadBikes(String type, String search) {
        try {
            ObservableList<Equipment> equipment = DataFetcher.getBikes(this.employee.getLocation(), "search=" + search + "&type=" + type, bikeTypes, locations);
            fillEquipmentTable(equipment);
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    /**
     * Loads equipment from the database, given a search parameter, and displays it in the equipment table
     * @param type Equipment type to filer by
     * @param search Parameters to search by
     */
    private void loadEquipment(String type, String search) {
        try {
            ObservableList<Equipment> equipment = DataFetcher.getEquipment(this.employee.getLocation(), "search=" + search + "&type=" + type, equipmentTypes, locations);
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

    @FXML
    protected void filterAndSearchRentals(Event e) {
        //rentalsTable.getItems().clear();

        String search = rentalSearch.getText();

        boolean showingBikes = rentalsFilter.getSelectionModel().getSelectedItem().equals("Bikes");
        if(showingBikes){
            loadBikeRentals(search);
        }else{
            loadEquipmentRentals(search);
        }

    }

    private void loadBikeRentals(String search) {
        try {
            ObservableList<Rental> rental = DataFetcher.getBikeRentals(this.employee.getLocation(), "search=" + search, bikeTypes, locations);
            fillRentalsTable(rental);
        } catch (EmptyDatasetException | InvalidParametersException | ErrorException e) {
            return;
        }
    }

    private void loadEquipmentRentals(String search) {
        try {
            ObservableList<Rental> rental = DataFetcher.getEquipmentRentals(this.employee.getLocation(), "search=" + search, equipmentTypes, locations);
            fillRentalsTable(rental);
        } catch (EmptyDatasetException |InvalidParametersException | ErrorException e) {
            return;
        }
    }

    protected void fillRentalsTable(ObservableList<Rental> rental) throws ErrorException {
        try {
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
                HashMap<String, String> accountTypes = DataFetcher.getAccountTypes();
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

    /** REPORTS METHODS **/

    @FXML
    protected void generateReport(Event e) throws ErrorException {
        //check what report type we are looking at
        String reportType = (String)generateReportType.getSelectionModel().getSelectedItem();
        switch(reportType) {
            case "General":
                generateGeneralReport();
                break;
            case "Revenue":
                generateRevenueReport();
                break;
            default:
                throw new ErrorException("Unknown error has occurred. Check the logfile", true);
        }
    }

    @FXML
    protected void loadSavedReport(Event e) throws ErrorException, EmptyDatasetException {
        clearAndHideGraphs();

        String reportType = (String) savedReportType.getSelectionModel().getSelectedItem();
        String filename = (String) savedReportName.getSelectionModel().getSelectedItem();

        Results bikeResults, equipmentResults;

        switch(reportType) {
            case "General":
                barChart.setVisible(true);
                bikeResults = DataFetcher.getSavedReport(reportType, this.employee.getLocation().getLocationID(), filename);
                equipmentResults = DataFetcher.getSavedReport(reportType, this.employee.getLocation().getLocationID(), filename + "e");
                insertDataToBarChart(bikeResults, equipmentResults);
                break;
            case "Revenue":
                lineChart.setVisible(true);
                bikeResults = DataFetcher.getSavedReport(reportType, this.employee.getLocation().getLocationID(), filename);
                equipmentResults = DataFetcher.getSavedReport(reportType, this.employee.getLocation().getLocationID(), filename + "e");
                insertDataToLineGraph(bikeResults, equipmentResults);
                break;
        }

    }

    @FXML
    protected void enableDisableGenerateFields(Event e) {
        boolean atLeastOneCheckbox = checkCheckBoxes();
        boolean hasStartDate = (generateStartDate.getValue() != null);
        boolean hasEndDate = (generateEndDate.getValue() != null);
        boolean needsEndDate;

        /* Enable or disable the end date chooser */
        String reportType = (String)generateReportType.getSelectionModel().getSelectedItem();
        switch(reportType) {
            case "General":
                generateEndDate.setDisable(true);
                needsEndDate = false;
                break;
            case "Revenue":
            default:
                generateEndDate.setDisable(false);
                needsEndDate = true;
                break;
        }

        /*
         * The generate button is disabled if AT LEAST ONE of the following is true:
         *  - There is no checkbox selected
         *  - There is no start date selected
         *  - An end date is needed AND none is selected
         * If all are false, the generate button is enabled
         */
        if((atLeastOneCheckbox == false)
                || (hasStartDate == false)
                || ((needsEndDate == true) && (hasEndDate == false))){
            generateReportBtn.setDisable(true);
        }else{
            generateReportBtn.setDisable(false);
        }
    }

    /**
     * Checks that at least one checkbox is selected
     * @return TRUE if at least one checbox is selected, false otherwise
     */
    private boolean checkCheckBoxes(){
        if(generateCheckbox1.isSelected()){
            return true;
        }

        if(generateCheckbox2.isSelected()){
            return true;
        }

        if(generateCheckbox3.isSelected()){
            return true;
        }

        if(generateCheckbox4.isSelected()){
            return true;
        }

        if(generateCheckbox5.isSelected()){
            return true;
        }

        return false;
    }

    @FXML
    protected void getReportNames() {
        savedReportName.setDisable(false);
        enableDisableLoadButton();

        String reportType = (String) savedReportType.getSelectionModel().getSelectedItem();

        //Set saved reports dropdown
        HashMap<String, String> savedReports = DataFetcher.getFilenameDropdownValues(reportType, this.employee.getLocation().getLocationID());
        if(savedReports != null) {
            ObservableList<String> reports = OptionsList.createList(savedReports);
            savedReportName.setItems(reports);
            equipmentFilter.getSelectionModel().selectFirst();
        }else{
            savedReportName.getItems().clear();
        }
    }

    @FXML
    protected void enableDisableLoadButton(){
        boolean reportTypeSelected = !(savedReportType.getSelectionModel().isEmpty());
        boolean reportFileSelected = !(savedReportName.getSelectionModel().isEmpty());

        if(reportTypeSelected && reportFileSelected){
            loadReportBtn.setDisable(false);
        }else{
            loadReportBtn.setDisable(true);
        }
    }

    private void generateGeneralReport() throws ErrorException {
        clearAndHideGraphs();
        barChart.setVisible(true);

        String dateString = convertDate(generateStartDate.getValue());

        //get the data
        String bikeReport = "generateDailyGeneralReport";
        String equipmentReport = "generateDailyGeneralEquipmentReport";
        String params = "location_id=" + this.employee.getLocation().getLocationID() + "&date=" + dateString;

        try {
            insertDataToBarChart(DataFetcher.getReport(bikeReport, params), DataFetcher.getReport(equipmentReport, params));
            //setDropdownOptions();
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    protected void generateRevenueReport() throws ErrorException {
        clearAndHideGraphs();
        lineChart.setVisible(true);

        //get From and To dates
        String fromDate = convertDate(generateStartDate.getValue());
        String toDate = convertDate(generateEndDate.getValue());

        //get the data
        String bikesReport = "generateRevenueReport";
        String equipmentReport = "generateRevenueEquipmentReport";
        String params = "location_id=" + this.employee.getLocation().getLocationID() + "&fromDate=" + fromDate + "&toDate=" + toDate;

        try {
            Results bikeResults = DataFetcher.getReport(bikesReport, params);
            Results equipmentResults = DataFetcher.getReport(equipmentReport, params);
            insertDataToLineGraph(bikeResults, equipmentResults);
            //setDropdownOptions();
        } catch (EmptyDatasetException exc) {
            return;
        }
    }

    private void insertDataToBarChart(Results bikeRes, Results equipmentRes) {
        String[] headers;
        headers = bikeRes.getHeaders(); //headers will be the same for both results

        ObservableList<String> categories = FXCollections.observableArrayList();

        XYChart.Series bikeSeries = new XYChart.Series();
        XYChart.Series equipmentSeries = new XYChart.Series();
        bikeSeries.setName("Bikes");
        equipmentSeries.setName("Equipment");

        /** As this report has the same number of rows, we can input both at the same time **/
        //input data into chart
        for(int i = 0; i < bikeRes.getCols(); i++) {
            bikeSeries.getData().add(new XYChart.Data<String, Integer>(headers[i], Integer.parseInt((String)bikeRes.getElement(0, i))));
            equipmentSeries.getData().add(new XYChart.Data<String, Integer>(headers[i], Integer.parseInt((String)equipmentRes.getElement(0, i))));

            categories.add(headers[i]);
        }

        barXAxis.setCategories(categories);
        barChart.getData().add(bikeSeries);
        barChart.getData().add(equipmentSeries);
    }

    private void insertDataToLineGraph(Results bikeRes, Results equipmentRes) {
        String[] headers;
        headers = bikeRes.getHeaders(); //too many dates to show at once

        lineXAxis.setLabel("Dates");
        lineYAxis.setLabel("Revenue in £");

        lineXAxis.setAutoRanging(true);
        lineYAxis.setAutoRanging(true);

        XYChart.Series bikeSeries = new XYChart.Series();
        XYChart.Series equipmentSeries = new XYChart.Series();
        bikeSeries.setName("Bikes");
        equipmentSeries.setName("Equipment");

        ObservableList<String> categories = FXCollections.observableArrayList();

        //input bike data into chart
        for(int i = 0; i < bikeRes.getRows(); i++) {
            bikeSeries.getData().add(new XYChart.Data<String, Double>((String)bikeRes.getElement(i, "Date"), Double.parseDouble((String)bikeRes.getElement(i, "Revenue"))));
            categories.add((String)bikeRes.getElement(i, "Date"));
        }

        //input equipment data into chart
        for(int i = 0; i < equipmentRes.getRows(); i++) {
            equipmentSeries.getData().add(new XYChart.Data<String, Double>((String)equipmentRes.getElement(i, "Date"), Double.parseDouble((String)equipmentRes.getElement(i, "Revenue"))));
        }

        lineXAxis.setCategories(categories);
        lineChart.getData().add(bikeSeries);
        lineChart.getData().add(equipmentSeries);
        lineChart.autosize();

    }

    private void clearAndHideGraphs() {
        barChart.getData().clear();
        barChart.setVisible(false);
        barXAxis.getCategories().clear();

        lineChart.getData().clear();
        lineChart.setVisible(false);
        lineXAxis.getCategories().clear();
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
