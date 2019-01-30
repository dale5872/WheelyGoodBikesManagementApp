package App.FXControllers;

import App.Classes.EmployeeAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    @FXML private ToggleButton bikesTabButton; //Gets the bikes tab button object
    @FXML private ToggleButton penaltiesTabButton; //Gets the penalties tab button object
    @FXML private ToggleButton reportsTabButton; //Gets the reports tab button object
    @FXML private ToggleButton userTabButton; //Gets the user's account tab button object
    private List<ToggleButton> tabButtons; //List to store all tab buttons

    @FXML private Pane bikesTab; //Gets the bikes tab object
    @FXML private Pane penaltiesTab; //Gets the penalties tab object
    @FXML private Pane reportsTab; //Gets the reports tab object
    @FXML private Pane userTab; //Gets the user tab object
    private List<Pane> tabs; //List to store all tabs;

    private static EmployeeAccount employee;

    public void setEmployee(EmployeeAccount e) {
        this.employee = e;
    }

    /**
     * The initialise method is called when the form first loads
     */
    @SuppressWarnings("Duplicates")
    public void initialize(){
        /* Initialise the tabButtons list and add all tab buttons to it */
        tabButtons = new ArrayList<>();
        tabButtons.add(bikesTabButton);
        tabButtons.add(penaltiesTabButton);
        tabButtons.add(reportsTabButton);
        tabButtons.add(userTabButton);

        /* Initialise the tabs list and add all tabs to it */
        tabs = new ArrayList<>();
        tabs.add(bikesTab);
        tabs.add(penaltiesTab);
        tabs.add(reportsTab);
        tabs.add(userTab);

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
    @SuppressWarnings("Duplicates")
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

            Stage thisScreen = (Stage) userTabButton.getScene().getWindow();
            thisScreen.close();

        }catch (IOException ex){
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            System.exit(2);
        }
    }
}
