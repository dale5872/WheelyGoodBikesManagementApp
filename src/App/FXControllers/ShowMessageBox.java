package App.FXControllers;

import App.JavaFXLoader;

public class ShowMessageBox {
    public void show(String message) {
        JavaFXLoader loader = new JavaFXLoader();
        loader.loadNewFXWindow("MessageBox", "Wheely Good Bikes", false);

        MessageBoxController controller = (MessageBoxController) loader.getController();
        controller.setMessage(message);
    }
}
