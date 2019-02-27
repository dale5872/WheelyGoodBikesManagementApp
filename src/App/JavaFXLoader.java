package App;

import App.FXControllers.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Acts as a wrapper class for methods to load a JavaFX window
 */
public class JavaFXLoader {
    Controller controller;

    /**
     * Loads a new JavaFX window, given the filename of the FXML file
     * @param filename The filename of the FXML file to use
     * @param title The title of the window
     * @param maximised Whether the window should be maximised
     */
    public void loadNewFXWindow(String filename, String title, boolean maximised){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/" + filename + ".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            prepareStage(stage, scene, title, maximised);

            this.controller = loader.getController();
            this.controller.setStage(stage);
        }catch(Exception ex){
            /**
             * TODO: Proper error handling for when FXML files can't be found/loaded
             * BODY: Critical error
             */
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Loads a new JavaFX window, given an existing Stage and the filename of the FXML file
     * @param stage The existing stage
     * @param filename The filename of the FXML file to use
     * @param title The title of the window
     * @param maximised Whether the window should be maximised
     */
    public void loadNewFXWindow(Stage stage, String filename, String title, boolean maximised){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/" + filename + ".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            prepareStage(stage, scene, title, maximised);

            this.controller = loader.getController();
            this.controller.setStage(stage);
        }catch(Exception ex){
            /**
             * TODO: Proper error handling for when FXML files can't be found/loaded
             * BODY: Critical error
             */
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Sets the title and scene of the stage, and whether the stage should be maximised, then makes the stage visible
     * @param stage
     * @param scene
     * @param title
     * @param maximised
     */
    private void prepareStage(Stage stage, Scene scene, String title, boolean maximised){
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(maximised);
        stage.show();
    }

    /**
     * Returns the controller from the loaded window
     * @return
     */
    public Controller getController(){
        return this.controller;
    }
}
