package crushers.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import crushers.App;
import crushers.model.PaymentAccount;
import crushers.util.Http;
import crushers.util.Json;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountTransferController implements Initializable {
    
    @FXML
    private Label transferFundsLabel, fromLabel, toLabel, amountLabel, sekLabel, commentLabel, errorLabel;

    @FXML
    private ChoiceBox<PaymentAccount> accountFromBox, accountToBox;

    @FXML
    private TextField amountField;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button transferFunds, done;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<PaymentAccount> userAccounts = App.currentCustomer.getAccountList();
        accountFromBox.setStyle("-fx-font-family: SansSerif");
        accountToBox.setStyle("-fx-font-family: SansSerif");
        accountFromBox.getItems().addAll(userAccounts);
        accountToBox.getItems().addAll(userAccounts);
        accountFromBox.setOnAction(this::firstChoice);
    }
    private void firstChoice(Event event) {
        accountToBox.getItems().remove(accountFromBox.getValue());
    }
    @FXML
    private void transferFunds(ActionEvent e) throws IOException, InterruptedException {
        if (accountFromBox.getValue() == null) {
            errorLabel.setText("Please select an account to transfer funds from.");
        } else if (accountToBox.getValue() == null) {
            errorLabel.setText("Please select an account to transfer funds to.");
        } else if (amountField.getText() == null) {
            errorLabel.setText("Please enter an amount to transfer!");
        } else if (!amountField.getText().matches("^[0-9]+$")){
            errorLabel.setText("Please enter a valid numeric amount.");
        } else {

            double amountSek = 0;
            JsonNode toNode = Json.nodeWithFields("id", accountToBox.getValue().getId(), "type", "payment");
            JsonNode fromNode = Json.nodeWithFields("id", accountFromBox.getValue().getId(), "type", "payment");
            try{
                amountSek = Double.parseDouble(amountField.getText());
            }catch(NumberFormatException nfe){
                errorLabel.setText("Please enter valid funds!");
                return;
            }
            
            String comment = commentArea.getText();
            if (amountSek > accountFromBox.getValue().getBalance()) {
                errorLabel.setText("Insufficient funds!");
            }else if(amountSek <= 0){
                errorLabel.setText("Please enter sufficient funds!");
            }else if(comment.isBlank()){
                errorLabel.setText("Please enter transfer description!");
            }else{

                JsonNode transactionNode = Json.nodeWithFields("id", 0, "from", fromNode, "to", toNode, "amount", amountSek, "description", comment, "date", null);
                Http.authPost("transactions", App.currentToken, transactionNode);
                MainController.accCtrl.updateAccountList();
                Alert alert = new Alert(AlertType.INFORMATION, "Transfer successful!");
                alert.setHeaderText("");                    Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK){
                    Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
                    oldStage.close();
                }
            }
        }
    }
    @FXML
    public void done(ActionEvent e) throws IOException {
        // Util.closeAndShow("AccountView", "Account Overview", e);
        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();
    }
}



