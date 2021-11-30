package crushers.controllers;

import crushers.App;
import crushers.model.Bank;
import crushers.model.Customer;
import crushers.model.PaymentAccount;
import crushers.model.Transaction;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AccountTransferController implements Initializable {
    private Customer currentCustomer = App.currentCustomer;
    private String[] accounts;



    @FXML
    private Label transferFundsLabel, fromLabel, toLabel, amountLabel, sekLabel, commentLabel, errorLabel;

    @FXML
    private ChoiceBox accountFromBox, accountToBox;

    @FXML
    private TextField amountField;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button transferButton, cancelButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<PaymentAccount> userAccounts = App.currentCustomer.getAccountList();
        accountFromBox.setStyle("-fx-font-family: SansSerif");
        accountToBox.setStyle("-fx-font-family: SansSerif");
        accounts = new String[userAccounts.size()];
        for(int i= 0; i< accounts.length; i++) {
            accounts[i] = userAccounts.get(i).getName();
        }
        accountFromBox.getItems().addAll(accounts);
        accountToBox.getItems().addAll(accounts);
        accountFromBox.setOnAction(this::firstChoice);
    }

    private void firstChoice(Event event) {
        accountToBox.getItems().remove(accountFromBox.getValue());
    }

    private void transferFunds(ActionEvent e) throws Exception {
        if (accountFromBox.getValue() == null) {
            errorLabel.setText("Please select an account to transfer funds from.");
        } else if (accountToBox.getValue() == null) {
            errorLabel.setText("Please select an account to transfer funds to.");
        } else if (amountField.getText() == null) {
            errorLabel.setText("Please enter an amount to transfer!");
        } else {
           PaymentAccount paymentAccountFrom = (PaymentAccount) accountFromBox.getValue();
           PaymentAccount paymentAccountTo = (PaymentAccount) accountToBox.getValue();
           double amountSek = Double.parseDouble(amountField.getText());
           String comment = commentArea.getText();

        //    Transaction transaction = new Transaction(App.currentCustomer.getId(), paymentAccountFrom, paymentAccountTo, amountSek, comment,null);
        Transaction transaction = new Transaction(333, paymentAccountFrom, paymentAccountTo, amountSek, comment,null);

        }
    }

}



