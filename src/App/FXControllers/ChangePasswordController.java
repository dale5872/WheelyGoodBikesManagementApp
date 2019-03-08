package App.FXControllers;

import DatabaseConnector.Authenticate;
import DatabaseConnector.LoginFailedException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ChangePasswordController extends PopupController{
    private String username;

    /* Controls */
    @FXML private PasswordField currentPwordField;
    @FXML private PasswordField newPwordField;
    @FXML private PasswordField newPasswordConfirm;

    @FXML private Label incorrectWarning;
    @FXML private Label confirmationWarning;

    /**
     * Passes in the username of the account to change
     * @param username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Authenticates the current password, checks that the new password and confirmation match, then updates password
     */
    @FXML
    protected void confirm(){
        String currentPassword = currentPwordField.getText();

        try{
            Authenticate.authorize(username, currentPassword);
            incorrectWarning.setVisible(false);

            if(passwordsMatch()){
                String newPassword = newPwordField.getText();

                /**
                 *
                 * TODO: Change the logged in user's password
                 */

                super.close();
            }
        }catch(LoginFailedException e){
            incorrectWarning.setVisible(true);
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
