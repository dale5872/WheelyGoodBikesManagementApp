package App.FXControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the manager system
 * This class contains methods for all event handling on the manager system
 */
public class ManagerSystemController {
    @FXML private ToggleButton pinkTabButton; //Gets the red tab button object
    @FXML private ToggleButton orangeTabButton; //Gets the green tab button object
    @FXML private ToggleButton brownTabButton; //Gets the yellow tab button object
    @FXML private ToggleButton purpleTabButton; //Gets the blue tab button object
    private List<ToggleButton> tabButtons; //List to store all tab buttons

    @FXML private Pane pinkTab; //Gets the red tab object
    @FXML private Pane orangeTab; //Gets the green tab object
    @FXML private Pane brownTab; //Gets the yellow tab object
    @FXML private Pane blueTab; //Gets the blue tab object
    private List<Pane> tabs; //List to store all tabs;

    /**
     * The initialise method is called when the form first loads
     */
    @SuppressWarnings("Duplicates")
    public void initialize(){
        /* Initialise the tabButtons list and add all tab buttons to it */
        tabButtons = new ArrayList<>();
        tabButtons.add(pinkTabButton);
        tabButtons.add(orangeTabButton);
        tabButtons.add(brownTabButton);
        tabButtons.add(purpleTabButton);

        /* Initialise the tabs list and add all tabs to it */
        tabs = new ArrayList<>();
        tabs.add(pinkTab);
        tabs.add(orangeTab);
        tabs.add(brownTab);
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
