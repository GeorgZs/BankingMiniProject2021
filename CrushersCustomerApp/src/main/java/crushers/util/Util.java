package crushers.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import crushers.App;
import crushers.model.Contact;
import crushers.model.Customer;
import crushers.model.PaymentAccount;
import crushers.model.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
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

    public static void closeAndShow(String name, String title, ActionEvent e){ // closes e stage and shows new stage
        FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/" + name + ".fxml"));
        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static Stage closeAndCreate(String name, String title, ActionEvent e){ // closes e stage and creates new stage
        FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/" + name + ".fxml"));
        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle(title);
            stage.setResizable(false);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return stage;
    }

    public static void showModal(String name, String title, ActionEvent e){ // consumes e and shows modal stage
        FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/" + name + ".fxml"));
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    public static void createStage(String name, String title, ActionEvent e){
        FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/" + name + ".fxml"));
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void logOutAlert(String title, String body, ActionEvent e){
        Alert alert = new Alert(AlertType.CONFIRMATION, body);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setX(500);
        alert.setY(250);
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.CANCEL){
            return;
        }

        try {
            Http.authPost("auth/logout", App.currentToken, "");
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Util.closeAndShow("MainView", "Crushers Bank", e);
    }

    public static void updateCustomer() {
        try {
            Customer customer = Json.parse(Http.authGet("customers/@me", App.currentToken), Customer.class);
            ArrayList<PaymentAccount> accounts = Json.parseList(Http.authGet("accounts/@me", App.currentToken), PaymentAccount.class);
            ArrayList<Transaction> transactions = Json.parseList(Http.authGet("transactions/accounts/" + App.currentAccountID, App.currentToken), Transaction.class);
            ArrayList<Contact> contacts = Json.parseList(Http.authGet("customers/@contacts", App.currentToken), Contact.class);
            if(accounts == null){
                accounts = new ArrayList<>();
            }
            if(contacts == null){
                contacts = new ArrayList<>();
            }
            if(transactions == null){
                transactions = new ArrayList<>();
            }
            customer.setAccountList(accounts);
            customer.setContactList(contacts);
            customer.getAccountWithId(App.currentAccountID).setTransactions(transactions);
            App.currentCustomer = customer;
            App.currentAccount.setBalance(App.currentCustomer.getAccountWithId(App.currentAccountID).getBalance());
            // System.out.println("Customer: " + customer);
            // System.out.println("Contacts: " + contacts);
            // System.out.println("Location: http://localhost:8080/" + "transactions/accounts/" + App.currentAccountID);
            // System.out.println("Token: " + App.currentToken);
            // System.out.println("Transactions: " + transactions);
            // System.out.println("Loans: " + App.currentCustomer.getLoans());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}
