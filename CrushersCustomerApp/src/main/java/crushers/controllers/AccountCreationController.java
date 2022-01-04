package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
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
        } else{
            BankAccount account = new BankAccount();
            account.setType(isPayment ? "payment" : "savings");
            account.setBank(bankSelection.getValue());

            if (account.isSavings() && !accountDescriptionField.getText().isBlank()) {
                account.setName(accountDescriptionField.getText());
            }

            try {
                ServerFacade.instance.createBankAccount(account);
                MainController.accCtrl.updateAccountList();
            }
            catch (HttpException ex) {
                invalidLabel.setText(ex.getError());
                System.out.println(ex.getError());
            }
            catch (Exception ex) {
                invalidLabel.setText("Oops, something went wrong! Could not create the bank account!");
                ex.printStackTrace();
            }

            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            bankSelection.getItems().addAll(ServerFacade.instance.listAllBanks());
        } 
        catch (HttpException ex) {
            System.out.println(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void setPayment(ActionEvent e){
        this.isPayment = true;
    }
    public void setSavings(ActionEvent e){
        this.isPayment = false;
    }
}
