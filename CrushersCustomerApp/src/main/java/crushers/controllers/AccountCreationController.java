package crushers.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import crushers.App;
import crushers.model.Bank;
import crushers.model.PaymentAccount;
import crushers.model.SavingsAccount;
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
    
    private Boolean isPayment;

    public void createAccount(ActionEvent e){
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
            AccountController accCtrl = MainController.accCtrl;

            /*

            This needs attention
             */
            if(isPayment){
                PaymentAccount account = new PaymentAccount(accountName, 0, bank);
                accCtrl.addAccountToList(account);
                // accCtrl.addPaymentToList(account);
                App.currentCustomer.createAccount(account);
            }else{
                try{

                double savingsGoal = Double.parseDouble(savingsGoalField.getText());
                SavingsAccount account = new SavingsAccount(accountName, 0, savingsGoal, bank);
                accCtrl.addSavingsToList(account);
                App.currentCustomer.createAccount(account);

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
        bankSelection.getItems().addAll(App.banks);
        savingsGoalLabel.setVisible(false);
        savingsGoalField.setVisible(false);
        sekLabel.setVisible(false);
    }
}
