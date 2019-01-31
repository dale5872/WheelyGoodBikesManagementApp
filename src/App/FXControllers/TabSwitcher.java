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
            ToggleButton btn = tabButtons.get(i);

            if(btn == clickedButton){
                /* For the clicked button:
                    - remove the "inactive" CSS style from the button and replace with the "active" CSS style
                    - disable the button for that tab
                    - make the associated tab visible
                 */
                btn.getStyleClass().remove("nav-bar-tab-inactive");
                btn.getStyleClass().add("nav-bar-tab-active");

                if(btn.getId().equals("userTabButton")){
                    btn.getStyleClass().remove("account-button-inactive");
                    btn.getStyleClass().add("account-button-active");
                }

                btn.setDisable(true);

                tabs.get(i).setVisible(true);
            }else{
                /* For all other buttons:
                    - remove the "active" CSS style from the button (if present) and replace with the "inactive" CSS style
                    - enabled the button for that tab
                    - make the associated tab invisible
                 */
                btn.getStyleClass().remove("nav-bar-tab-active");
                btn.getStyleClass().add("nav-bar-tab-inactive");

                if(btn.getId().equals("userTabButton")){
                    btn.getStyleClass().remove("account-button-active");
                    btn.getStyleClass().add("account-button-inactive");
                }

                btn.setDisable(false);

                tabs.get(i).setVisible(false);
            }
        }
    }
}
