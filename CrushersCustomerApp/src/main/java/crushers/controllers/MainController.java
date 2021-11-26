package crushers.controllers;

import java.io.IOException;

import crushers.App;
import crushers.model.Customer;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    public void register(ActionEvent e) throws IOException{

        root = Util.registerLoader.load();
        stage = new Stage();
        stage.setScene(new Scene(root));

        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Registration Form");
        stage.show();

    }

    public static AccountController accCtrl;

    public void login(ActionEvent e) throws IOException{

        String email = usernameField.getText();
        String password = passwordField.getText();

        for(Customer customer: App.crushersBank.getCustomers()){
            if(customer.getEmail().equals(email) && customer.getPassword().equals(password)){

            App.currentCustomer = customer;
            
            Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow(); // close upon login
            oldStage.close();

            root = Util.accountLoader.load();
            accCtrl = Util.accountLoader.getController();
            accCtrl.displayName(customer.getFirstName() + " " + customer.getLastName());
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle("Select an Account");
            stage.show();
            return;
            }
        }  
        invalidLoginLabel.setText("Invalid username or password!");
    }

    public void requestHelp(){
        System.out.println("cry about it");
        for(Customer customer: App.crushersBank.getCustomers()){
            System.out.println(customer);
        }
    }

    public void forgottenPassword(){
        System.out.println("lol dummy");
    }

}
