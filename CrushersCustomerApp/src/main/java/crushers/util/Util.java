package crushers.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


import crushers.App;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
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
            ServerFacade.instance.logoutUser();
            App.currentCustomer = null;
            App.currentCustomerAccounts = new ArrayList<>();
            App.currentCustomerContacts = new ArrayList<>();

            App.currentAccount = null;
            App.currentAccountTransactions = new ArrayList<>();

            App.currentTransaction = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Util.closeAndShow("MainView", "Crushers Bank", e);
    }

    public static void updateCustomer() {
        try {
            App.currentCustomer = ServerFacade.instance.getLoggedInCustomer();
        }
        catch (HttpException ex) {
            System.out.println(ex.getError());
            return;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        // Get the customer's accounts
        try {
            App.currentCustomerAccounts = new ArrayList<BankAccount>();
            App.currentCustomerAccounts = ServerFacade.instance.listAllBankAccountsOfThisCustomer();
        }
        catch (HttpException ex) {
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // Get the customer's contacts
        try {
            App.currentCustomerContacts = new ArrayList<Contact>();
            App.currentCustomerContacts = ServerFacade.instance.listAllContactsOfThisCustomer();
        }
        catch (HttpException ex) {
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // Get the selected account's transactions
        try {
            App.currentAccountTransactions = new ArrayList<>();
            App.currentAccountTransactions = ServerFacade.instance.listAllTransactionsOfAccount(App.currentAccount.getId());
        }
        catch (HttpException ex) {
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // Get the loans at the selected account's bank
        ArrayList<BankAccount> loans = App.currentCustomerAccounts.stream()
            .filter(account -> account.getBank().equals(App.currentAccount.getBank()) && account.isLoan())
            .collect(Collectors.toCollection(ArrayList::new));


        System.out.println("Customer: " + App.currentCustomer);
        System.out.println("Contacts: " + App.currentCustomerContacts.size());
        System.out.println("Transactions: " + App.currentAccountTransactions.size());
        System.out.println("Loans: " + loans.size());
    }

    public static double trunc(double toTrunc){
        return Math.floor(toTrunc*100)/100;
    }
}
