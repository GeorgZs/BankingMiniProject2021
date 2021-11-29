package crushers.controllers;

import crushers.App;
import crushers.model.Customer;
import crushers.model.PaymentAccount;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AccountTransferController implements Initializable{
    private Customer currentCustomer = App.currentCustomer;
    private String[] accounts;

    @FXML
    private Label transferFundsLabel, fromLabel, toLabel, amountLabel, sekLabel, commentLabel;

    @FXML
    private ChoiceBox accountFrom, accountTo;

    @FXML
    private TextField amountField;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button transferButton, cancelButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<PaymentAccount> userAccounts = App.currentCustomer.getAccountList();
        accounts = new String[userAccounts.size()];
        for(int i= 0; i< accounts.length; i++) {
            accounts[i] = userAccounts.get(i).getName();
        }
        accountFrom.getItems().addAll(accounts);
        accountTo.getItems().addAll(accounts);
        accountFrom.setOnAction(this::firstChoice);
    }

    private void firstChoice(Event event) {
        accountTo.getItems().remove(accountFrom.getValue());
    }

}

