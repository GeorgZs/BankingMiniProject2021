package crushers.util;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Util {
    
    // public static final FXMLLoader mainLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/MainView.fxml"));
    // public static final FXMLLoader registerLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/RegisterView.fxml"));
    // public static final FXMLLoader accountLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/AccountView.fxml"));
    // public static final FXMLLoader accountCreationLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/AccountCreationView.fxml"));
    // public static final FXMLLoader systemLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/SystemView.fxml"));

    // maybe static controller references?

    // i tried pre-assigning fxmlloaders but "rOoT aLrEaDy AsSiGnEd"
    
    // public static FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/AccountView.fxml"));
    // public static final Parent accountScene = loader.load();

    public static void closeAndShow(String name, String title, ActionEvent e){
        FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/" + name + ".fxml"));
        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    
    }
}
