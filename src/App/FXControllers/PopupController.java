package App.FXControllers;

import javafx.fxml.FXML;

public class PopupController extends Controller{
    protected SystemController parentController;

    /**
     * Sets and disables the parent controller
     * Sets the on close action to ensure that the parent controller is re-enabled on closing the pop-up
     * Sets the pop-up to always be on top
     * @param controller The parent controller
     */
    public void prepare(SystemController controller){
        this.parentController = controller;
        this.parentController.disable();

        super.stage.setOnCloseRequest(e -> close());
        super.stage.setAlwaysOnTop(true);
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
