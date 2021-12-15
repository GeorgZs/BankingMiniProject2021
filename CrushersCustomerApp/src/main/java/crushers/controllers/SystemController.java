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

    /*
    CONTACTS
    */

    @FXML
    private TableView<Contact> contactTableView;
    @FXML
    private AnchorPane contactsAnchor;
    @FXML
    private Button createContact, deleteContact, makePayment;
    @FXML
    private TextField nameField, accountID;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label contactErrorLabel;

    private ObservableList<Contact> observableContact = FXCollections.observableArrayList();

    public void makeAPayment(ActionEvent e){
        Util.showModal("PaymentView", "Make a payment", e);
    }

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
            JsonNode contactNode = Json.getEmptyNode();
            PaymentAccount account = new PaymentAccount(Integer.parseInt(accountID.getText()), "payment");
            ((ObjectNode)contactNode).put("name", nameField.getText());
            ((ObjectNode)contactNode).putPOJO("account", account);
            ((ObjectNode)contactNode).put("description", descriptionArea.getText());
            Contact createdContact = Json.parse(Http.authPost("customers/contact", App.currentToken, contactNode), Contact.class);
            contactTableView.getItems().add(createdContact);
            }
        }

    /*
    PAYMENTS
    */

    @FXML
    private Button makeANewPayment, makeAPaymentRequest, reportTransaction, viewTransactionDetails;
    @FXML
    private ListView<Transaction> transactionHistory;
    @FXML
    private ListView<String> timeList;
    @FXML
    private ListView<String> labelList;
    
    public void logout(ActionEvent e) throws IOException{
        Util.logOutAlert("Logging out?", "Are you sure you want to log-out?", e);
    }

    public void selectDifferentAccount(ActionEvent e) throws IOException{
        Util.closeAndShow("AccountView", "Select an Account", e);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        welcomeLabel.setText("Welcome " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName());
        //Payments
        ObservableList<String> observableTimeList = FXCollections.observableArrayList();
        ObservableList<Transaction> observableTransactionList = FXCollections.observableArrayList();
        ObservableList<String> observableLabelList = FXCollections.observableArrayList();
        try {
            ArrayList<Transaction> transactions = Json.parseList(Http.authGet("transactions/accounts/" + App.currentAccountID, App.currentToken), Transaction.class);
            System.out.println(transactions);
            List<Transaction> transactionList = App.currentAccount.getTransactions();;
            if(transactionList != null) {
                observableTransactionList.addAll(transactionList);
                for (Transaction transaction : transactionList) {
                    observableTimeList.add(transaction.getDate());
                    if (!transaction.getLabel().isEmpty()) {
                        observableLabelList.add(transaction.getLabel());
                    } else {
                        observableLabelList.add("");
                    }
                }
            }
            timeList.setItems(observableTimeList);
            transactionHistory.setItems(observableTransactionList);
            labelList.setItems(observableLabelList);
        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
        }
        //Contacts
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {
            contacts = Json.parseList(Http.authGet("customers/@contacts", App.currentToken), Contact.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
        if(contacts != null){
            contactTableView.getItems().addAll(contacts);
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
    public void setLabel(ActionEvent e) {
        if(transactionHistory.getSelectionModel().getSelectedItem() == null){
            transactionError.setText("Please select a transaction");
        }else{
            App.currentTransaction = transactionHistory.getSelectionModel().getSelectedItem();;
            Util.closeAndShow("SetLabelView", "Set Label",e);
        }
    }

    public void addMoney(ActionEvent e){
        Util.getAccountWithID(App.currentAccountID).deposit(100);
    }
}