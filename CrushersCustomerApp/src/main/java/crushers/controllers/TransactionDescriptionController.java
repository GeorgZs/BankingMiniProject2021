package crushers.controllers;

import crushers.common.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TransactionDescriptionController{

    Transaction transaction;

    @FXML
    private Label transactionIdLabel, transactionAmountLabel, transactionFromLabel, transactionToLabel, transactionDescriptionLabel, transactionLabelLabel;

    public void init() {
        transactionIdLabel.setText(String.valueOf(transaction.getId()));
        transactionAmountLabel.setText(transaction.getAmount() + " SEK");
        transactionFromLabel.setText(transaction.getFrom().toString());
        transactionToLabel.setText(transaction.getTo().toString());
        transactionDescriptionLabel.setText(transaction.getDescription());
        transactionLabelLabel.setText(String.join(", ", transaction.getLabels()));
    }

    public void setCurrentTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}


