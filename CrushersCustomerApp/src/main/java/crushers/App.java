package crushers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

import crushers.common.models.*;

public class App extends Application {

    // Declaring quantities that are convenient to store globally
    public static User currentCustomer;
    public static List<BankAccount> currentCustomerAccounts;
    public static List<Contact> currentCustomerContacts;

    public static BankAccount currentAccount;
    public static List<Transaction> currentAccountTransactions;

    public static Transaction currentTransaction;
    
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        configStage(stage, "Crushers Bank");
    } // Loads Log-in window

    public static void configStage(Stage stage, String title){
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle(title);
        stage.show();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        launch(args);
    }

}
