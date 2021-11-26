package crushers.controllers;

import java.io.IOException;

import crushers.App;
import crushers.model.Customer;
import crushers.model.User;
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
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label forgottenPasswordLabel;
    @FXML
    private Label requestHelpLabel;
    @FXML
    private ImageView loginScreenImage;
    @FXML
    private Label invalidLoginLabel;
    
    public void register(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/RegisterView.fxml"));
        root = loader.load();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Registration Form");
        stage.show();

    }

    public static Customer currentCustomer;

    public static AccountController accCtrl;

    public void login(ActionEvent e) throws IOException{

        String email = usernameField.getText();
        String password = passwordField.getText();

        for(Customer customer: App.crushersBank.getCustomers()){
            if(customer.getEmail().equals(email) && customer.getPassword().equals(password)){

            App.currentCustomer = customer;
            
            Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow(); // close upon login
            oldStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/AccountView.fxml"));
            root = loader.load();
            accCtrl = loader.getController();
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
