package App.FXControllers;

import App.Classes.EmployeeAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
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
    @FXML private ToggleButton logoutButton;
    private List<ToggleButton> tabButtons; //List to store all tab buttons

    @FXML private Pane pinkTab; //Gets the red tab object
    @FXML private Pane orangeTab; //Gets the green tab object
    @FXML private Pane brownTab; //Gets the yellow tab object
    @FXML private Pane blueTab; //Gets the blue tab object
    private List<Pane> tabs; //List to store all tabs;

    @FXML private ToggleButton userButton;

    private static EmployeeAccount employee;

    public void setEmployee(EmployeeAccount e) {
        this.employee = e;
        /* set up welcome message */
        userButton.setText(employee.getName());
    }

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

    /**
     * Logs the user out
     */
    @FXML
    protected void logout(ActionEvent e) {
        this.employee = null;
        try {
            /* Create and show the log in window */
            Parent root = FXMLLoader.load(getClass().getResource("../FXML/LogIn.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Wheely Good Bikes");
            primaryStage.setScene(scene);
            primaryStage.show();

            Stage thisScreen = (Stage) userButton.getScene().getWindow();
            thisScreen.close();

        }catch (IOException ex){
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            System.exit(2);        }
    }
}
