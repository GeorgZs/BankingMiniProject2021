package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import crushers.App;
import crushers.model.*;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;
import static crushers.util.Util.trunc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SystemController implements Initializable{

    @FXML
    private Label welcomeLabel, errorLabel, transactionError, loanErrorLabel, notificationErrorLabel, overviewErrorLabel;
    @FXML
    private Pane pane;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView<Contact> contactTableView;
    @FXML
    private TableView<Transaction> transactionTableView;
    @FXML
    private AnchorPane contactsAnchor;
    @FXML
    private Button createContact, deleteContact, makePayment, makeANewPayment, makeAPaymentRequest, reportTransaction, viewTransactionDetails, apply, paybackLoan;
    @FXML
    private TextField nameField, accountID, loanAmountField, amountPayback, recipientIdField;
    @FXML
    private TextArea descriptionArea, loanPurposeField, transactionLabelField, requestBodyField;
    @FXML
    private Label contactErrorLabel, accountIdLabel, accountNumberLabel, accountNameLabel, accountBalanceLabel, accountOwnerLabel, accountTypeLabel, accountBankLabel, transactionsMadeLabel, numberOfContactsLabel, pendingLoansLabel, totalDebtLabel, netWorthLabel;
    @FXML
    private TableView<Loan> loanTableView;
    @FXML
    private ListView<String> notificationsListView;

    private ObservableList<String> observableNotifications;

    boolean showCardNumber;

    /*
    CONTACTS
    */
//Allows the user to create a contact by using their account ID.
    public void createContact(ActionEvent e) throws IOException, InterruptedException {

        if (nameField.getText().isBlank()) {
            contactErrorLabel.setText("Please enter a contact name!");
        } else if (nameField.getText().matches("^[0-9]+$")) {
            contactErrorLabel.setText("Please enter alphabetical characters only!");
        } else if (descriptionArea.getText().isBlank()){
            contactErrorLabel.setText("Please enter a contact description!");
        }else {
            try{
                Integer.parseInt(accountID.getText());
            }catch(NumberFormatException nfe){
                contactErrorLabel.setText("Please enter a valid account ID");
                return;
            }
            // Adding contact to table and sending request to API
            PaymentAccount account = new PaymentAccount(Integer.parseInt(accountID.getText()), "payment");
            JsonNode accountNode = Json.nodeWithFields("id", accountID.getText(), "type", "payment");
            JsonNode contactNode = Json.nodeWithFields("name", nameField.getText(), "account", accountNode, "description", descriptionArea.getText());
            Contact createdContact = Json.parse(Http.authPost("customers/contact", App.currentToken, contactNode), Contact.class);
            System.out.println("Created: " + createdContact);
            contactTableView.getItems().add(createdContact);
            Util.updateCustomer();
            updateAccountOverview();
            }
        }

    /*
    PAYMENTS
    */
//Make a payment. Opens the payment controller. Allows you to make payments to created contacts.
    public void makeAPayment(ActionEvent e){
        Util.showModal("PaymentView", "Make a Payment", e);
    }
//Apply your own label to a transaction.
    public void setLabel(ActionEvent e) throws Exception{
        transactionError.setTextFill(Color.RED);
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
        if(selectedTransaction == null){
            transactionError.setText("Please select a transaction!");
        }else if(transactionLabelField.getText().isBlank()){
            transactionError.setText("Please enter a valid label!");
        }else{
            JsonNode transactionNode = Json.nodeWithFields("label", transactionLabelField.getText(), "transaction", Json.nodeWithFields("id", selectedTransaction.getId()));
            Http.authPut("transactions/label", transactionNode, Transaction.class, App.currentToken);
            transactionError.setTextFill(Color.GREEN);
            transactionError.setText("Transaction Successfully Labeled!");
        }
        updateAccountOverview();
    }
//View details of a specific transaction.
    public void viewTransactionDetails(ActionEvent e){
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
        if(selectedTransaction == null){
            transactionError.setTextFill(Color.RED);
            transactionError.setText("Please select a transaction!");
        }else{
            FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/TransactionDescriptionView.fxml"));
            Stage stage = new Stage();
            try {
                Parent root = (Parent) loader.load();
                TransactionDescriptionController tdc = loader.getController();
                tdc.setCurrentTransaction(selectedTransaction);
                tdc.init();
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
                stage.setTitle("Transaction Overview");
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
//Adds transaction to the table view.
    public void addTransactionToTable(Transaction transaction){
        if(transaction != null){
            transactionTableView.getItems().add(transaction);
            Util.updateCustomer();
            updateAccountOverview();
        }
    }
//Create a recurring transaction. Opens Recurring transaction controller.
    public void createRecurringTransaction(ActionEvent e){
        Util.showModal("RecurringView", "Create Recurring Transaction", e);
    }

    /*
    LOANS
    */
// Apply for a loan. Adds loan to the loan table after creation.
    public void apply(ActionEvent e) throws IOException, InterruptedException {
        if (loanAmountField.getText().isBlank()){
            loanErrorLabel.setText("Must enter an amount.");
        } else if (!loanAmountField.getText().matches("^[0-9]+$")){
            loanErrorLabel.setText("Please enter a numerical amount, excluding special characters.");
        } else if (loanPurposeField.getText().isBlank()){
            loanErrorLabel.setText("Must include justification for applying for a loan.");
        } else if (Double.parseDouble(loanAmountField.getText()) > 25000) {
            loanErrorLabel.setText("Maximum loan amount is 25,000 SEK.");
        } else if (Double.parseDouble(loanAmountField.getText()) < 1000){
            loanErrorLabel.setText("Minimum loan amount is 1,000 SEK.");
        } else {
            double loanAmount = Double.parseDouble(loanAmountField.getText());
            String loanReason = loanPurposeField.getText();
            JsonNode account = Json.nodeWithFields("id",App.currentAccountID,"type","payment");
            JsonNode loanNode = Json.nodeWithFields("amount",loanAmount,"purpose",loanReason,"account",account);
            Loan loan = Json.parse(Json.stringify(loanNode), Loan.class);
            Http.authPost("transactions/loan", App.currentToken, loanNode);
            loanTableView.getItems().add(loan);
            updateAccountOverview();
            updateNotifications();
        }
    }
// Payback a loan by selecting it in the table
    public void paybackLoan(ActionEvent e) throws Exception {
    if (amountPayback.getText().isBlank()){
        loanErrorLabel.setText("Must enter an amount to payback.");
    } else if (!amountPayback.getText().matches("^[0-9]+$")) {
        loanErrorLabel.setText("Must enter a numerical amount.");
    } else if (Double.parseDouble(amountPayback.getText()) > App.currentAccount.getBalance()) {
        loanErrorLabel.setText("Payback amount cannot exceed account balance.");
    } else if (loanTableView.getSelectionModel().getSelectedItem() == null) {
        loanErrorLabel.setText("Must select a loan to payback.");
    } else if (loanTableView.getSelectionModel().getSelectedItem().getAmount() < Double.parseDouble(amountPayback.getText())){
        loanErrorLabel.setText("Cannot pay more than the remaining loan amount.");
    } else {
        double amountToPay = Double.parseDouble(amountPayback.getText());
        JsonNode account = Json.nodeWithFields("id",App.currentAccountID,"type","payment");
        Loan loan = loanTableView.getSelectionModel().getSelectedItem();
<<<<<<< HEAD
        JsonNode transaction = Json.nodeWithFields("label",loan.getPurpose(),"id",loan.getAccountId(),"from",account,"to",null,"amount",amountToPay,"description",null,"date",null);
        Transaction transactionObj = Http.authPut("transactions/loan",transaction,Transaction.class,App.currentToken);
        System.out.println(transactionObj);
        System.out.println("Test\nTest");
=======
        JsonNode transaction = Json.nodeWithFields("label",loan.getPurpose(),"id",loan.getAccountId(),"from",account,"to",null,"amount",amountToPay,"description",null,"date",LocalDateTime.now());
        Http.authPut("transactions/loan",transaction,Transaction.class,App.currentToken);
        System.out.println(transaction.toString());
        Util.updateCustomer();
>>>>>>> db4d6bb7683ea329dc10942b2694b0b3eaed5555
        }
    }

    /*
    CONTROLLER INIT
    */

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Util.updateCustomer();
        System.out.println(App.currentCustomer);
        welcomeLabel.setText("Welcome " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName() + " (ID" + App.currentCustomer.getId() + ")");
        updateNotifications();

        /*
        TRANSACTIONS (TABLE INIT)
        */

        TableColumn<Transaction, Integer> transactionIdColumn = new TableColumn<>("ID");
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transactionTableView.getColumns().add(transactionIdColumn);

        TableColumn<Transaction, Double> transactionAmountColumn = new TableColumn<>("Amount");
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        transactionTableView.getColumns().add(transactionAmountColumn);

        TableColumn<Transaction, String> transactionFromColumn = new TableColumn<>("From");
        transactionFromColumn.setCellValueFactory(new PropertyValueFactory<>("fromString"));
        transactionTableView.getColumns().add(transactionFromColumn);

        TableColumn<Transaction, String> transactionToColumn = new TableColumn<>("To");
        transactionToColumn.setCellValueFactory(new PropertyValueFactory<>("toString"));
        transactionTableView.getColumns().add(transactionToColumn);
        
        TableColumn<Transaction, String> transactionDateColumn = new TableColumn<>("Date");
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionTableView.getColumns().add(transactionDateColumn);

        TableColumn<Transaction, String> transactionDescriptionColumn = new TableColumn<>("Description");
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionTableView.getColumns().add(transactionDescriptionColumn);
        
        ArrayList<Transaction> transactions = App.currentCustomer.getAccountWithId(App.currentAccountID).getTransactions();
        if(transactions != null){
            transactionTableView.getItems().addAll(transactions);
        }

        /*
        CONTACTS (TABLE INIT)
        */

        TableColumn<Contact, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactTableView.getColumns().add(idColumn);

        TableColumn<Contact, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactTableView.getColumns().add(nameColumn);

        TableColumn<Contact, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactTableView.getColumns().add(descriptionColumn);

        TableColumn<Contact, Integer> accountIdColumn = new TableColumn<>("Account ID");
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        contactTableView.getColumns().add(accountIdColumn);
        
        if(App.currentCustomer.getContactList() != null){
            contactTableView.getItems().addAll(App.currentCustomer.getContactList());
        }

        /*
        LOANS (TABLE INIT)
        */

        TableColumn<Loan, Integer> loanAccountIdColumn = new TableColumn<>("Account ID");
        loanAccountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        loanTableView.getColumns().add(loanAccountIdColumn);

        TableColumn<Loan, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        loanTableView.getColumns().add(amountColumn);

        TableColumn<Loan, String> dateColumn = new TableColumn<>("Purpose");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        loanTableView.getColumns().add(dateColumn);

        ArrayList<Loan> loans = App.currentCustomer.getLoansWithAccountId(App.currentAccountID);
        if(loans != null){
            loanTableView.getItems().addAll(loans);
        }
        updateAccountOverview();
    }


    /*
    INTEREST
    */

    public void getInterest(ActionEvent e) throws IOException, InterruptedException {
        overviewErrorLabel.setTextFill(Color.RED);
        String res = Http.authPost("transactions/interestRate/" + App.currentAccountID, App.currentToken, null);
        if(res.contains("balance too low")){
            overviewErrorLabel.setText("Account balance too low!");
        }else if(res.contains("Not a savings account")){
            overviewErrorLabel.setText("Cannot receive interest on Payment account!");
        }else{
            overviewErrorLabel.setTextFill(Color.GREEN);
            overviewErrorLabel.setText("Interest successfully received!");
        }
        updateAccountOverview();
    }

    /*
    MISCELLANEOUS
    */

    public void sendNotification(ActionEvent e) throws IOException, InterruptedException{
        notificationErrorLabel.setTextFill(Color.RED);
        try{
            int recipientId = Integer.parseInt(recipientIdField.getText());
            String requestBody = requestBodyField.getText();
            if(requestBody.isBlank()){
                notificationErrorLabel.setText("Please fill a request");
                return;
            }
            JsonNode notificationNode = Json.nodeWithFields("notification", requestBody, "targetCustomer", Json.nodeWithFields("id", recipientId));
            String res = Http.authPost("customers/notification", App.currentToken, notificationNode);
            if(res.contains("Internal server error")){
                notificationErrorLabel.setText("Account ID does not exist!");
                return;
            }
            notificationErrorLabel.setTextFill(Color.GREEN);
            notificationErrorLabel.setText("Request successfully sent!");
        }catch(NumberFormatException nfe){
            notificationErrorLabel.setText("Please enter a valid ID");
        }catch(Exception ex){
            notificationErrorLabel.setText("Account ID does not exist!");
        }
        
    }
//Logs the user out.
    public void logout(ActionEvent e) throws IOException{
        App.currentAccountID = 0;
        Util.logOutAlert("Logging out?", "Are you sure you want to log-out?", e);
    }
//Returns the user to the account overview so they can create a new account of select a pre-existing account.
    public void selectDifferentAccount(ActionEvent e) throws IOException{
        Util.closeAndShow("AccountView", "Select an Account", e);
    }
//Toggles the visibility of the user's card number in the system view.
    public void toggleDisplayCardNumber(ActionEvent e){
        showCardNumber = ! showCardNumber;
        updateAccountOverview();
    }
//Updates the account overview.
    public void updateAccountOverview(){
        Util.updateCustomer();
        String accountType = App.currentAccount.getInterestRate() == 0.0 ? "Payment" : "Savings";
        String cardNumber = showCardNumber ? App.currentAccount.getNumber() : "SE ** **** *********";

        accountIdLabel.setText("Account ID: " + App.currentAccount.getId());
        accountNumberLabel.setText("Account Number: " + cardNumber);
        accountNameLabel.setText("Account Name: " + App.currentAccount.getName());
        accountBalanceLabel.setText("Account Balance: " + trunc(App.currentAccount.getBalance()) + " SEK");
        accountOwnerLabel.setText("Account Owner: " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName());
        accountTypeLabel.setText("Account Type: " + accountType);
        accountBankLabel.setText("Account Bank: " + App.currentAccount.getBank());

        double debt = 0;
        for(Loan loan: App.currentCustomer.getLoansWithAccountId(App.currentAccountID)){
            debt += loan.getAmount();
        }
        double netWorth = App.currentCustomer.getAccountWithId(App.currentAccountID).getBalance() - debt;

        transactionsMadeLabel.setText("Transactions Made: " + App.currentCustomer.getAccountWithId(App.currentAccountID).getTransactions().size() + " transactions");
        numberOfContactsLabel.setText("Number of Contacts: " + App.currentCustomer.getContactList().size() + " contacts");
        pendingLoansLabel.setText("Pending Loans: " + App.currentCustomer.getLoansWithAccountId(App.currentAccountID).size() + " loans");
        totalDebtLabel.setText("Total Debt: " + trunc(debt) + " SEK");
        netWorthLabel.setText(trunc(netWorth) + " SEK");
        if(netWorth > 0){
            netWorthLabel.setTextFill(Color.GREEN);
        }else if(netWorth < 0){
            netWorthLabel.setTextFill(Color.RED);
        }
        transactionTableView.getItems().setAll(App.currentCustomer.getAccountWithId(App.currentAccountID).getTransactions());
    }
//Updates notifications view.
    public void updateNotifications(){
        try {
            LinkedHashMap<String, String> notifications = Json.parseLinkedHashMap(Http.authGet("customers/notifications", App.currentToken), String.class, String.class);
            if(notifications == null){
                notifications = new LinkedHashMap<String, String>();
            }
            ArrayList<String> notificationList = new ArrayList<String>();
            for(String key: notifications.keySet()){
                notificationList.add("(" + key + "): " + notifications.get(key));
            }
            observableNotifications = FXCollections.observableArrayList();
            observableNotifications.addAll(notificationList);
            notificationsListView.setItems(observableNotifications);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}