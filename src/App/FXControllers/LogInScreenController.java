package App.FXControllers;

import App.Classes.EmployeeAccount;
import DatabaseConnector.Authenticate;
import DatabaseConnector.LoginFailedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/**
 * Controller for the login window
 */
public class LogInScreenController {
    @FXML private TextField usernameField; //Get the username TextField object from the FXML
    @FXML private PasswordField passwordFieldHidden; //Get the PasswordField object from the FXML
    @FXML private TextField passwordFieldVisible; //Get the visible password field object from the FXML
    @FXML private Label incorrectCredentials; //Get the incorrect credentials Label object from the FXML
    @FXML private Button showHidePasswordButton; //Get the show/hide password button from the FXML

    /**
     * When the password is changed in either the hidden or visible field, this method changes the other field to match
     */
    @FXML
    protected void passwordChanged(KeyEvent e){
        if(e.getSource().equals(passwordFieldHidden)){
            passwordFieldVisible.setText(passwordFieldHidden.getText());
        }else{
            passwordFieldHidden.setText(passwordFieldVisible.getText());
        }
    }

    /**
     * Shows or hides the password
     */
    @FXML
    protected void showHidePassword(){
        if(passwordFieldHidden.isVisible()){
            passwordFieldHidden.setVisible(false);
            passwordFieldVisible.setVisible(true);
            showHidePasswordButton.setText("Hide");
        }else{
            passwordFieldHidden.setVisible(true);
            passwordFieldVisible.setVisible(false);
            showHidePasswordButton.setText("Show");
        }
    }

    /**
     * Handles the log in button being pressed
     * First authenticates the username and password entered
     * If credentials are authenticated, creates and opens the main window, closing the log in window
     */
    @FXML
    protected void login(){

        incorrectCredentials.setVisible(false);
        String username = usernameField.getText();
        String password = passwordFieldHidden.getText();

        /* To test UI with DATABASE */
        try {
            EmployeeAccount activeAccount = Authenticate.authorize(username, password);
            createAndShowMainWindow(activeAccount);
        } catch (LoginFailedException e) {
            incorrectCredentials.setVisible(true);
        } catch (InvalidParametersException e) {
            incorrectCredentials.setVisible(true);
        }
    }

    /**
     * Creates and opens the appropriate main window - depending on whether the employee is a manager or an operator
     * @param activeAccount
     */
    private void createAndShowMainWindow(EmployeeAccount activeAccount) throws InvalidParametersException {
        String filename; //The name of the appropriate FXML file

        /*
         * renamed from getEmployeeType() to getAccType(). This should be clarified later
         * We have 3 account types, 'Manager', 'Operator', 'Admin'
         */
        switch(activeAccount.getAccType()){
            case "Manager":
                filename = "ManagerSystem";
                break;
            case "Operator":
                filename = "OperatorSystem";
                break;
            default: //Account is not a manager or operator - reject  log in
                throw new InvalidParametersException("Unknown error occurred when authenticating user.");
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/" + filename + ".fxml"));
            Parent root = loader.load();
            if(filename.equals("ManagerSystem")) {
                ManagerSystemController controller = loader.getController();
                controller.setEmployee(activeAccount);
            } else if(filename.equals("OperatorSystem")) {
                OperatorSystemController controller = loader.getController();
                controller.setEmployee(activeAccount);
            }
            Scene scene = new Scene(root);
            Stage managerWindow = new Stage();
            managerWindow.setTitle("Wheely Good Bikes");
            managerWindow.setScene(scene);
            managerWindow.setMaximized(true);
            managerWindow.show();

            closeLoginScreen();
        }catch(Exception e){
            System.out.print(e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Closes the log in screen
     */
    private void closeLoginScreen(){
        Stage logInScreen = (Stage) usernameField.getScene().getWindow();
        logInScreen.close();
    }
}
