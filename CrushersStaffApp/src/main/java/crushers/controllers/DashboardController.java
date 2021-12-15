package crushers.controllers;

import crushers.WindowManager;
import crushers.api.HttpError;
import crushers.api.ServerFacade;
import crushers.datamodels.Notification;
import crushers.datamodels.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import java.util.*;

public class DashboardController {
    @FXML
    private VBox sidebar;
    @FXML
    private Button addStaff;
    @FXML
    private Button accountInformation;
    @FXML
    private Button registerStaff;
    @FXML
    private Button createAccount;
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
    private Pane createAccounts;
    @FXML
    private Pane savingAccount;
    @FXML
    private Pane paymentAccount;
    @FXML
    private Pane accountBar;
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
    private ChoiceBox<String> clerkSecurityQuestion;
    @FXML
    private TextField clerkAnswer;
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
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TableColumn<User, Integer> clerkID;
    @FXML
    private TableColumn<User, String>firstNameClerk;
    @FXML
    private TableColumn<User, String>lastNameClerk;
    @FXML
    private TableColumn<User, String>emailClerk;
    @FXML
    private TableColumn<User, String>addressClerk;
    @FXML
    private TableView tableView;
    @FXML
    private Button sendNotificationButton;
    @FXML
    private Button cancelNotificationButton;
    @FXML
    private TextArea notificationMessage;

    private String[] clerkQuestion = {
            "What's the name of your first pet?",
            "What's the name of your home-town?",
            "What's your favorite movie?",
            "Which high-school did you graduate?",
            "What's your mother's maiden name?",
            "What's the name of your first school?",
            "What was your favorite food as a child?",
            "What's your favorite book?"

    };


    @FXML
    void initializeTable() {
        clerkID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        firstNameClerk.setCellValueFactory(new PropertyValueFactory<>("First name"));
        lastNameClerk.setCellValueFactory(new PropertyValueFactory<>("Last name"));
        emailClerk.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressClerk.setCellValueFactory(new PropertyValueFactory<>("street address"));

        tableView.setItems(createList());

    }

    private ObservableList createList() {

            // try {
                // Collection<Clerk> clerkList = new ArrayList<>(); // Http.get("/staff/@bank", Collection.class);
                ObservableList informationClerk = FXCollections.observableArrayList();;
            //     for (Clerk clerk: clerkList) {
            //         informationClerk.add(new ClerkTableView(clerk.getId(),clerk.getFirstName(),clerk.getLastName(),clerk.getEmail(),clerk.getAddress()));
            //     }
                return informationClerk;
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
            // return null;
    }

    @FXML
    void initialize(){
        clerkSecurityQuestion.setItems(FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList(clerkQuestion))));
        clerkSecurityQuestion.setStyle("-fx-font-family: SansSerif");
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
            transactions.setVisible(true);
            transactionBar.setVisible(true);
            plus3.setImage(new Image("file:src/main/resources/crushers/images/icons8-minus-48.png"));
        }

    }

    @FXML
    private void onClickedCreateAccount(javafx.event.ActionEvent event) throws Exception {
        if(createAccounts.isVisible() || accountBar.isVisible()) {
            createAccounts.setVisible(false);
            accountBar.setVisible(false);
            plus4.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        } else {
            createAccounts.setVisible(true);
            accountBar.setVisible(true);
            plus4.setImage(new Image("file:src/main/resources/crushers/images/icons8-minus-48.png"));
        }

    }

    @FXML
    public void onClickedLogout(MouseEvent mouseEvent) throws Exception {
        ServerFacade.instance.logoutUser();
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

        //Clerk clerk = new Clerk(clerkEmail, clerkFirst,clerkLast,clerkAddress, clerkPassword, null, null);
        // Http.post("/staff", clerk, Clerk.class);

        User clerk = new User();
        clerk.setFirstName(clerkFirst);
        clerk.setLastName(clerkLast);
        clerk.setAddress(clerkAddress);
        clerk.setEmail(clerkEmail);
        clerk.setPassword(clerkPassword);

        try {
            ServerFacade.instance.createClerk(clerk);

        } catch (Exception e) {
            if(e instanceof HttpError) showAlert(((HttpError)e).getError());
            e.printStackTrace();
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
    private void onHoverTransBetween(MouseEvent mouseEvent) {
        try {
            Pane transferBetween = (Pane) mouseEvent.getSource();
            transferBetween.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onHoverTransBetweenEnded(MouseEvent mouseEvent) {
        try {
            Pane transferBetween = (Pane) mouseEvent.getSource();
            transferBetween.setOpacity(1);
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
    private void onClickedNotification(javafx.event.ActionEvent actionEvent) throws Exception {
        if (notification.isVisible() || notificationBar.isVisible()) {
            notification.setVisible(false);
            notificationBar.setVisible(false);
            plus5.setImage(new Image("file:src/main/resources/crushers/images/icons8-plus-48.png"));
        } else {
            notification.setVisible(true);
            notificationBar.setVisible(true);
            plus5.setImage(new Image("file:src/main/resources/crushers/images/icons8-minus-48.png"));
        }

    }

    @FXML
    private void sendNotification(javafx.event.ActionEvent event) throws Exception {
        if(notificationMessage.getText().isEmpty() || notificationMessage.getText().isBlank()) {
            notificationMessage.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            notificationMessage.setStyle("-fx-border-color: transparent");
        }

        Notification notification = new Notification();
        notification.setNotification(notificationMessage.getText());

        try {
            ServerFacade.instance.sendNotification(notification);
        } catch (Exception e) {
            if(e instanceof HttpError) showAlert(((HttpError)e).getError());
            e.printStackTrace();
        }

    }

    private void showAlert(String message){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Warning");
        a.setHeaderText(message);
        a.getDialogPane().setStyle("-fx-font-family: SansSerif");
        a.show();
    }




    @FXML
    public void searchStaff() {

    }

    /*
    public void createTransaction(){
        gettexfields - from those text fields
        find the acccont to or the one you are withdrawing from
        call your method (___,___, balance, description)
        Transaction transaction = new Transaction(account1, account2, amount.getTextField, description.getTextfield);
        balance = from text field
        description = from text field
        HTTP.post("/transactions", transaction, Transaction.class)
    }
     */


    /*
        String[] security = new String[10];
        //security[0] = clerkPassword;
        //need question at index 0 and answer index 1

    }

     */
}
