package crushers;

import crushers.model.PaymentAccount;
import crushers.model.Transaction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import com.jfoenix.controls.JFXTextField;

import crushers.model.Customer;


public class App extends Application {

    public static Customer currentCustomer;
    public static PaymentAccount currentAccount;
    public static Transaction currentTransaction;
    public static String currentToken;
    public static int currentAccountID;
    
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    

        /*
        stylesheet
        */

        scene.getStylesheets().add(getClass().getClassLoader().getResource("crushers/stylesheets/main.css").toExternalForm());

        configStage(stage, "Crushers Bank");
    }

    public static void configStage(Stage stage, String title){
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle(title);
        stage.show();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        launch(args);
    }

}