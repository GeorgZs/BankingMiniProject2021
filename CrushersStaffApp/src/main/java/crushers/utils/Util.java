package crushers.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Util {
    public static void showModal(String name, String title, MouseEvent e) { // consumes e and shows modal stage
        FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/gui/" + name + ".fxml"));
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            //stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}
