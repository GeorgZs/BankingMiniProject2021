package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SystemController implements Initializable{

    AccountController accountController = new AccountController();
    @FXML
    private Label welcomeLabel, errorLabel;
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
    
    public void logout(ActionEvent e) throws IOException{
        Util.logOutAlert("Logging out?", "Are you sure you want to log-out?", e);
    }
    public void selectDifferentAccount(ActionEvent e) throws IOException{
        Util.closeAndShow("AccountView", "Select an Account", e);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName());
        //Contacts
        ObservableList<Contact> observableContact = FXCollections.observableArrayList();
        observableContact.addAll(App.currentCustomer.getContactList());
        contactList.setItems(observableContact);

    }
    public void getInterest(ActionEvent e) throws IOException, InterruptedException {
        ArrayList<Integer> years = new ArrayList<>();
        if (years.contains(LocalDateTime.now().getYear())){
            errorLabel.setText("Already received interest this year");
        } else {
            years.add(LocalDateTime.now().getYear());
            Http.post("transactions/interestRate/" + accountController.getAccount().getId(), Customer.class);
        }
    }

    public void addMoney(ActionEvent e){
        Util.getAccountWithID(App.currentAccountID).deposit(100);
    }
}