package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import crushers.App;
import crushers.model.Customer;
import crushers.util.Http;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SystemController implements Initializable{

    AccountController accountController = new AccountController();
    @FXML
    private Label welcomeLabel, errorLabel;
    @FXML
    private Pane pane;
    @FXML
    private TabPane tabPane;
    
    public void logout(ActionEvent e) throws IOException{
        Util.showAlert("Logging out?", "Are you sure you want to log-out?", e);
    }
    public void selectDifferentAccount(ActionEvent e) throws IOException{
        Util.closeAndShow("AccountView", "Select an Account", e);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome " + App.currentCustomer.getFirstName() + " " + App.currentCustomer.getLastName());
    }
    public void getInterest(ActionEvent e) throws IOException, InterruptedException {
        ArrayList<Integer> years = new ArrayList<>();
        if (years.contains(LocalDateTime.now().getYear())){
            errorLabel.setText("Already received interest this year");
        } else {
            years.add(LocalDateTime.now().getYear());
            Http.post("transactions/interestRate/" + accountController.getAccount().getId(), Customer.class);
        }
    }
    public void createContact(ActionEvent e) throws IOException, InterruptedException {

    }
}