package App.FXControllers;

import App.Classes.EmployeeAccount;
import App.JavaFXLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class SystemController extends Controller{
    protected EmployeeAccount employee;

    /* Controls */
    protected List<ToggleButton> tabButtons; //List to store all tab buttons
    protected List<AnchorPane> tabs; //List to store all tabs;

    @FXML private AnchorPane parentPane;

    /** Account Tab **/
    @FXML protected Label userAccountID;
    @FXML protected Label userAccountUsername;
    @FXML protected Label userAccountName;
    @FXML protected Label userAccountType;
    @FXML protected Label userAccountLocation;

    @FXML protected VBox contactDetailsNonEditable;
    @FXML protected Label userAccountEmail;
    @FXML protected Label userAccountPhone;

    @FXML protected VBox contactDetailsEditable;
    @FXML protected TextField userAccountPhoneTextbox;
    @FXML protected TextField userAccountEmailTextbox;

    @FXML protected HBox changeContactContainer;
    @FXML protected Button contactDetailsViewBtn;

    @FXML protected HBox confirmContactContainer;

    public void setEmployee(EmployeeAccount e) {
        this.employee = e;

        //set account labels
        userAccountID.setText("" + employee.getEmployeeID());
        userAccountUsername.setText(employee.getUsername());
        userAccountName.setText(employee.getFirstName() + " " + employee.getLastName());
        userAccountEmail.setText(employee.getEmail());
        userAccountEmailTextbox.setText(employee.getEmail());
        userAccountPhone.setText(employee.getPhoneNumber());
        userAccountPhoneTextbox.setText(employee.getPhoneNumber());
        userAccountType.setText(employee.getAccType());
        userAccountLocation.setText(employee.getLocationName());
    }

    /**
     * Enable the window
     */
    public void enable(){
        parentPane.setDisable(false);
    }

    /**
     * Disable the window
     */
    public void disable(){
        parentPane.setDisable(true);
    }

    /**
     * Handles switching the view of the user's contact details, between editable and non editable
     * @param e
     */
    @FXML
    protected void switchContactDetailsView(ActionEvent e){
        if(e.getSource() == contactDetailsViewBtn){
            contactDetailsNonEditable.setVisible(false);
            changeContactContainer.setVisible(false);

            contactDetailsEditable.setVisible(true);
            confirmContactContainer.setVisible(true);
        }else{
            contactDetailsNonEditable.setVisible(true);
            changeContactContainer.setVisible(true);

            contactDetailsEditable.setVisible(false);
            confirmContactContainer.setVisible(false);
        }
    }

    /**
     * Cancels changing the logged in user's contact details, setting the textboxes back to the stored value and switching back to non-editable view
     * @param e
     */
    @FXML
    protected void cancelChangeContact(ActionEvent e){
        userAccountEmailTextbox.setText(employee.getEmail());
        userAccountPhoneTextbox.setText(employee.getPhoneNumber());

        switchContactDetailsView(e);
    }

    /**
     * Shows the dialog to change the password of the active account
     */
    @FXML
    protected void showChangePasswordDialog(){
        /* Load the popup */
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("ChangePassword", "Change Password", false);

        disable();

        ChangePasswordController controller = (ChangePasswordController) loader.getController();
        controller.setParentController(this);
        controller.setOnCloseAction();
        controller.setUsername(employee.getUsername());
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
    @FXML
    protected void logout(ActionEvent e) {
        new JavaFXLoader().loadNewFXWindow("LogIn", "Wheely Good Bikes", false);

        employee = null;
        super.stage.close();
    }
}
