package crushers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    protected Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        final FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/main.fxml"));
        final Parent root = fxmlLoader.load();

        final Scene scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}