package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import crushers.App;
import crushers.model.*;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SystemController implements Initializable{

    @FXML
    private Label welcomeLabel, errorLabel, transactionError;
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
    private TextField nameField, accountID, loanAmountField, amountPayback;
    @FXML
    private TextArea descriptionArea, loanPurposeField;
    @FXML
    private Label contactErrorLabel, accountIdLabel, accountNumberLabel, accountNameLabel, accountBalanceLabel, accountOwnerLabel, accountTypeLabel, accountBankLabel, transactionsMadeLabel, numberOfContactsLabel, pendingLoansLabel, totalDebtLabel, netWorthLabel;
    @FXML
    private TableView<Loan> loanTableView;

    boolean showCardNumber;

    /*
    CONTACTS
    */

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

    public void makeAPayment(ActionEvent e){
        Util.showModal("PaymentView", "Make a Payment", e);
    }

    public void makeAPaymentRequest(ActionEvent e){
        Util.showModal("RequestView", "Make a Payment Request", e);
    }

    public void addTransactionToTable(Transaction transaction){
        if(transaction != null){
            transactionTableView.getItems().add(transaction);
            Util.updateCustomer();
            updateAccountOverview();
        }
    }

    /*
    LOANS
    */

    public void apply(ActionEvent e) throws IOException, InterruptedException {
    if (loanAmountField.getText().isBlank()){
        errorLabel.setText("Must enter an amount.");
    } else if (!loanAmountField.getText().matches("^[0-9]+$")){
        errorLabel.setText("Please enter a numerical amount, excluding special characters.");
    } else if (loanPurposeField.getText().isBlank()){
        errorLabel.setText("Must include justification for applying for a loan.");
    } else if (Double.parseDouble(loanAmountField.getText()) > 25000) {
        errorLabel.setText("Maximum loan amount is 25,000 SEK.");
    } else if (Double.parseDouble(loanAmountField.getText()) < 1000){
        errorLabel.setText("Minimum loan amount is 1,000 SEK.");
    } else {

       double loanAmount = Double.parseDouble(loanAmountField.getText());
       String loanReason = loanPurposeField.getText();
       PaymentAccount account = App.currentAccount;
       Loan loan = new Loan(loanAmount, loanReason, account);
       Http.authPost("transactions/loan", App.currentToken, loan);
       Util.updateCustomer();
        }
    }

    public void paybackLoan(ActionEvent e) {
    if (amountPayback.getText().isBlank()){
        errorLabel.setText("Must enter an amount to payback.");
    } else if (!amountPayback.getText().matches("^[0-9]+$")) {
        errorLabel.setText("Must enter a numerical amount.");
    } else if (Double.parseDouble(amountPayback.getText()) > App.currentAccount.getBalance()) {
        errorLabel.setText("Payback amount cannot exceed account balance!");
    } else {
        double amountToPay = Double.parseDouble(amountPayback.getText());

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Util.updateCustomer();
        welcomeLabel.setText("Welcome " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName());
        
        // Account Overview

        updateAccountOverview();

        // Payments

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

        // Contacts

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

        // Loans
        TableColumn<Loan, Integer> loanAccountIdColumn = new TableColumn<>("Account ID");
        loanAccountIdColumn.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        loanTableView.getColumns().add(loanAccountIdColumn);

        TableColumn<Loan, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        loanTableView.getColumns().add(amountColumn);

        TableColumn<Loan, String> purposeColumn = new TableColumn<>("Purpose");
        purposeColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        loanTableView.getColumns().add(purposeColumn);

        ArrayList<Loan> loans = App.currentCustomer.getLoans();
        if(loans != null){
            loanTableView.getItems().addAll(loans);
            // loanTableView.getItems().addAll(loans);
        }
    }
    
    /*
    INTEREST
    */

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

    public void logout(ActionEvent e) throws IOException{
        Util.logOutAlert("Logging out?", "Are you sure you want to log-out?", e);
    }

    public void selectDifferentAccount(ActionEvent e) throws IOException{
        Util.closeAndShow("AccountView", "Select an Account", e);
    }

    public void setLabel(ActionEvent e){

    }
    public void viewTransactionDetails(ActionEvent e){

    }

    public void toggleDisplayCardNumber(ActionEvent e){
        showCardNumber = ! showCardNumber;
        updateAccountOverview();
    }

    public void updateAccountOverview(){
        String accountType = App.currentAccount.getInterestRate() == 0.0 ? "Payment" : "Savings";
        String cardNumber = showCardNumber ? App.currentAccount.getNumber() : "SE ** **** *********";

        accountIdLabel.setText("Account ID: " + App.currentAccount.getId());
        accountNumberLabel.setText("Account Number: " + cardNumber);
        accountNameLabel.setText("Account Name: " + App.currentAccount.getName());
        accountBalanceLabel.setText("Account Balance: " + App.currentAccount.getBalance() + " SEK");
        accountOwnerLabel.setText("Account Owner: " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName());
        accountTypeLabel.setText("Account Type: " + accountType);
        accountBankLabel.setText("Account Bank: " + App.currentAccount.getBank());

        transactionsMadeLabel.setText("Transactions Made: " + App.currentCustomer.getAccountWithId(App.currentAccountID).getTransactions().size());
        numberOfContactsLabel.setText("Number of Contacts:" + App.currentCustomer.getContactList().size());
        pendingLoansLabel.setText("Pending Loans: " + 0);
        totalDebtLabel.setText("Total Debt: " + 0);
        netWorthLabel.setText("Net Worth: " + 0);
    }
}