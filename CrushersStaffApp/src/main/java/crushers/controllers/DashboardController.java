package crushers.controllers;

import crushers.WindowManager;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController implements Initializable {
    @FXML
    private VBox sidebar;
    @FXML
    private Button addStaff;
    @FXML
    private Button accountInformation;
    @FXML
    private Button registerStaff;
    @FXML
    private Button bankAccounts;
    @FXML
    private Button notificationButton;
    @FXML
    private HBox logout;
    @FXML
    private GridPane staffOverview;
    @FXML
    private Pane staffInformation;
    @FXML
    private Pane addClerk;
    @FXML
    private Pane creatingStaff;
    @FXML
    private Pane transactions;
    @FXML
    private Pane transactionBar;
    @FXML
    private Pane notificationBar;
    @FXML
    private Pane notification;
    @FXML
    private Pane deposit;
    @FXML
    private Pane withdraw;
    @FXML
    private Pane transferBetween;
    @FXML
    private Pane bankAccount;
    @FXML
    private Pane savingAccount;
    @FXML
    private Pane paymentAccount;
    @FXML
    private Pane accountBar;
    @FXML
    private Pane interestRate;
    @FXML
    private Pane listTransaction;
    @FXML
    private Pane susTransaction;
    @FXML
    private TextField clerkFirstName;
    @FXML
    private TextField clerkLastName;
    @FXML
    private TextField clerkStreetAddress;
    @FXML
    private TextField city;
    @FXML
    private TextField clerkPostal;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private ImageView plus;
    @FXML
    private ImageView plus2;
    @FXML
    private ImageView plus3;
    @FXML
    private ImageView plus4;
    @FXML
    private ImageView plus5;
    @FXML
    private TableColumn<User, Integer> clerkID;
    @FXML
    private TableColumn<User, String> firstNameClerk;
    @FXML
    private TableColumn<User, String> lastNameClerk;
    @FXML
    private TableColumn<User, String> emailClerk;
    @FXML
    private TableColumn<User, String> typeClerk;
    @FXML
    private TableView<User> tableView;
    @FXML
    private Button sendNotificationButton;
    @FXML
    private TextArea notificationMessage;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clerkID.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameClerk.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameClerk.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailClerk.setCellValueFactory(new PropertyValueFactory<>("email"));
        typeClerk.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableView.setStyle("-fx-font-family: SansSerif");
        
        executor.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                try {
                    List<User> clerks = ServerFacade.instance.listAllClerks();
                    tableView.getItems().clear();
                    tableView.getItems().addAll(clerks);
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }, 0, 15, TimeUnit.SECONDS);
    }

    @FXML
    private void onHover(MouseEvent mouseEvent) {
        try{
            HBox logout = (HBox) mouseEvent.getSource();
            logout.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickedStaff(javafx.event.ActionEvent event) throws Exception {
        if(addClerk.isVisible() || creatingStaff.isVisible()){
            addClerk.setVisible(false);
            creatingStaff.setVisible(false);
            plus.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        } else {
            hideAllTabs();
            addClerk.setVisible(true);
            creatingStaff.setVisible(true);
            plus.setImage(new Image("file:src/main/resources/crushers/images/icons8-minus-48.png"));
        }
    }

    @FXML
    private void onClickedInformation(javafx.event.ActionEvent event) throws Exception {
        if(staffOverview.isVisible() || staffInformation.isVisible()) {
            staffOverview.setVisible(false);
            staffInformation.setVisible(false);
            plus2.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        } else {
            hideAllTabs();
            staffOverview.setVisible(true);
            staffInformation.setVisible(true);
            plus2.setImage(new Image("file:src/main/resources/crushers/images/icons8-minus-48.png"));
        }
    }

    @FXML
    private void onClickedTransaction(javafx.event.ActionEvent event) throws Exception {
        if(transactions.isVisible() || transactionBar.isVisible()) {
            transactions.setVisible(false);
            transactionBar.setVisible(false);
            plus3.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        } else {
            hideAllTabs();
            transactions.setVisible(true);
            transactionBar.setVisible(true);
            plus3.setImage(new Image("file:src/main/resources/crushers/images/icons8-minus-48.png"));
        }

    }

    @FXML
    private void onClickedBankAccounts(javafx.event.ActionEvent event) throws Exception {
        if(bankAccount.isVisible() || accountBar.isVisible()) {
            bankAccount.setVisible(false);
            accountBar.setVisible(false);
            plus4.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        } else {
            hideAllTabs();
            bankAccount.setVisible(true);
            accountBar.setVisible(true);
            plus4.setImage(new Image("file:src/main/resources/crushers/images/icons8-minus-48.png"));
        }

    }

    @FXML
    private void onClickedNotification(javafx.event.ActionEvent actionEvent) throws Exception {
        if (notification.isVisible() || notificationBar.isVisible()) {
            notification.setVisible(false);
            notificationBar.setVisible(false);
            plus5.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        } else {
            hideAllTabs();
            notification.setVisible(true);
            notificationBar.setVisible(true);
            plus5.setImage(new Image("file:src/main/resources/crushers/images/icons8-minus-48.png"));
        }
    }

    @FXML
    private void hideAllTabs() {
        addClerk.setVisible(false);
        creatingStaff.setVisible(false);
        plus.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        
        staffOverview.setVisible(false);
        staffInformation.setVisible(false);
        plus2.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        
        transactions.setVisible(false);
        transactionBar.setVisible(false);
        plus3.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        
        bankAccount.setVisible(false);
        accountBar.setVisible(false);
        plus4.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));

        notification.setVisible(false);
        notificationBar.setVisible(false);
        plus5.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
    }

    @FXML
    public void onClickedLogout(MouseEvent mouseEvent) throws Exception {
        try {
            ServerFacade.instance.logoutUser();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        WindowManager.showPage(WindowManager.Pages.Login);
    }

    @FXML
    private void onHoverEnded(MouseEvent mouseEvent) {
        try{
            HBox logout = (HBox)mouseEvent.getSource();
            logout.setOpacity(1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registerClerk(javafx.event.ActionEvent event) throws Exception {
        String clerkFirst = clerkFirstName.getText();
        String clerkLast = clerkLastName.getText();
        String clerkAddress = clerkStreetAddress.getText();
        String clerkCity = city.getText();
        String clerkZip = clerkPostal.getText();
        String clerkEmail = email.getText();
        String clerkPassword = password.getText();

        if(clerkFirst.isEmpty() || clerkFirst.isBlank()) {
            clerkFirstName.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clerkFirstName.setStyle("-fx-border-color: transparent");
        } if(clerkLast.isEmpty() || clerkLast.isBlank()) {
            clerkLastName.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clerkLastName.setStyle("-fx-border-color: transparent");
        } if(clerkAddress.isEmpty() || clerkAddress.isBlank()) {
            clerkStreetAddress.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clerkStreetAddress.setStyle("-fx-border-color: transparent");
        } if(clerkCity.isBlank() || clerkCity.isEmpty()) {
            city.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            city.setStyle("-fx-border-color: transparent");
        } if(clerkZip.isEmpty() || clerkZip.isBlank()) {
            clerkPostal.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clerkPostal.setStyle("-fx-border-color: transparent");
        } if(clerkEmail.isEmpty() || clerkEmail.isBlank() || !clerkEmail.contains("@")) {
            email.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            email.setStyle("-fx-border-color: transparent");
        } if(clerkPassword.isEmpty() || clerkPassword.isBlank() || clerkPassword.length() < 8) {
            password.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            password.setStyle("-fx-border-color: transparent");
        }

        User clerk = new User();
        clerk.setFirstName(clerkFirst);
        clerk.setLastName(clerkLast);
        clerk.setAddress(clerkAddress);
        clerk.setEmail(clerkEmail);
        clerk.setPassword(clerkPassword);

        try {
            ServerFacade.instance.createClerk(clerk);
            WindowManager.showAlert("Clerk successfully created!");
        }         
        catch (HttpException ex) {
            WindowManager.showAlert(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onHoverDeposit(MouseEvent mouseEvent) {
        try {
            Pane deposit = (Pane)mouseEvent.getSource();
            deposit.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onHoverEndedDeposit(MouseEvent mouseEvent) {
        try{
            Pane deposit = (Pane)mouseEvent.getSource();
            deposit.setOpacity(1);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onHoverWithdraw(MouseEvent mouseEvent) {
        try {
            Pane withdraw = (Pane) mouseEvent.getSource();
            withdraw.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onHoverEndedWithdraw(MouseEvent mouseEvent) {
        try {
            Pane withdraw = (Pane) mouseEvent.getSource();
            withdraw.setOpacity(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onClickedDeposit(MouseEvent mouseEvent) throws IOException {
        WindowManager.showModal(WindowManager.Pages.Deposit, "Crushers Bank - Deposit");
    }

    @FXML
    private void onClickedWithdraw(MouseEvent mouseEvent) throws IOException {
        WindowManager.showModal(WindowManager.Pages.Withdraw, "Crushers Bank - Withdraw");
    }

    @FXML
    private void onHoverPayment(MouseEvent mouseEvent) throws IOException {
        try {
            Pane paymentAccount = (Pane) mouseEvent.getSource();
            paymentAccount.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onHoverPaymentEnded(MouseEvent mouseEvent) throws IOException {
        try {
            Pane paymentAccount = (Pane) mouseEvent.getSource();
            paymentAccount.setOpacity(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickedPayment(MouseEvent mouseEvent) throws IOException {
        WindowManager.showModal(WindowManager.Pages.Payment, "Crushers Bank - Payment account");
    }

    @FXML
    private void onHoverSavings(MouseEvent mouseEvent) throws IOException {
        try {
            Pane savingAccount = (Pane) mouseEvent.getSource();
            savingAccount.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onHoverSavingsEnded(MouseEvent mouseEvent) throws IOException {
        try {
            Pane savingAccount = (Pane) mouseEvent.getSource();
            savingAccount.setOpacity(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickedSavings(MouseEvent mouseEvent) throws IOException {
        WindowManager.showModal(WindowManager.Pages.Savings, "Crushers Bank - Savings account");
    }

    @FXML
    private void sendNotification(javafx.event.ActionEvent event) throws Exception {
        if(notificationMessage.getText().isEmpty() || notificationMessage.getText().isBlank()) {
            notificationMessage.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            notificationMessage.setStyle("-fx-border-color: transparent");
        }

        Notification notification = new Notification();
        notification.setContent(notificationMessage.getText());

        try {
            ServerFacade.instance.sendNotification(notification);
            WindowManager.showAlert("Notification sent!");
        }
        catch (HttpException ex) {
            WindowManager.showAlert(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void onHoverInterest(MouseEvent mouseEvent) throws Exception {
        try {
            Pane interestRate = (Pane) mouseEvent.getSource();
            interestRate.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onHoverInterestEnded(MouseEvent mouseEvent) throws Exception {
        try {
            Pane interestRate = (Pane) mouseEvent.getSource();
            interestRate.setOpacity(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickedInterest(MouseEvent mouseEvent) throws Exception {
        WindowManager.showModal(WindowManager.Pages.Interest, "Crushers Bank - Interest rate");

    }

    @FXML
    private void onHoverListOfTransaction(MouseEvent mouseEvent) throws Exception {
        try {
            Pane listTransaction = (Pane) mouseEvent.getSource();
            listTransaction.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onHoverListOfTransactionEnded(MouseEvent mouseEvent) throws Exception {
        try {
            Pane listTransaction = (Pane) mouseEvent.getSource();
            listTransaction.setOpacity(1);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickedListOfTransaction(MouseEvent mouseEvent) throws Exception {
        WindowManager.showModal(WindowManager.Pages.TransactionList, "Crushers Bank - Transaction List");
    }

    @FXML
    private void onHoverSuspiciousTransaction(MouseEvent mouseEvent) throws Exception {
        try {
            Pane susTransaction = (Pane) mouseEvent.getSource();
            susTransaction.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onHoverSuspiciousTransactionEnded(MouseEvent mouseEvent) {
        try {
            Pane susTransaction = (Pane) mouseEvent.getSource();
            susTransaction.setOpacity(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickedSuspiciousTransaction(MouseEvent mouseEvent) throws Exception {
        WindowManager.showModal(WindowManager.Pages.SuspiciousTransaction, "Crushers Bank - Suspicious Transactions");

    }
}
