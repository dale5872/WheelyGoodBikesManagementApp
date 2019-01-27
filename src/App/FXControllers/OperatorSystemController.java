package App.FXControllers;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the operator system
 * This class contains methods for all event handling on the operator system
 */
public class OperatorSystemController {
    @FXML private ToggleButton redTabButton; //Gets the red tab button object
    @FXML private ToggleButton greenTabButton; //Gets the green tab button object
    @FXML private ToggleButton yellowTabButton; //Gets the yellow tab button object
    @FXML private ToggleButton blueTabButton; //Gets the blue tab button object
    private List<ToggleButton> tabButtons; //List to store all tab buttons

    @FXML private Pane redTab; //Gets the red tab object
    @FXML private Pane greenTab; //Gets the green tab object
    @FXML private Pane yellowTab; //Gets the yellow tab object
    @FXML private Pane blueTab; //Gets the blue tab object
    private List<Pane> tabs; //List to store all tabs;

    /**
     * The initialise method is called when the form first loads
     */
    @SuppressWarnings("Duplicates")
    public void initialize(){
        /* Initialise the tabButtons list and add all tab buttons to it */
        tabButtons = new ArrayList<>();
        tabButtons.add(redTabButton);
        tabButtons.add(greenTabButton);
        tabButtons.add(yellowTabButton);
        tabButtons.add(blueTabButton);

        /* Initialise the tabs list and add all tabs to it */
        tabs = new ArrayList<>();
        tabs.add(redTab);
        tabs.add(greenTab);
        tabs.add(yellowTab);
        tabs.add(blueTab);
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
}
