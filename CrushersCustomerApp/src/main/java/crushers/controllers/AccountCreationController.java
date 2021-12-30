package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import crushers.App;
import crushers.model.Bank;
import crushers.model.PaymentAccount;
import crushers.model.SavingsAccount;
import crushers.util.Http;
import crushers.util.Json;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AccountCreationController implements Initializable{
    
    @FXML
    private ChoiceBox<Bank> bankSelection;
    @FXML
    private RadioButton paymentOption, savingsOption;
    @FXML
    private TextField accountDescriptionField;
    @FXML
    private Label invalidLabel;
    @FXML
    private Button createAccountButton;
    
    private Boolean isPayment = true;

    public void createAccount(ActionEvent e) throws IOException, InterruptedException{
        if(bankSelection.getValue() == null){
            invalidLabel.setText("Please select a bank!");
        }else if(isPayment == null){
            invalidLabel.setText("Please select an account type!");
        }else if(accountDescriptionField.getText().isBlank()){
            invalidLabel.setText("Please enter an account name!");
        }else{
            Bank bank = bankSelection.getValue();
            String accType = isPayment ? "payment" : "savings";
            String accName = accountDescriptionField.getText();
            // AccountController accCtrl = MainController.accCtrl;
            
            String bankString = "{\"bank\": {\"id\": " + bank.getId() + " },";
            bankString += "\"type\": \"" + accType + "\",";
            bankString += "\"name\": \"" + accName + "\"}";

            // String bankString = {
            //     "bank": { "id": 1003 },
            //     "type": "payment",
            //     "name": "testing stuff"
            // }

            if(isPayment){


                String createResponse = Http.authPost("accounts", App.currentToken, Json.toNode(bankString));
                System.out.println("Response:\n" + createResponse);
                // PaymentAccount createdAccount = Json.parse(createResponse, PaymentAccount.class);

                // System.out.println("Created account: " + createdAccount);
                // App.currentCustomer.createAccount(createdAccount);

            }else{
                String createResponse = Http.authPost("accounts", App.currentToken, Json.toNode(bankString));
                System.out.println("Response:\n" + createResponse);

                // SavingsAccount createdAccount = Json.parse(createResponse, SavingsAccount.class);
                // App.currentCustomer.createAccount(createdAccount);
            }

            MainController.accCtrl.updateAccountList();

            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            bankSelection.getItems().addAll(Json.parseList(Http.get("banks"), Bank.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setPayment(ActionEvent e){
        this.isPayment = true;
    }
    public void setSavings(ActionEvent e){
        this.isPayment = false;
    }
}
