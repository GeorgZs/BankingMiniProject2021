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
        if(App.currentTransaction.getFrom().getNumber() == App.currentAccount.getNumber()) {
            fromAccount.setText(" Me");
            amount.setText("-" + App.currentTransaction.getAmount() + " SEK");
        }
        else {
<<<<<<< HEAD
            fromAccount.setText(App.currentTransaction.getFrom().getNumber());
=======
            amount.setText("+" + App.currentTransaction.getAmount() + " SEK");
            fromAccount.setText(App.currentTransaction.getAccountFrom().getNumber());
>>>>>>> a353cbd92c22e28dd065f6cfac517715584839dd
        }
        if(App.currentTransaction.getTo().getNumber() == App.currentAccount.getNumber()) {
            toAccount.setText(" Me");
        }
        else {
            toAccount.setText(App.currentTransaction.getTo().getNumber());
        }
        amount.setText(App.currentTransaction.getAmount() + " SEK");
        date.setText(App.currentTransaction.getDate());
        description.setText(App.currentTransaction.getDescription());
    }
}
