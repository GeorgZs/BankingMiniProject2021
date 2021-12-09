package crushers.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import crushers.App;
import crushers.model.Credentials;
import crushers.model.Customer;
import crushers.model.PaymentAccount;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainController { // test commit

    private Stage stage;
    private Parent root;
    
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

        // Util.showModal("RegisterView", "Registration Form", e);
        // we need to set stylesheet so this is commented. Uncomment in the future

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/RegisterView.fxml"));
        root = loader.load();
        stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("crushers/stylesheets/main.css").toExternalForm());
        stage.setScene(scene);

        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Registration Form");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    public void login(ActionEvent e) throws IOException{

        String email = usernameField.getText();
        String password = passwordField.getText();

        try { // this block sends the email and password to the api and stores the token
            String responseString = Http.post("auth/login", new Credentials(email, password)); // json string resposne
            System.out.println(responseString);
            if(responseString.contains("error")){ // if the response is an error, print its value
                System.out.println(Json.toNode(responseString).get("error").asText());
                invalidLoginLabel.setText("Invalid E-mail or Password!");
                return;
            }
            App.currentToken = Json.toNode(responseString).get("token").asText(); // else we get a valid token
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        try { // this block uses the token to find the logged in customer and set their account list
            App.currentCustomer = Json.parse(Http.authGet("customers/@me", App.currentToken), Customer.class);
            List<PaymentAccount> accounts = Json.parseList(Http.authGet("accounts/@me", App.currentToken), PaymentAccount.class);
            App.currentCustomer.setAccountList(new ArrayList<>(accounts));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        // Util.closeAndShow("AccountView", "Account Overview", e); same spiel

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

        stage.getScene().getStylesheets().add(getClass().getClassLoader().getResource("crushers/stylesheets/main.css").toExternalForm());
        stage.show();

    }

    public void requestHelp(){
        System.out.println("cry about it");
    }

    public void forgottenPassword() throws IOException {
        Util.showModal("ChangePasswordView", "Reset Password", new ActionEvent());
    }
}
