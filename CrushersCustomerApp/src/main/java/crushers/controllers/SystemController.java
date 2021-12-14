package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import crushers.App;
import crushers.model.*;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SystemController implements Initializable{

    @FXML
    private Label welcomeLabel, errorLabel, transactionError;
    @FXML
    private Pane pane;
    @FXML
    private TabPane tabPane;

    // Contacts
    @FXML
    private AnchorPane contactsAnchor;
    @FXML
    private Button createContact, deleteContact, makePayment;
    @FXML
    private ListView<Contact> contactList;
    @FXML
    private TextField nameField, contactID, accountID;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label contactErrorLabel;

    public void makeAPayment(ActionEvent e){
        Util.showModal("PaymentView", "Make a payment", e);
    }
    public void createContact(ActionEvent e) throws IOException, InterruptedException {
    if (nameField.getText() == null) {
        contactErrorLabel.setText("Please enter a contact name!");
        } else if (nameField.getText().matches("^[0-9]+$")) {
        contactErrorLabel.setText("Please enter alphabetical characters only!");
        } else if (accountID.getText() == null) {
        contactErrorLabel.setText("Please enter an account ID!");
        } else if (contactID.getText() == null) {
        contactErrorLabel.setText("Please enter a 4-digit contact ID of your choosing!");
        } else if (!contactID.getText().matches("^[0-9]+$")) {
        contactErrorLabel.setText("Contact ID must be comprised of 4-digits!");
        } else if (contactID.getText().length() < 4 || contactID.getText().length() > 4) {
        contactErrorLabel.setText("Contact ID must be 4-digits in length!");
    } else {
        String name = nameField.getText();
        String accID = accountID.getText();
        int ID = Integer.parseInt(contactID.getText());
        String account = Http.get("account/"+accID);
        PaymentAccount account1 = Json.parse(account, PaymentAccount.class);
        String description = descriptionArea.getText();
        Contact contact = new Contact(ID, name, account1, description);
        }
    }

    public void deleteContact(ActionEvent e) throws IOException, InterruptedException {

    }

    // Payments
    @FXML
    private Button makeANewPayment, makeAPaymentRequest, reportTransaction, viewTransactionDetails;
    @FXML
    private ListView<Transaction> transactionHistory;
    @FXML
    private ListView<String> timeList;
    
    public void logout(ActionEvent e) throws IOException{
        Util.logOutAlert("Logging out?", "Are you sure you want to log-out?", e);
    }
    public void selectDifferentAccount(ActionEvent e) throws IOException{
        Util.closeAndShow("AccountView", "Select an Account", e);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName());
        //Payments
        ObservableList<String> observableTimeList = FXCollections.observableArrayList();
        ObservableList<Transaction> observableTransactionList = FXCollections.observableArrayList();
        try {
            ArrayList<Transaction> transactions = Json.parseList(Http.authGet("transactions/accounts/" + App.currentAccountID, App.currentToken), Transaction.class);
            System.out.println(transactions);
        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            List<Transaction> transactionList = App.currentAccount.getTransactions();
            System.out.println(transactionList);
            if(App.currentAccount.getTransactions() != null) {
                observableTransactionList.addAll(App.currentAccount.getTransactions());
                for(Transaction transaction : App.currentAccount.getTransactions()) {
                    observableTimeList.add(transaction.getDate());
                }
                timeList.setItems(observableTimeList);
                transactionHistory.setItems(observableTransactionList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Contacts
        ObservableList<Contact> observableContact = FXCollections.observableArrayList();
        if(App.currentCustomer.getContactList() != null) {
            observableContact.addAll(App.currentCustomer.getContactList());
            contactList.setItems(observableContact);
        }
    }
    public void getInterest(ActionEvent e) throws IOException, InterruptedException {
        ArrayList<Integer> years = new ArrayList<>();
        if (years.contains(LocalDateTime.now().getYear())){
            System.out.println("no");
            errorLabel.setText("Already received interest this year");
        } else {
            years.add(LocalDateTime.now().getYear());
            Http.post("transactions/interestRate/" + App.currentAccount.getId(), Customer.class);
            System.out.println("done");
        }
    }
    public void transactionDetails(ActionEvent e) {
        if(transactionHistory.getSelectionModel().getSelectedItem() == null){
            transactionError.setText("Please select a transaction");
        }else{
            App.currentTransaction = transactionHistory.getSelectionModel().getSelectedItem();;
            Util.showModal("TransactionDescriptionView", "Transaction Description",e);
        }
    }

    public void addMoney(ActionEvent e){
        Util.getAccountWithID(App.currentAccountID).deposit(100);
    }
}