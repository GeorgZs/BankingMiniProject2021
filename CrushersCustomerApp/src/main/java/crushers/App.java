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

        ArrayList<String> securityQA = new ArrayList<String>(Arrays.asList("What's the name of your first pet?", "Alfie"));
        User admin = new User("John", "Smith", "Willy Street", "smith@google.com", "password", securityQA, 333);
        bank.registerUser(admin);

        Parent root = FXMLLoader.load(getClass().getResource("views/MainView.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);

        scene.getStylesheets().add(getClass().getClassLoader().getResource("crushers/stylesheets/main.css").toExternalForm());

        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Crushers Bank");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}