package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import crushers.App;
import crushers.model.Bank;
import crushers.model.PaymentAccount;
import crushers.model.SavingsAccount;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private TextField accountDescriptionField, savingsGoalField;
    @FXML
    private Label savingsGoalLabel, sekLabel, invalidLabel;
    @FXML
    private Button createAccountButton;
    
    private Boolean isPayment = true;

    public void createAccount(ActionEvent e) throws IOException, InterruptedException{
        if(bankSelection.getValue() == null){
            invalidLabel.setText("Please select a bank!");
        }else if(isPayment == null){
            invalidLabel.setText("Please select an account type!");
        }else if(!isPayment && savingsGoalField.getText().isBlank()){
            invalidLabel.setText("Please enter a savings goal!");
        }else if(accountDescriptionField.getText().isBlank()){
            invalidLabel.setText("Please enter an account name!");
        }else{
            String accountName = accountDescriptionField.getText();
            Bank bank = bankSelection.getValue();
            String accType = isPayment ? "payment" : "savings";
            String accName = accountDescriptionField.getText();
            AccountController accCtrl = MainController.accCtrl;
            
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
                PaymentAccount createdAccount = Json.parse(createResponse, PaymentAccount.class);

                System.out.println("Created account: " + createdAccount);
                accCtrl.addPaymentToList(createdAccount);
                App.currentCustomer.createAccount(createdAccount);

            }else{
                try{

                double savingsGoal = Double.parseDouble(savingsGoalField.getText());
                
                String createResponse = Http.authPost("accounts", App.currentToken, Json.toNode(bankString));

                SavingsAccount createdAccount = Json.parse(createResponse, SavingsAccount.class);
                accCtrl.addSavingsToList(createdAccount);
                App.currentCustomer.createAccount(createdAccount);

                }catch(NumberFormatException nfe){
                    invalidLabel.setText("Please enter a valid savings goal!");
                    return;
                }
            }

            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setGoalVisible(ActionEvent e){ // savings account seletion
        savingsGoalLabel.setVisible(true);
        savingsGoalField.setVisible(true);
        sekLabel.setVisible(true);
        this.isPayment = false;
    }
    public void setGoalInvisible(ActionEvent e){ // payment account selection
        savingsGoalLabel.setVisible(false);
        savingsGoalField.setVisible(false);
        sekLabel.setVisible(false);
        this.isPayment = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            bankSelection.getItems().addAll(Json.parseList(Http.get("banks"), Bank.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        bankSelection.setStyle("-fx-font-family: SansSerif");
    }
}
