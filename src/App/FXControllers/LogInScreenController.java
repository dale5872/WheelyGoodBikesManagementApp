package App.FXControllers;

import App.Classes.EmployeeAccount;
import DatabaseConnector.Authenticate;
import DatabaseConnector.LoginFailedException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Controller for the login window
 */
public class LogInScreenController {
    @FXML private TextField usernameField; //Get the username TextField object from the FXML
    @FXML private PasswordField passwordField; //Get the PasswordField object from the FXML
    @FXML private Label incorrectCredentials; //Get the incorrect credentials Label object from the FXML

    /**
     * Handles the log in button being pressed
     * First authenticates the username and password entered
     * If credentials are authenticated, creates and opens the main window, closing the log in window
     */
    @FXML
    protected void login(){

        incorrectCredentials.setVisible(false);
        String username = usernameField.getText();
        String password = passwordField.getText();

        /* To test UI with DATABASE */
        try {
            EmployeeAccount activeAccount = Authenticate.authorize(username, password);
            createAndShowMainWindow(activeAccount);
        } catch (LoginFailedException e) {
            incorrectCredentials.setVisible(true);
        }
    }

    /**
     * Creates and opens the appropriate main window - depending on whether the employee is a manager or an operator
     * @param activeAccount
     */
    private void createAndShowMainWindow(EmployeeAccount activeAccount){
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
                /**
                 * TODO reject log in
                 * BODY As the user's account is not a manager or operator, reject the login with an error message
                 */
                    filename = "";
                break;
        }

        try{
/**
            Parent root = FXMLLoader.load(getClass().getResource("../FXML/" + filename + ".fxml"));
            Scene scene = new Scene(root);
            Stage managerWindow = new Stage();
            managerWindow.setTitle("Wheely Good Bikes");
            managerWindow.setScene(scene);
            managerWindow.setMaximized(true);
            managerWindow.show();
*/

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
