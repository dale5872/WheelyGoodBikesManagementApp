//TODO: Refactor to allow EmployeeAccount and Accounts
//TODO: Refactor to avoid duplicate code for inputting data into tables on different views

package App.FXControllers;

import App.Classes.Account;
import App.Classes.EmployeeAccount;
import App.Classes.Location;
import DatabaseConnector.Query;
import DatabaseConnector.Results;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
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

    //Table
    @FXML private TableColumn tableID;
    @FXML private TableColumn tableUsername;
    @FXML private TableColumn tableFirstName;
    @FXML private TableColumn tableLastName;
    @FXML private TableColumn tableEmail;
    @FXML private TableColumn tablePhoneNumber;
    @FXML private TableColumn tableAccountType;
    @FXML private TableColumn tableLocation;
    @FXML private TableView accountsTable;

    //Search Properties
    @FXML private RadioButton managersRadio;
    @FXML private RadioButton operatorsRadio;
    @FXML private RadioButton allRadio;


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

        ObservableList<EmployeeAccount> accounts = fetchAccounts(queryString);

        tableID.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("employeeID")
        );
        tableUsername.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("username")
        );
        tableFirstName.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("firstName")
        );
        tableLastName.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("lastName")
        );
        tableEmail.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("email")
        );
        tablePhoneNumber.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("phoneNumber")
        );
        tableAccountType.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("accType")
        );
        tableLocation.setCellValueFactory(
                new PropertyValueFactory<EmployeeAccount, String>("LocationName")
        );

        accountsTable.setItems(accounts);

    }

    /**
     * Uses the query string to fetch the data from the SQL server and
     * creates many Account / EmployeeAccount objects and parses the data
     * into these objects. Then returns the list of these objects for
     * display into the table
     * @param queryString Query to execute on the SQL Server
     * @return A list of Accounts filled with the data in each account
     */
    private ObservableList<EmployeeAccount> fetchAccounts(String queryString) {
        ObservableList<EmployeeAccount> accounts = FXCollections.observableArrayList();

        Query q = new Query();
        q.updateQuery(queryString);
        Results res = q.executeQuery();

        //check we have results
        if(!res.isEmpty()) {
            //add data
            /*
            TABLE STRUCTURE:
            (employeeID, username, firstName, lastName, email, phone, accountType, location)
             */
            for(int r = 0; r < res.getRows(); r++) {
                EmployeeAccount acc = new EmployeeAccount();

                acc.setEmployeeID((int)res.getElement(r,0));
                acc.setUsername((String)res.getElement(r,1));
                acc.setFirstName((String)res.getElement(r,2));
                acc.setLastName((String)res.getElement(r,3));
                acc.setEmail((String)res.getElement(r,4));
                acc.setPhoneNumber((String)res.getElement(r,5));
                acc.setAccType((String)res.getElement(r,6));
                Location loc = new Location((int)res.getElement(r,7), (String)res.getElement(r,8));
                acc.setLocation(loc);

                accounts.add(acc);
            }
        }

        return accounts;
    }
}
