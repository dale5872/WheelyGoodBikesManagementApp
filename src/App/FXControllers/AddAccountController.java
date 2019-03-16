package App.FXControllers;

import App.Classes.Account;
import App.Classes.EmployeeAccount;
import App.Classes.Location;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class AddAccountController extends AccountPopupController{
    /* Controls */
    @FXML private VBox workLocationBox;

    @FXML private Label passwordWarning;
    @FXML private PasswordField password;
    @FXML private PasswordField reenterPassword;

    /**
     * Adds the account types and locations to the dropdown boxes
     * @param accountTypes
     * @param locations
     */
    public void setDropdownValues(HashMap<String, String> accountTypes, ObservableList<Location> locations){
        super.setDropdownValues(accountTypes, locations);
    }

    /**
     * Handles the account type chosen in the dropdown being changed.
     * If "User" is chosen, the location selection is cleared and the location dropdown disabled.
     * Else, the location dropdown is enabled. If there is an existing account, the location dropdown is set to the value from the existing account.
     */
    @FXML
    protected void changeAccountType(){
        String selectedType = (String) accountTypeCombo.getValue();

        if(selectedType.equals("User")){
            workLocationBox.setVisible(false);
        }else{
            workLocationBox.setVisible(true);
        }
    }

    /**
     * Creates a new account and passes it back to the parent window, either to be added as a new account or to replace an existing account
     */
    @SuppressWarnings("Duplicates")
    @FXML
    protected void confirm(){
        if(validateInput() == true){
            Account newAccount;

            /* Get the chosen account type and map it to its system index */
            String chosenAccountType = (String) accountTypeCombo.getValue();
            int accountTypeIndex = Integer.parseInt(accountTypes.get(chosenAccountType));

            if (chosenAccountType.equals("User")) {
                newAccount = new Account();
                super.setCommonValues(newAccount);
            } else {
                newAccount = new EmployeeAccount();
                super.setCommonValues(newAccount);

                /* Get location */
                String locationName = (String) workLocationCombo.getValue();
                Location loc = OptionsList.findLocationByName(locations, locationName);
                ((EmployeeAccount) newAccount).setLocation(loc);
            }

            /* Pass newAccount back to parent form */
            OperatorSystemController controller = (OperatorSystemController) super.parentController;
            controller.addAccount(newAccount, password.getText(), accountTypeIndex);

            super.close();
        }
    }

    /**
     * Checks that no fields are blank and that the passwords match, showing/highlighting warning labels if not
     * @return
     */
    @SuppressWarnings("Duplicates")
    private boolean validateInput(){
        boolean hasBlankFields = checkForBlankFields();
        boolean passwordsMatch = reenterPassword.getText().equals(password.getText());

        /* Highlight "All fields required" label if there are blank fields, set to normal otherwise */
        if(hasBlankFields){
            allFieldsWarning.getStyleClass().add("warningLabel");
        }else{
            allFieldsWarning.getStyleClass().remove("warningLabel");
        }

        /* Show password mismatch warning if passwords don't match, hide otherwise */
        if(!passwordsMatch){
            passwordWarning.setVisible(true);
        }else{
            passwordWarning.setVisible(false);
        }

        return !hasBlankFields && passwordsMatch;
    }

    /**
     * Checks for any blank controls
     * @return TRUE if there are blank fields, FALSE if there are none
     */
    private boolean checkForBlankFields(){
        if(!accountTypeCombo.getValue().equals("User")) { //Only need to check this if the account is not a user account
            if (workLocationCombo.getSelectionModel().isEmpty()) {
                return true;
            }
        }

        if(password.getText().equals("")){
            return true;
        }

        if(reenterPassword.getText().equals("")){
            return true;
        }

        return checkCommonControlsForBlanks();
    }
}
