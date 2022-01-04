package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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

import crushers.App;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import crushers.util.Util;

import static crushers.util.Util.trunc;

public class AccountController implements Initializable{
    
    double customerTotalBalance;
    @FXML
    static AnchorPane accountAnchor;
    @FXML
    private ListView<BankAccount> accountList;
    @FXML
    private Button createNewAccountButton, selectButton, logoutButton, transferButton;
    @FXML
    private Label welcomeLabel, accountTypeLabel, accountNameLabel, totalBalanceLabel, accountBalanceLabel, accountIDLabel, accountBankLabel, invalidLabel;
    @FXML
    private VBox accountDetailsBox;

    private ObservableList<BankAccount> observableAccount;

    public static SystemController sysCtrl;

    public void displayName(String username){
        welcomeLabel.setText("Welcome, " + username);
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
        } 
        catch (HttpException ex) {
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        accountDetailsBox.setVisible(false);
        
        accountList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BankAccount>(){

            @Override
            public void changed(ObservableValue<? extends BankAccount> observable, BankAccount oldValue, BankAccount newValue) {               
                    BankAccount account = accountList.getSelectionModel().getSelectedItem();
                    if(account == null){
                        return;
                    }

                    accountTypeLabel.setText("Account type: " + account.getType());
                    accountDetailsBox.setVisible(true);
                    accountBankLabel.setText("Bank: " + account.getBank().getName());
                    
                    accountNameLabel.setText("Account name: " + account.getName());
                    accountBalanceLabel.setText("Account balance: " + trunc(account.getBalance()) + " SEK");
                    accountIDLabel.setText("Account ID: " + account.getId());
            } 
        });

    }

    public double getCustomerTotalBalance(){
        if(App.currentCustomerAccounts.size() == 0){
            return 0;
        }
        else {
            for (BankAccount account : App.currentCustomerAccounts) {
                customerTotalBalance += account.getBalance();
            }

            return trunc(customerTotalBalance);
        }
    }
    public void updateAccountList() throws Exception {
        App.currentCustomerAccounts = new ArrayList<BankAccount>();
        App.currentCustomerAccounts = ServerFacade.instance.listAllBankAccountsOfThisCustomer();

        observableAccount = FXCollections.observableArrayList();
        observableAccount.addAll(App.currentCustomerAccounts);
        accountList.setItems(observableAccount);
    }
}
