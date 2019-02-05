package App.FXControllers;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

import java.util.List;

/**
 * Provides a method for switching between tabs
 */
public class TabSwitcher {
    /**
     * Switches between a given list of tabs, based on a given list of tab buttons and the button which has been clicked.
     * The clicked button has its CSS style class changed to active, and is disabled. All other buttons have their CSS style class changed to inactive and are enabled.
     * The tab associated with the clicked button is made visible, and all others tabs are made invisible.
     * @param tabButtons A list of all tab buttons
     * @param tabs A list of all tabs
     * @param clickedButton The tab button that has been clicked
     */
    public static void switchTab(List<ToggleButton> tabButtons, List<AnchorPane> tabs, ToggleButton clickedButton){
        for(int i = 0; i < tabButtons.size(); i++){
            if(tabButtons.get(i) == clickedButton){
                activateTab(tabButtons.get(i), tabs.get(i));
            }else{
                deactivateTab(tabButtons.get(i), tabs.get(i));
            }
        }
    }

    /**
     * Switches to the first tab
     * @param tabButtons A list of all tab buttons
     * @param tabs A list of all tabs
     */
    public static void setToFirstTab(List<ToggleButton> tabButtons, List<AnchorPane> tabs){
        ToggleButton firstButton = tabButtons.get(0);
        AnchorPane firstTab = tabs.get(0);

        for(int i = 0; i < tabButtons.size(); i++){
            deactivateTab(tabButtons.get(i), tabs.get(i));
        }

        activateTab(firstButton, firstTab);
    }

    /**
     * Activates a tab
     * The "inactive " CSS style is removed from the button and replaced with the "active" CSS style
     * The tab button is set to selected and disabled
     * The associated tab is made visible
     * @param btn
     * @param tab
     */
    private static void activateTab(ToggleButton btn, AnchorPane tab){
        btn.getStyleClass().remove("nav-bar-tab-inactive");
        btn.getStyleClass().add("nav-bar-tab-active");

        if(btn.getId().equals("userTabButton")){
            btn.getStyleClass().remove("account-button-inactive");
            btn.getStyleClass().add("account-button-active");
        }

        btn.setSelected(true);
        btn.setDisable(true);

        tab.setVisible(true);
    }

    /**
     * Deactivates a tab
     * The "active " CSS style is removed from the button and replaced with the "inactive" CSS style
     * The tab button is set to not selected and is enabled
     * The associated tab is made invisible
     * @param btn
     * @param tab
     */
    private static void deactivateTab(ToggleButton btn, AnchorPane tab){
        btn.getStyleClass().remove("nav-bar-tab-active");
        btn.getStyleClass().add("nav-bar-tab-inactive");

        if(btn.getId().equals("userTabButton")){
            btn.getStyleClass().remove("account-button-active");
            btn.getStyleClass().add("account-button-inactive");
        }

        btn.setSelected(false);
        btn.setDisable(false);

        tab.setVisible(false);
    }
}
