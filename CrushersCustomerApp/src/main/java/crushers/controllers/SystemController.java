package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import crushers.App;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
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
    private Label welcomeLabel, errorLabel, transactionError, loanErrorLabel, notificationErrorLabel, overviewErrorLabel, noNotificationsLabel;
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
    private TextArea descriptionArea, loanPurposeField, transactionLabelField;
    @FXML
    private Label contactErrorLabel, accountIdLabel, accountNumberLabel, accountNameLabel, accountBalanceLabel, accountOwnerLabel, accountTypeLabel, accountBankLabel, transactionsMadeLabel, numberOfContactsLabel, pendingLoansLabel, totalDebtLabel, netWorthLabel;
    @FXML
    private TableView<BankAccount> loanTableView;
    @FXML
    private ListView<Notification> notificationsListView;

    private ObservableList<Notification> observableNotifications;

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
        } else {
            try{
                Integer.parseInt(accountID.getText());
            }catch(NumberFormatException nfe){
                contactErrorLabel.setText("Please enter a valid account ID");
                return;
            }
            // Adding contact to table and sending request to API
            BankAccount account = new BankAccount();
            account.setId(Integer.parseInt(accountID.getText()));

            Contact contact = new Contact();
            contact.setAccount(account);
            contact.setName(nameField.getText());
            contact.setDescription(descriptionArea.getText());

            try {
                Contact createdContact = ServerFacade.instance.createContact(contact);
                contactTableView.getItems().add(createdContact);
                Util.updateCustomer();
                updateAccountOverview();
            }
            catch (HttpException ex) {
                contactErrorLabel.setText(ex.getError());
                System.out.println(ex.getError());
            }
            catch (Exception ex) {
                contactErrorLabel.setText("Oops, something went wrong! Could not create the contact!");
                ex.printStackTrace();
            }
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
            try {
                TransactionLabels labels = new TransactionLabels();
                labels.setLabels(transactionLabelField.getText().split(", "));

                ServerFacade.instance.setTransactionLabels(selectedTransaction.getId(), labels);
                transactionError.setTextFill(Color.GREEN);
                transactionError.setText("Transaction Successfully Labeled!");
            }
            catch (HttpException ex) {
                transactionError.setText(ex.getError());
                System.out.println(ex.getError());
            }
            catch (Exception ex) {
                transactionError.setText("Oops, something went wrong! Could not apply the label!");
                ex.printStackTrace();
            }
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
            BankAccount loan = new BankAccount();
            loan.setType("loan");
            loan.setBalance(-Double.parseDouble(loanAmountField.getText()));
            loan.setName(loanPurposeField.getText());
            loan.setBank(App.currentAccount.getBank());

            try {
                loan = ServerFacade.instance.createBankAccount(loan);
                loanTableView.getItems().add(loan);
                updateAccountOverview();
                updateNotifications();
            }
            catch (HttpException ex) {
                loanErrorLabel.setText(ex.getError());
                System.out.println(ex.getError());
            }
            catch (Exception ex) {
                loanErrorLabel.setText("Oops, something went wrong! Could not request the loan!");
                ex.printStackTrace();
            }
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
        } else if (loanTableView.getSelectionModel().getSelectedItem().getBalance() + Double.parseDouble(amountPayback.getText()) > 0){
            loanErrorLabel.setText("Cannot pay more than the remaining loan amount.");
        } else {
            Transaction transaction = new Transaction();
            transaction.setFrom(App.currentAccount);
            transaction.setAmount(Double.parseDouble(amountPayback.getText()));
            transaction.setTo(loanTableView.getSelectionModel().getSelectedItem());

            try {
                transaction = ServerFacade.instance.createTransaction(transaction);
            }
            catch (HttpException ex) {
                loanErrorLabel.setText(ex.getError());
                System.out.println(ex.getError());
            }
            catch (Exception ex) {
                loanErrorLabel.setText("Oops, something went wrong! Could not pay back the loan!");
                ex.printStackTrace();
            }
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
        
        transactionTableView.getItems().addAll(App.currentAccountTransactions);

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
        
        contactTableView.getItems().addAll(App.currentCustomerContacts);

        /*
        LOANS (TABLE INIT)
        */

        TableColumn<BankAccount, Integer> loanAccountIdColumn = new TableColumn<>("Account ID");
        loanAccountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loanTableView.getColumns().add(loanAccountIdColumn);

        TableColumn<BankAccount, Double> amountColumn = new TableColumn<>("Balance");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        loanTableView.getColumns().add(amountColumn);

        TableColumn<BankAccount, String> dateColumn = new TableColumn<>("Purpose");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        loanTableView.getColumns().add(dateColumn);

        ArrayList<BankAccount> loans = App.currentCustomerAccounts.stream()
            .filter(account -> account.getBank().equals(App.currentAccount.getBank()) && account.isLoan())
            .collect(Collectors.toCollection(ArrayList::new));
        
        loanTableView.getItems().addAll(loans);
        updateAccountOverview();
    }


    /*
    INTEREST
    */

    public void getInterest(ActionEvent e) throws IOException, InterruptedException {
        overviewErrorLabel.setTextFill(Color.RED);

        try {
            ServerFacade.instance.requestsInterests(App.currentAccount.getId());
            overviewErrorLabel.setTextFill(Color.GREEN);
            overviewErrorLabel.setText("Interest successfully received!");
        }
        catch (HttpException ex) {
            overviewErrorLabel.setText(ex.getError());
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            overviewErrorLabel.setText("Oops, something went wrong! Could not request the interests!");
            ex.printStackTrace();
        }

        updateAccountOverview();
    }

    /*
    MISCELLANEOUS
    */

//Logs the user out.
    public void logout(ActionEvent e) throws IOException{
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
        ArrayList<BankAccount> loans = App.currentCustomerAccounts.stream()
            .filter(account -> account.getBank().equals(App.currentAccount.getBank()) && account.isLoan())
            .collect(Collectors.toCollection(ArrayList::new));

        for(BankAccount loan: loans){
            debt += loan.getBalance();
        }

        double netWorth = App.currentAccount.getBalance() - debt;

        transactionsMadeLabel.setText("Transactions Made: " + App.currentAccountTransactions.size() + " transactions");
        numberOfContactsLabel.setText("Number of Contacts: " + App.currentCustomerContacts.size() + " contacts");
        pendingLoansLabel.setText("Pending Loans: " + loans.size() + " loans");
        totalDebtLabel.setText("Total Debt: " + trunc(debt) + " SEK");
        netWorthLabel.setText(trunc(netWorth) + " SEK");

        if(netWorth > 0){
            netWorthLabel.setTextFill(Color.GREEN);
        }else if(netWorth < 0){
            netWorthLabel.setTextFill(Color.RED);
        }

        transactionTableView.getItems().setAll(App.currentAccountTransactions);
    }
//Updates notifications view.
    public void updateNotifications(){
        try {
            List<Notification> notifications = ServerFacade.instance.listAllNotificationsForThisUser();
            observableNotifications = FXCollections.observableArrayList();
            observableNotifications.addAll(notifications);
            notificationsListView.setItems(observableNotifications);
            noNotificationsLabel.setVisible(notifications.isEmpty());
        }
        catch (HttpException ex) {
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
