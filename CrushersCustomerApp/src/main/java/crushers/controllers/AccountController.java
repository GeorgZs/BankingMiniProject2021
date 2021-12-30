package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import crushers.App;
import crushers.model.PaymentAccount;
import crushers.model.SavingsAccount;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;
import static crushers.util.Util.trunc;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AccountController implements Initializable{
    
    double customerTotalBalance;
    @FXML
    static AnchorPane accountAnchor;
    @FXML
    private ListView<PaymentAccount> accountList;
    @FXML
    private Button createNewAccountButton, selectButton, logoutButton, transferButton;
    @FXML
    private Label welcomeLabel, accountTypeLabel, accountNameLabel, totalBalanceLabel, accountBalanceLabel, accountIDLabel, accountBankLabel, invalidLabel;
    @FXML
    private VBox accountDetailsBox;

    private ObservableList<PaymentAccount> observableAccount;

    public static SystemController sysCtrl;

    public void displayName(String username){
        welcomeLabel.setText("Welcome, " + username);
    }

    public void displayTotalBalance(double totalBalance) {

    }

    public void logout(ActionEvent e) throws IOException{
        Util.logOutAlert("Logging out?", "Are you sure you want to log-out?", e);
    }

    public void createNewAccount(ActionEvent e) throws IOException{
        Util.showModal("AccountCreationView", "Register an Account", e);
    }

    public void select(ActionEvent e) throws IOException{
        if(accountList.getSelectionModel().getSelectedItem() == null){
            invalidLabel.setText("Please select an account!");
        }else{
            App.currentAccount = accountList.getSelectionModel().getSelectedItem();
            App.currentAccountID = App.currentAccount.getId();
            System.out.println(App.currentAccountID);
            // Util.closeAndShow("SystemView", "Crushers System", e);

            Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
            oldStage.close();
            FXMLLoader loader = new FXMLLoader(App.class.getClassLoader().getResource("crushers/views/SystemView.fxml"));
            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle("Crushers System");
            stage.show();

            sysCtrl = loader.getController();
        }
    }

    public void transferFunds(ActionEvent e) throws IOException {
        Util.showModal("AccountTransferView", "Transfer Funds", e);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        String firstName = App.currentCustomer.getFirstName();
        String lastName = App.currentCustomer.getLastName();
        totalBalanceLabel.setText("Total Balance: " + getCustomerTotalBalance());
        welcomeLabel.setText("Welcome, " + firstName + " " + lastName);

        try {
            updateAccountList();
        } catch (MismatchedInputException e) {
            System.out.println("Empty customer");
            e.printStackTrace();
        }

        accountDetailsBox.setVisible(false);
        
        accountList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PaymentAccount>(){

            @Override
            public void changed(ObservableValue<? extends PaymentAccount> observable, PaymentAccount oldValue, PaymentAccount newValue) {               
                    PaymentAccount account = accountList.getSelectionModel().getSelectedItem();
                    if(account == null){
                        return;
                    }
                    if(account.getInterestRate() == 0.0){
                        accountTypeLabel.setText("Account type: Payment");
                    }else{
                        accountTypeLabel.setText("Account type: Savings");
                    }
                    accountDetailsBox.setVisible(true);
                    accountBankLabel.setText("Bank: " + account.getBank().getName());
                    
                    accountNameLabel.setText("Account name: " + account.getName());
                    accountBalanceLabel.setText("Account balance: " + trunc(account.getBalance()) + " SEK");
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
        return trunc(customerTotalBalance);
        }
    }
    public void updateAccountList() throws MismatchedInputException{
        try {
            String res = Http.authGet("accounts/@me", App.currentToken);
            if(res.contains("Internal server error")){
                App.currentCustomer.setAccountList(new ArrayList<PaymentAccount>());
            }else{
                App.currentCustomer.setAccountList(Json.parseList(res, PaymentAccount.class));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        observableAccount = FXCollections.observableArrayList();
        observableAccount.addAll(App.currentCustomer.getAccountList());
        accountList.setItems(observableAccount);
    }

    // public void addSavingsToList(SavingsAccount account){
    //     observableAccount.add(account);
    //     accountList.refresh();
    // }
    // public void addPaymentToList(PaymentAccount account){
    //     observableAccount.add(account);
    //     accountList.refresh();
    // }
}
