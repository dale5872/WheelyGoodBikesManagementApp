package App.FXControllers;

import javafx.scene.control.ToggleButton;

import java.util.List;

/**
 * Provides methods for switching between tabs
 */
public class TabSwitcher {
    private final List<Tab> tabs;
    private Tab activeTab;

    /**
     * Creates a tab switcher object containing the given list of tabs
     * @param tabs A list of Tab objects
     */
    public TabSwitcher(List<Tab> tabs){
        this.tabs = tabs;
    }

    /**
     * Switches to the first tab
     */
    public void switchToFirstTab(){
        activeTab = tabs.get(0);
        activeTab.activateTab();

        for(int i = 1; i < tabs.size(); i++){
            tabs.get(i).deactivateTab();
        }
    }

    /**
     * Switches to the tab of the button that has been clicked.
     * @param clickedButton The tab button that has been clicked
     */
    public void switchToClickedButton(ToggleButton clickedButton){
        for(Tab tab : tabs){
            if(tab.isClickedTab(clickedButton)){
                tab.activateTab();
                activeTab = tab;
            }else{
                tab.deactivateTab();
            }
        }
    }

    /**
     * Go to the child tab of the active tab, if one exists
     */
    public void goToChildTab(){
        activeTab.goToChildTab();
    }

    /**
     * Return to the parent tab if currently at the child tab
     */
    public void returnToParentTab(){
        activeTab.returnToParentTab();
    }
}
