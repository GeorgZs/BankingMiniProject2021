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
    private TextField nameField, accountID, loanAmountField, loanNotes, amountPayback;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label contactErrorLabel;
    @FXML
    private TableView<Loan> loanTable;

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
            JsonNode contactNode = Json.nodeWithFields("name", nameField.getText(), "account", account, "description", descriptionArea.getText());
            Contact createdContact = Json.parse(Http.authPost("customers/contact", App.currentToken, contactNode), Contact.class);
            contactTableView.getItems().add(createdContact);
            Util.updateCustomer();
            }
        }

    /*
    PAYMENTS
    */

    public void makeAPayment(ActionEvent e){
        Util.showModal("PaymentView", "Make a payment", e);
    }

    public void addTransactionToTable(Transaction transaction){
        if(transaction != null){
            transactionTableView.getItems().add(transaction);
        }
    }

    /*
    LOANS
    */

    public void apply(ActionEvent e) {
    if (loanAmountField.getText().isBlank()){
        errorLabel.setText("Must enter an amount.");
    } else if (!loanAmountField.getText().matches("^[0-9]+$")){
        errorLabel.setText("Please enter a numerical amount, excluding special characters.");
    } else if (loanNotes.getText().isBlank()){
        errorLabel.setText("Must include justification for applying for a loan.");
    } else if (Double.parseDouble(loanAmountField.getText()) > 25000) {
        errorLabel.setText("Maximum loan amount is 25,000 SEK.");
    } else if (Double.parseDouble(loanAmountField.getText()) < 1000){
        errorLabel.setText("Minimum loan amount is 1,000 SEK.");
    } else {
       double loanAmount = Double.parseDouble(loanAmountField.getText());
       String loanReason = loanNotes.getText();
       PaymentAccount account = App.currentAccount;
       Loan loan = new Loan(loanAmount, loanReason, account);
        }
    }

    public void paybackLoan(ActionEvent e) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Util.updateCustomer();
        welcomeLabel.setText("Welcome " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName());
        
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
}