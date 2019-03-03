package App.FXControllers;

import javafx.fxml.FXML;

public class PopupController extends Controller{
    protected OperatorSystemController parentController;

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
        super.stage.setOnCloseRequest(e -> close());
    }

    /**
     * Closes the form without making changes
     */
    @FXML
    protected void close(){
        parentController.enable();
        super.stage.close();
    }
}
