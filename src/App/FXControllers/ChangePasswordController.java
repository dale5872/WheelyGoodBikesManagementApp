package App.FXControllers;

import App.Classes.EmployeeAccount;
import DatabaseConnector.Authenticate;
import DatabaseConnector.InsertFailedException;
import DatabaseConnector.LoginFailedException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import static App.FXControllers.DataFetcher.updateEmployeePassword;

public class ChangePasswordController extends PopupController{
    private EmployeeAccount account;

    /* Controls */
    @FXML private PasswordField currentPwordField;
    @FXML private PasswordField newPwordField;
    @FXML private PasswordField newPasswordConfirm;

    @FXML private Label incorrectWarning;
    @FXML private Label confirmationWarning;

    /**
     * Passes in the account to change
     * @param account
     */
    public void setAccount(EmployeeAccount account){
        this.account = account;
    }

    /**
     * Authenticates the current password, checks that the new password and confirmation match, then updates password
     */
    @FXML
    protected void confirm(){
        String currentPassword = currentPwordField.getText();

        try{
            Authenticate.authorize(account.getUsername(), currentPassword);
            incorrectWarning.setVisible(false);

            if(passwordsMatch()){
                String newPassword = newPwordField.getText();

                updateEmployeePassword(this.account, newPassword);

                super.close();
            }
        }catch(LoginFailedException e){
            incorrectWarning.setVisible(true);
        } catch (InsertFailedException e) {
            //do nothing
        }
    }

    /**
     * Checks that the new password and the confirmation match, show warning label if not
     * @return
     */
    private boolean passwordsMatch(){
        String newPassword = newPwordField.getText();
        String confirmPassword = newPasswordConfirm.getText();

        if(!confirmPassword.equals(newPassword)){
            confirmationWarning.setVisible(true);
            return false;
        }else{
            confirmationWarning.setVisible(false);
            return true;
        }
    }
}
