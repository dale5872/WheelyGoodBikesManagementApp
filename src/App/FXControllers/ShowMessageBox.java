package App.FXControllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShowMessageBox {

    public void show(String message) {
        try{
            String filename = "MessageBox";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/" + filename + ".fxml"));
            Parent root = loader.load();
            MessageBoxController controller = loader.getController();
            controller.setMessage(message);

            Scene scene = new Scene(root);
            Stage managerWindow = new Stage();
            managerWindow.setTitle("Wheely Good Bikes");
            managerWindow.setScene(scene);
            managerWindow.show();

        }catch(Exception e){
            System.out.print(e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
}
