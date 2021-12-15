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
        String automaticLabel;
        if(theLabel.trim().isEmpty()){
            errorLabel.setText("Label can not be empty.");
        }
        else if(theLabel.matches("^[0-9]+$"))
        {
            errorLabel.setText("Label can not contain only numbers.");
        }
        else {
            if (theLabel.contains("pharmacy") || theLabel.contains("gym") || theLabel.contains("doctor")) {
                automaticLabel = "Health & fitness";
            } else if (theLabel.contains("restaurant") || theLabel.contains("coffee") || theLabel.contains("food") || theLabel.contains("cafe")) {
                automaticLabel = "Food";
            } else if (theLabel.contains("rent") || theLabel.contains("bills")) {
                automaticLabel = "Housing & Utilities";
            } else if (theLabel.contains("car") || theLabel.contains("gas") || theLabel.contains("bus")) {
                automaticLabel = "Transportation";
            }
            else {
                automaticLabel = "Other";;
            }
            TransactionLabel transactionLabel = new TransactionLabel(automaticLabel, App.currentTransaction);
            App.currentTransaction.setLabel(automaticLabel);
            // Http.post("/transactions/label", transactionLabel);
            Util.closeAndShow("SystemView", "System",e);
        }
    }
    public void cancel(ActionEvent e) {
        Util.showModal("SystemView", "System view", e);
    }
}
