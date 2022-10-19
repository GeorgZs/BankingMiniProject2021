package crushers;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    protected Stage stage;

    @Override
    public void start(Stage window) throws IOException {
        window.setResizable(true);
        window.setTitle("PESA!");
        WindowManager.setMainWindow(window);
        WindowManager.showPage(WindowManager.Pages.Login);
        window.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
