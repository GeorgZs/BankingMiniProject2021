package crushers.controllers;

import crushers.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionDescriptionController implements Initializable {
    @FXML
    private TextField fromAccount, toAccount, amount, date;
    @FXML
    private TextArea description;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(App.currentTransaction.getAccountFrom().getNumber() == App.currentAccount.getNumber()) {
            fromAccount.setText(" Me");
        }
        else {
            fromAccount.setText(App.currentTransaction.getAccountFrom().getNumber());
        }
        if(App.currentTransaction.getAccountTo().getNumber() == App.currentAccount.getNumber()) {
            toAccount.setText(" Me");
        }
        else {
            toAccount.setText(App.currentTransaction.getAccountTo().getNumber());
        }
        amount.setText(App.currentTransaction.getAmount() + " SEK");
        date.setText(App.currentTransaction.getDate());
        description.setText(App.currentTransaction.getDescription());
    }
}
