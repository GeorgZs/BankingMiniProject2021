package crushers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import crushers.model.Bank;
import crushers.model.User;

public class App extends Application {

    public static Bank bank = new Bank("Crushers Bank");

    @Override
    public void start(Stage stage) throws IOException {

        User admin = new User("Jesus", "Christ", "Heaven", "christ@bible.com", "faith", new ArrayList<String>(Arrays.asList("T")), "333");
        bank.registerUser(admin);

        Parent root = FXMLLoader.load(getClass().getResource("views/MainView.fxml"));

        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Crushers Bank");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}