package App.FXControllers;

import App.Classes.Account;
import App.Classes.Location;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class AccountPopupController extends PopupController {
    protected HashMap<String, String> accountTypes;
    protected ObservableList<Location> locations;

    /* Controls */
    @FXML protected Label allFieldsWarning;

    @FXML protected TextField username;
    @FXML protected TextField firstName;
    @FXML protected TextField lastName;
    @FXML protected TextField email;
    @FXML protected TextField phoneNumber;

    @FXML protected ComboBox accountTypeCombo;
    @FXML protected ComboBox workLocationCombo;

    /**
     * Adds the account types and locations to given dropdown boxes
     * @param accountTypes
     * @param locations
     */
    protected void setDropdownValues(HashMap<String, String> accountTypes, ObservableList<Location> locations){
        this.accountTypes = accountTypes;
        this.locations = locations;

        ObservableList<String> accountTypeOptions = OptionsList.createList(this.accountTypes);
        accountTypeCombo.setItems(accountTypeOptions);
        accountTypeCombo.getSelectionModel().selectFirst();

        ObservableList<String> locationOptions = OptionsList.createLocationNameList(this.locations);
        workLocationCombo.setItems(locationOptions);
    }

    /**
     * Creates a new account, and sets its values based on controls passed to it
     * @param account The account to set the values of
     * @return
     */
    protected void setCommonValues(Account account){
        account.setUsername(username.getText());
        account.setFirstName(capitalise(firstName.getText()));
        account.setLastName(capitalise(lastName.getText()));
        account.setEmail(email.getText());
        account.setPhoneNumber(phoneNumber.getText());
        account.setAccType((String) accountTypeCombo.getValue());
    }

    /**
     * Capitalise a string
     * @param string
     * @return
     */
    protected String capitalise(String string){
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * Checks for any blank controls
     * @return TRUE if there are blank fields, FALSE if there are none
     */
    protected boolean checkCommonControlsForBlanks(){
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

        if(accountTypeCombo.getSelectionModel().isEmpty()){
            return true;
        }

        return false;
    }
}
