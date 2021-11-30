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
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import crushers.model.Bank;
import crushers.model.Customer;
import crushers.model.PaymentAccount;
import crushers.model.SavingsAccount;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;

public class App extends Application {

    public static Bank crushersBank = new Bank("Crushers", "Crushers Bank By People For People", null);
    public static Bank swedbank = new Bank("Swedbank", "#1 Bank of Latvia", null);
    public static ArrayList<Bank> banks = new ArrayList<Bank>(List.of(crushersBank, swedbank));

    public static Customer currentCustomer;

    static Customer emptyCustomer = new Customer("", "", "", "", "", new ArrayList<String>(List.of("test")));
    @Override
    public void start(Stage stage) throws IOException {

        // Registering local defaults
        ArrayList<String> securityQA = new ArrayList<String>(Arrays.asList("What's the name of your first pet?", "Alfie"));
        Customer defaultCustomer = new Customer("John", "Smith", "Willy Street", "smith@google.com", "password", securityQA);
        Customer emptyCustomer = new Customer("", "", "", "", "", securityQA);
    
        defaultCustomer.createAccount(new PaymentAccount("Education Fund", 0.01, crushersBank));
        defaultCustomer.createAccount(new PaymentAccount("Moolah", 7832, crushersBank));
        emptyCustomer.createAccount(new SavingsAccount("Kaylee's funds", 10, 1000, swedbank));
        crushersBank.registerCustomer(defaultCustomer);
        crushersBank.registerCustomer(emptyCustomer);
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

    public static void main(String[] args) {
        launch(args);
        
    }

}