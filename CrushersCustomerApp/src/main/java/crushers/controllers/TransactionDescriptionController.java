package crushers.controllers;

import crushers.model.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TransactionDescriptionController{

    Transaction transaction;

    @FXML
    private Label transactionIdLabel, transactionAmountLabel, transactionFromLabel, transactionToLabel, transactionDescriptionLabel;

    public void init() {
        transactionIdLabel.setText(String.valueOf(transaction.getId()));
        transactionAmountLabel.setText(transaction.getAmount() + " SEK");
        transactionFromLabel.setText(transaction.getFromString() + "");
        transactionToLabel.setText(transaction.getToString() + "");
        transactionDescriptionLabel.setText(transaction.getDescription());

    }

    public void setCurrentTransaction(Transaction transaction){
        this.transaction = transaction;
    }


}
