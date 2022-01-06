package crushers.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;

import crushers.App;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import crushers.util.Util;

public class MainController { // test commit
    
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton, registerButton;
    @FXML
    private Label forgottenPasswordLabel, requestHelpLabel, invalidLoginLabel;
    @FXML
    private ImageView loginScreenImage;

    public static AccountController accCtrl;
    
    public void register(ActionEvent e) throws IOException{
        Util.showModal("RegisterView", "Registration Form", e);
    }

    public void login(ActionEvent e) throws IOException, InterruptedException{
        Credentials credentials = new Credentials();
        credentials.setEmail(usernameField.getText());
        credentials.setPassword(passwordField.getText());

        try {
            ServerFacade.instance.loginUser(credentials);
            App.currentCustomer = ServerFacade.instance.getLoggedInCustomer();
        }
        catch (HttpException ex) {
            invalidLoginLabel.setText("Invalid E-mail or Password!");
            System.out.println(ex.getError());
            return;
        }
        catch (Exception ex) {
            invalidLoginLabel.setText("Invalid E-mail or Password!");
            ex.printStackTrace();
            return;
        }

        // Get the customer's accounts
        try {
            App.currentCustomerAccounts = new ArrayList<BankAccount>();
            App.currentCustomerAccounts = ServerFacade.instance.listAllBankAccountsOfThisCustomer();
        }
        catch (HttpException ex) {
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // Get the customer's contacts
        try {
            App.currentCustomerContacts = new ArrayList<Contact>();
            App.currentCustomerContacts = ServerFacade.instance.listAllContactsOfThisCustomer();
        }
        catch (HttpException ex) {
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();
        FXMLLoader loader = new FXMLLoader(App.class.getClassLoader().getResource("crushers/views/AccountView.fxml"));
        Stage stage = new Stage();
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Account Overview");
        stage.show();

        accCtrl = loader.getController();

        stage.show();

    }
}
