package App.FXControllers;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

public class Tab {
    private final AnchorPane pane;
    private final AnchorPane childPane;
    private final ToggleButton button;

    /**
     * Creates a tab with no child tab.
     * @param pane The tab's AnchorPane
     * @param button The tab's ToggleButton
     */
    public Tab(AnchorPane pane, ToggleButton button){
        this.pane = pane;
        this.button = button;
        this.childPane = null;
    }

    /**
     * Creates a tab with a child tab.
     * @param pane The tab's AnchorPane
     * @param button The tab's ToggleButton
     * @param childPane The child tab's AnchorPane
     */
    public Tab(AnchorPane pane, ToggleButton button, AnchorPane childPane){
        this.pane = pane;
        this.button = button;
        this.childPane = childPane;
    }

    /**
     * Checks if a clicked button is the button of this tab.
     * @param clickedButton
     * @return
     */
    public boolean isClickedTab(ToggleButton clickedButton){
        return button == clickedButton;
    }

    /**
     * Activates this tab.
     * Its button is selected and the tab is set to visible. Any child tab is hidden.
     */
    public void activateTab(){
        button.setSelected(true);

        pane.setVisible(true);

        if(childPane != null){
            childPane.setVisible(false);
        }
    }

    /**
     * Deactivates this tab.
     * Its button is deselected and the tab is set to visible. Any child tab is hidden.
     */
    public void deactivateTab(){
        button.setSelected(false);

        pane.setVisible(false);

        if(childPane != null){
            childPane.setVisible(false);
        }
    }

    /**
     * Activates the child tab, if one exists.
     * The button is selected, and the child tab is set to visible. The parent tab is hidden.
     */
    public void goToChildTab(){
        if(childPane != null){
            button.setSelected(true);

            pane.setVisible(false);
            childPane.setVisible(true);
        }
    }

    /**
     * Returns to the parent tab from the child tab.
     * The button is selected, and the parent tab is set to visible. The child tab is hidden.
     */
    public void returnToParentTab(){
        if(childPane != null){
            button.setSelected(true);

            pane.setVisible(true);
            childPane.setVisible(false);
        }
    }
}
