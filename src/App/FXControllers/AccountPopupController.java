package App.FXControllers;

import App.Classes.Account;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class AccountPopupController extends Controller{
    protected OperatorSystemController parentController;
    protected HashMap<String, String> accountTypes;
    protected HashMap<String, String> locations;

    /**
     * Passes the parent controller in
     * @param controller
     */
    public void setParentController(OperatorSystemController controller){
        this.parentController = controller;
    }

    /**
     * Ensures that the parent window is always re-enabled when this window is closed
     * This method must be called AFTER the Stage and Parent Controller have been set
     */
    public void setOnCloseAction(){
        super.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e){
                parentController.enable();
            }
        });
    }

    /**
     * Adds the account types and getLocations to given dropdown boxes
     * @param accountTypes
     * @param locations
     */
    protected void setDropdownValues(ComboBox accountTypeCombo, ComboBox workLocationCombo,
                                  HashMap<String, String> accountTypes, HashMap<String, String> locations){

        this.accountTypes = accountTypes;
        this.locations = locations;

        ObservableList<String> accountTypeOptions = OptionsListCreator.createList(this.accountTypes);
        accountTypeCombo.setItems(accountTypeOptions);

        ObservableList<String> locationOptions = OptionsListCreator.createList(this.locations);
        workLocationCombo.setItems(locationOptions);
    }

    /**
     * Creates a new account, and sets its values based on controls passed to it
     * @param username A TextField containing the username
     * @param firstName A TextField containing the user's first name
     * @param lastName A TextField containing the user's last name
     * @param email A TextField containing the user's email
     * @param phoneNumber A TextField containing the user's phone number
     * @param accountTypeCombo  A ComboBox containing the account type
     * @return
     */
    protected void setCommonValues(Account account,
                                   TextField username, TextField firstName, TextField lastName, TextField email,
                                   TextField phoneNumber, ComboBox accountTypeCombo){

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
}
