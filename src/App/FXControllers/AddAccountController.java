package App.FXControllers;

import App.Classes.Account;
import App.Classes.EmployeeAccount;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class AddAccountController extends AccountPopupController{
    /* Controls */
    @FXML private Label allFieldsWarning;

    @FXML private TextField username;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField email;
    @FXML private TextField phoneNumber;

    @FXML private ComboBox accountTypeCombo;

    @FXML private VBox workLocationBox;
    @FXML private ComboBox workLocationCombo;

    @FXML private Label passwordWarning;
    @FXML private PasswordField password;
    @FXML private PasswordField reenterPassword;

    /**
     * Adds the account types and locations to the dropdown boxes
     * @param accountTypes
     * @param locations
     */
    public void setDropdownValues(HashMap<String, String> accountTypes, HashMap<String, String> locations){
        super.setDropdownValues(accountTypeCombo, workLocationCombo, accountTypes, locations);
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
                super.setCommonValues(newAccount, username, firstName, lastName, email, phoneNumber, accountTypeCombo);
            } else {
                newAccount = new EmployeeAccount();
                super.setCommonValues(newAccount, username, firstName, lastName, email, phoneNumber, accountTypeCombo);

                /* Get the chosen location, map it to its system index, and set location attribute of newAccount */
                String chosenLocation = (String) workLocationCombo.getValue();
                int locationIndex = Integer.parseInt(locations.get(chosenLocation));
                ((EmployeeAccount) newAccount).setLocation(chosenLocation, locationIndex);
            }

            /* Pass newAccount back to parent form */
            this.parentController.addAccount(newAccount, password.getText(), accountTypeIndex);

            /* Close */
            this.parentController.enable();
            this.stage.close();
        }
    }

    /**
     * Checks that no fields are blank and that the passwords match, showing/highlighting warning labels if not
     * @return
     */
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

        if(!accountTypeCombo.getValue().equals("User")) { //Only need to check this if the account is not a user account
            if (workLocationCombo.getSelectionModel().getSelectedIndex() < 0) {
                return true;
            }
        }

        if(password.getText().equals("")){
            return true;
        }

        if(reenterPassword.getText().equals("")){
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
