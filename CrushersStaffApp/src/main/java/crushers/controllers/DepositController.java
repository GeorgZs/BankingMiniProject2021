package crushers.controllers;

import crushers.WindowManager;
import crushers.api.Http;
import crushers.api.HttpError;
import crushers.api.ServerFacade;
import crushers.datamodels.Bank;
import crushers.datamodels.BankAccount;
import crushers.datamodels.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.util.List;

public class DepositController {
    @FXML
    private TextField amount;
    @FXML
    private ChoiceBox<BankAccount> accountsTo;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker dateOnDeposit;
    @FXML
    private Button depositButton;
    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        List<BankAccount> accounts = null;
        try {
            accounts = ServerFacade.instance.listAllBankAccountsAtThisBank();
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountsTo.setItems(FXCollections.observableArrayList(accounts));
        accountsTo.setStyle("-fx-font-family: SansSerif");
    }


    @FXML
    private void onPressedDeposit(javafx.event.ActionEvent actionEvent) throws Exception {
        /*if(Integer.parseInt(amount.getText()) < 0) {
          amount.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
          throw new Exception("Amount cannot be negative");
      } else {
            amount.setStyle("-fx-border-color: transparent");
      }

         */
        Transaction deposit = new Transaction();
        accountsTo.getValue().setType("payment");
        deposit.setTo(accountsTo.getValue());
        deposit.setAmount(Double.parseDouble(amount.getText()));
        deposit.setDescription(description.getText());
        //deposit.setDate(dateOnDeposit.get);

        try {
            ServerFacade.instance.deposit(deposit);
            System.out.println("Deposited: " + amount.getText());
            WindowManager.closeModal();
        } catch (Exception e) {
            if (e instanceof HttpError) showAlert(((HttpError)e).getError());
            e.printStackTrace();
        }
    }

    @FXML
    private void onPressedCancel(javafx.event.ActionEvent actionEvent) throws Exception {
        WindowManager.closeModal();
    }

    private void showAlert(String message){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Warning");
        a.setHeaderText(message);
        a.getDialogPane().setStyle("-fx-font-family: SansSerif");
        a.show();
    }



}
