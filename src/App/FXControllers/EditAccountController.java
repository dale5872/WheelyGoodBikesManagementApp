package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.Classes.Location;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.util.HashMap;

public class EditAccountController extends AccountPopupController{
    private EmployeeAccount existingAccount;

    /**
     * Adds the account types and locations to the dropdown boxes
     * @param accountTypes
     * @param locations
     */
    public void setDropdownValues(HashMap<String, String> accountTypes, ObservableList<Location> locations){
        accountTypes.values().remove("User");

        super.setDropdownValues(accountTypes, locations);
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
            /* Create new account and set common  values */
            EmployeeAccount newAccount = new EmployeeAccount();
            super.setCommonValues(newAccount);

            /* Set IDs of new account */
            newAccount.setUserID(existingAccount.getUserID());
            newAccount.setEmployeeID(existingAccount.getEmployeeID());

            /* Get the chosen account type and map it to its system index */
            String chosenAccountType = (String) accountTypeCombo.getValue();
            int accountTypeIndex = Integer.parseInt(accountTypes.get(chosenAccountType));

            /* Get location */
            String locationName = (String) workLocationCombo.getValue();
            Location loc = OptionsList.findLocationByName(locations, locationName);
            newAccount.setLocation(loc);

            /* Pass newAccount back to parent form */
            OperatorSystemController controller = (OperatorSystemController) super.parentController;
            controller.updateAccount(existingAccount, newAccount, accountTypeIndex);

            super.close();
        }
    }

    /**
     * Checks that no fields are blank, and highlight warning label if there are
     * @return
     */
    @SuppressWarnings("Duplicates")
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
     * Checks for any blank controls
     * @return TRUE if there are blank fields, FALSE if there are none
     */
    private boolean checkForBlankFields(){
        if(workLocationCombo.getSelectionModel().isEmpty()){
            return true;
        }

        return checkCommonControlsForBlanks();
    }
}
