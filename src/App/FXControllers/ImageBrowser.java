package App.FXControllers;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ImageBrowser {
    private final FileChooser fileChooser;
    private File image;

    /**
     * Creates a file chooser that can select .jpeg, .jpg, or .png files
     * @param title The title of the browser
     */
    public ImageBrowser(String title){
        fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpeg", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    /**
     * Shows the file chooser
     * @param stage The stage of the browser
     */
    public void showBrowser(Stage stage){
        image = fileChooser.showOpenDialog(stage);
    }

    /**
     * Gets the path of the image chosen in the browser
     * @return A String of the absolute path to the image, NULL if no image has been chosen
     */
    public String getImagePath(){
        if(image != null) {
            return this.image.getAbsolutePath();
        }else{
            return null;
        }
    }
}
