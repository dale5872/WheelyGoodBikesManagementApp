package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Location;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class EditAccountController extends AccountPopupController{
    private EmployeeAccount existingAccount;

    /* Controls */
    @FXML private Label allFieldsWarning;

    @FXML private TextField username;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField email;
    @FXML private TextField phoneNumber;

    @FXML private ComboBox accountTypeCombo;
    @FXML private ComboBox workLocationCombo;

    /**
     * Adds the account types and locations to the dropdown boxes
     * @param accountTypes
     * @param locations
     */
    public void setDropdownValues(HashMap<String, String> accountTypes, HashMap<String, String> locations){
        accountTypes.values().remove("User");

        super.setDropdownValues(accountTypeCombo, workLocationCombo, accountTypes, locations);
    }

    /**
     * Passes in an existing account, placing its values in the controls
     * @param acc
     */
    public void setExistingAccount(EmployeeAccount acc){
        this.existingAccount = acc;

        /* Display account information in controls */
        username.setText(existingAccount.getUsername());
        firstName.setText(existingAccount.getFirstName());
        lastName.setText(existingAccount.getLastName());
        email.setText(existingAccount.getEmail());
        phoneNumber.setText(existingAccount.getPhoneNumber());

        /* Set account type dropdown */
        accountTypeCombo.getSelectionModel().select(existingAccount.getAccType());

        /* Set location dropdown */
        Location loc = existingAccount.getLocation();
        workLocationCombo.getSelectionModel().select(loc.getName());
    }

    /**
     * Creates a new account and passes it back to the parent window, either to be added as a new account or to replace an existing account
     */
    @SuppressWarnings("Duplicates")
    @FXML
    protected void confirm(){
        if(validateInput() == true){
            /* Create new account and set values */
            EmployeeAccount newAccount = new EmployeeAccount();
            super.setCommonValues(newAccount, username, firstName, lastName, email, phoneNumber, accountTypeCombo);

            /* Get the chosen account type and map it to its system index */
            String chosenAccountType = (String) accountTypeCombo.getValue();
            int accountTypeIndex = Integer.parseInt(accountTypes.get(chosenAccountType));

            /* Get the chosen location, map it to its system index, and set location attribute of newAccount */
            String chosenLocation = (String) workLocationCombo.getValue();
            int locationIndex = Integer.parseInt(locations.get(chosenLocation));
            newAccount.setLocation(chosenLocation, locationIndex);

            /* Pass newAccount back to parent form */
            this.parentController.updateAccount(existingAccount, newAccount, accountTypeIndex);

            /* Close */
            this.parentController.enable();
            this.stage.close();
        }
    }

    /**
     * Checks that no fields are blank, and highlight warning label if there are
     * @return
     */
    private boolean validateInput(){
        boolean hasBlankFields = checkForBlankFields();

        /* Highlight "All fields required" label if there are blank fields, set to normal otherwise */
        if(hasBlankFields){
            allFieldsWarning.getStyleClass().add("warningLabel");
        }else{
            allFieldsWarning.getStyleClass().remove("warningLabel");
        }

        return !hasBlankFields;
    }

    /**
     * Checks for any blank fields
     * Returns TRUE if there are blank fields, FALSE if there are none
     * @return
     */
    @SuppressWarnings("Duplicates")
    private boolean checkForBlankFields(){
        if(username.getText().equals("")){
            return true;
        }

        if(firstName.getText().equals("")){
            return true;
        }

        if(lastName.getText().equals("")){
            return true;
        }

        if(email.getText().equals("")){
            return true;
        }

        if(phoneNumber.getText().equals("")){
            return true;
        }

        if(accountTypeCombo.getSelectionModel().getSelectedIndex() < 0){
            return true;
        }

        if(workLocationCombo.getSelectionModel().getSelectedIndex() < 0){
            return true;
        }

        return false;
    }

    /**
     * Closes the form without making changes
     */
    @FXML
    protected void cancel(){
        this.parentController.enable();
        this.stage.close();
    }
}