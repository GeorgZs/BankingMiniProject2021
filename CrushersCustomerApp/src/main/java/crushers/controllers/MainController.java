package crushers.controllers;

import java.io.IOException;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import crushers.App;
import crushers.model.Credentials;
import crushers.model.Customer;
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
        String loginResponse = "";

        try {
            loginResponse = Http.post("auth/login", new Credentials(email, password)).body();
            App.currentToken = Json.parse(loginResponse, Token.class).getToken(); // parse json string to token and get attr
            System.out.println(loginResponse);
        } catch (InterruptedException e1) { // idk what this is but its probably bad so I added return
            e1.printStackTrace();
            return;
        } catch (UnrecognizedPropertyException upe){ // if the response is an error json, we want to print it and edit label
            System.out.println(Json.stringify(loginResponse));
            invalidLoginLabel.setText("Invalid username or password!");
            return;
        }

        try {
            Customer loggedInCustomer = Json.parse(Http.authGet("customers/@me", App.currentToken).body(), Customer.class);
            App.currentCustomer = loggedInCustomer;
            System.out.println(Http.authGet("customers/@me", App.currentToken).body());
            System.out.println(App.currentCustomer);
            System.out.println(App.currentToken);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
            return;
        }

        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();
        FXMLLoader loader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/AccountView.fxml"));
        Stage stage = new Stage();
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Account Overview");
        stage.show();

        accCtrl = loader.getController();

        // Stage stage = Util.closeAndCreate("AccountView", "Account Overview", e);
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
