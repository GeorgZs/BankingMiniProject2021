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
                // accCtrl.displayName(customer.getFirstName() + " " + customer.getLastName());
                stage = new Stage();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getClassLoader().getResource("crushers/stylesheets/main.css").toExternalForm());
                stage.setScene(scene);
                stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
                stage.setTitle("Account Overview");
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

    public void forgottenPassword() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/ChangePasswordView.fxml"));
        root = loader.load();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Reset password");
        stage.show();
    }
}
