package crushers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    protected Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(true);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getRoot().setStyle("-fx-font-family: SansSerif");
        stage.setTitle("PESA!");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
