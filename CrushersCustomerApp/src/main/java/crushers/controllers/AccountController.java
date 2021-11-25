package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import crushers.App;
import crushers.model.PaymentAccount;
import crushers.model.SavingsAccount;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class AccountController implements Initializable{
    
    private Stage stage;
    private Parent root;

    @FXML
    private ListView<PaymentAccount> accountList;
    @FXML
    private Button createNewAccountButton;
    @FXML
    private Button selectButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button logoutButton;



    public void displayName(String username){
        welcomeLabel.setText("Welcome, " + username);
    }

    public void logout(ActionEvent e) throws IOException{
        
        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();

        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/MainView.fxml"));
        root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setTitle("Crushers Bank");
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
    }

    public void createNewAccount(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/AccountCreationView.fxml"));
        root = loader.load();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Register new account");
        stage.show();
    }

    public void select(ActionEvent e){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(PaymentAccount account: App.currentCustomer.getAccountList()){
            accountList.getItems().add(account);
        }
    }

    public void addSavingsToList(SavingsAccount account){
        accountList.getItems().add(account);
    }
    public void addPaymentToList(PaymentAccount account){
        accountList.getItems().add(account);
    }
}
