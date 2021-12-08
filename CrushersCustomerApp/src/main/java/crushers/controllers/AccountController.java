package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;

import crushers.App;
import crushers.model.PaymentAccount;
import crushers.model.SavingsAccount;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AccountController implements Initializable{
    
    private Stage stage;
    private Parent root;
    double customerTotalBalance;
    @FXML
    static AnchorPane accountAnchor;
    @FXML
    private ListView<PaymentAccount> accountList;
    @FXML
    private Button createNewAccountButton, selectButton, logoutButton, transferButton;
    @FXML
    private Label welcomeLabel, accountTypeLabel, accountNameLabel, totalBalanceLabel, accountBalanceLabel, savingsGoalLabel, accountIDLabel, accountBankLabel, invalidLabel;
    @FXML
    private VBox accountDetailsBox;

    private ObservableList<PaymentAccount> observableAccount;

    public void displayName(String username){
        welcomeLabel.setText("Welcome, " + username);
    }

    public void displayTotalBalance(double totalBalance) {

    }

    public void logout(ActionEvent e) throws IOException{
        Util.showAlert("Logging out?", "Are you sure you want to log-out?", e);
    }

    public void createNewAccount(ActionEvent e) throws IOException{
        Util.showModal("AccountCreationView", "Register an Account", e);
    }

    public void select(ActionEvent e) throws IOException{
        if(accountList.getSelectionModel().getSelectedItem() == null){
            invalidLabel.setText("Please select an account!");
        }else{
            Util.closeAndShow("SystemView", "Crushers System", e);
        }
    }

    public void transferFunds(ActionEvent e) throws IOException {
        Util.showModal("AccountTransferView", "Transfer Funds", e);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        observableAccount = FXCollections.observableArrayList();
        String firstName = App.currentCustomer.getFirstName();
        String lastName = App.currentCustomer.getLastName();
        double totalBalance = getCustomerTotalBalance();
        totalBalanceLabel.setText("Total Balance: " + totalBalance);
        welcomeLabel.setText("Welcome, " + firstName + " " + lastName);

        System.out.println("Accounts: " + App.currentCustomer.getAccountList());

        observableAccount.addAll(App.currentCustomer.getAccountList());
        accountList.setItems(observableAccount);

        accountDetailsBox.setVisible(false);
        
        accountList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PaymentAccount>(){

            @Override
            public void changed(ObservableValue<? extends PaymentAccount> observable, PaymentAccount oldValue, PaymentAccount newValue) {               
                    PaymentAccount account = accountList.getSelectionModel().getSelectedItem();
                    try {
                        System.out.println(Json.stringify(account));
                    } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    accountDetailsBox.setVisible(true);
                    accountBankLabel.setText("Bank: " + account.getBank().toString());
                    accountTypeLabel.setText("Account type: " + account.getType());
                    accountNameLabel.setText("Account name: " + account.getName());
                    accountBalanceLabel.setText("Account balance: " + account.getBalance() + " SEK");
                    if(account.getType() == "Savings"){
                        savingsGoalLabel.setText("Savings goal: " + ((SavingsAccount)account).getSavingsGoal() + " SEK");
                        savingsGoalLabel.setVisible(true);
                    }else{
                        savingsGoalLabel.setText("");
                        savingsGoalLabel.setVisible(false);
                    }
                    accountIDLabel.setText("Account ID: " + account.getId());
            } 
        });

    }

    public double getCustomerTotalBalance(){
        if(App.currentCustomer.getAccountList().size() == 0){
            return 0;
        }else{
            ArrayList<PaymentAccount> currentCustomerAccounts = App.currentCustomer.getAccountList();
            for (PaymentAccount currentCustomerAccount : currentCustomerAccounts) {
                customerTotalBalance += currentCustomerAccount.getBalance();
            }
        return customerTotalBalance;
        }
    }

    public void addSavingsToList(SavingsAccount account){
        observableAccount.add(account);
    }
    public void addPaymentToList(PaymentAccount account){
        observableAccount.add(account);
    }
}
