package App.FXControllers;

import App.Classes.Account;
import App.Classes.EmployeeAccount;

import App.Classes.Location;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class AddEditAccountController extends Controller{
    private Account existingAccount;
    private OperatorSystemController parentController;
    private HashMap<String, String> accountTypes;
    private HashMap<String, String> locations;

    /* Controls */
    @FXML private TextField username;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField email;
    @FXML private TextField phoneNumber;

    @FXML private ComboBox accountType;
    @FXML private ComboBox workLocation;

    @FXML private PasswordField password;
    @FXML private PasswordField reenterPassword;

    @FXML private Button confirmButton;

    /**
     * Passes the parent controller in
     * @param controller
     */
    public void setParent(OperatorSystemController controller){
        this.parentController = controller;
    }

    /**
     * Adds the account types and locations to the dropdown boxes
     * @param accountTypes
     * @param locations
     */
    public void setDropdownValues(HashMap<String, String> accountTypes, HashMap<String, String> locations){
        this.accountTypes = accountTypes;
        this.locations = locations;


        ObservableList<String> accountTypeOptions = OptionsListCreator.createList(this.accountTypes);
        accountType.setItems(accountTypeOptions);

        ObservableList<String> locationOptions = OptionsListCreator.createList(this.locations);
        workLocation.setItems(locationOptions);
    }

    /**
     * Passes in an existing account, placing its values in the controls
     * @param acc
     */
    public void setExistingAccount(Account acc){
        this.existingAccount = acc;

        /* Display account information in controls */
        username.setText(existingAccount.getUsername());
        firstName.setText(existingAccount.getFirstName());
        lastName.setText(existingAccount.getLastName());
        email.setText(existingAccount.getEmail());
        phoneNumber.setText(existingAccount.getPhoneNumber());

        /* Set dropdowns */
        accountType.getSelectionModel().select(existingAccount.getAccType());
        if(acc instanceof EmployeeAccount){
            Location loc = ((EmployeeAccount) existingAccount).getLocation();
            workLocation.getSelectionModel().select(loc.getName());
        }else{
            workLocation.setDisable(true);
        }

        /* Hide password fields */
        password.setVisible(false);
        reenterPassword.setVisible(false);

        /* Change confirm button text */
        confirmButton.setText("Update");
    }

    /**
     * Handles the account type chosen in the dropdown being chosen.
     * If "User" is chosen, the location selection is cleared and the location dropdown disabled.
     * Else, the location dropdown is enabled. If there is an existing account, the location dropdown is set to the value from the existing account.
     */
    @FXML
    protected void changeAccountType(){
        String selectedType = (String) accountType.getValue();

        if(selectedType.equals("User")){
            workLocation.getSelectionModel().clearSelection();
            workLocation.setDisable(true);
        }else{
            if(existingAccount != null){
                Location loc = ((EmployeeAccount) existingAccount).getLocation();
                workLocation.getSelectionModel().select(loc.getName());
            }
            workLocation.setDisable(false);
        }
    }

    /**
     * Creates a new account and passes it back to the parent window, either to be added as a new account or to replace an existing account
     */
    @FXML
    protected void confirm(){
        /**
         * TODO: Input validation
         * BODY: Check that values have been entered for all fields, and that the passwords match
         */

        Account newAccount;

        /* Get the chosen account type and map it to its system index */
        String chosenAccountType = (String) accountType.getValue();
        int accountTypeIndex = Integer.parseInt(accountTypes.get(chosenAccountType));

        if(chosenAccountType.equals("User")){
            newAccount = new Account();
            setCommonValues(newAccount);
        }else{
            newAccount = new EmployeeAccount();
            setCommonValues(newAccount);

            /* Get the chosen location, map it to its system index, and set location attribute of newAccount */
            String chosenLocation = (String) workLocation.getValue();
            int locationIndex = Integer.parseInt(locations.get(chosenLocation));
            ((EmployeeAccount) newAccount).setLocation(chosenLocation, locationIndex);
        }

        /* Pass newAccount back to parent form */
        if(existingAccount == null){ //If there is no existing account, we're adding a new one
            this.parentController.addAccount(newAccount, password.getText(), accountTypeIndex);
        }else{ //Else, editing existing account
            this.parentController.updateAccount(existingAccount, newAccount, accountTypeIndex);
        }

        this.stage.close();
    }

    /**
     * Sets the values that are common to both user and employee accounts
     */
    private void setCommonValues(Account account){
        account.setUsername(username.getText());
        account.setFirstName(capitalise(firstName.getText()));
        account.setLastName(capitalise(lastName.getText()));
        account.setEmail(email.getText());
        account.setPhoneNumber(phoneNumber.getText());
    }

    /**
     * Capitalise a string
     * @param string
     * @return
     */
    private String capitalise(String string){
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * Closes the form without making changes
     */
    @FXML
    protected void cancel(){
        parentController.enable();
        this.stage.close();
    }
}
