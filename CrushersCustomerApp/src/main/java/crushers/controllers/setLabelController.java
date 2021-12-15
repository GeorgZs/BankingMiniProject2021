package crushers.controllers;

import crushers.App;
import crushers.model.TransactionLabel;
import crushers.util.Http;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;


public class setLabelController {
    @FXML
    private TextField labelField;
    @FXML
    private Label errorLabel;

    public void submitLabel(ActionEvent e) throws IOException, InterruptedException {
        String theLabel = labelField.getText();
        if(theLabel.trim().isEmpty()){
            errorLabel.setText("Label can not be empty.");
        }
        else if(theLabel.matches("^[0-9]+$"))
        {
            errorLabel.setText("Label can not contain only numbers.");
        }
        else {
            TransactionLabel transactionLabel = new TransactionLabel(theLabel, App.currentTransaction);
            App.currentTransaction.setLabel(theLabel);
            // Http.post("/transactions/label", transactionLabel);
            Util.closeAndShow("SystemView", "System",e);
        }
    }
}
