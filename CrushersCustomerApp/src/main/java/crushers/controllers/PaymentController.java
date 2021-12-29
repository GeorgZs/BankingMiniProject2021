package crushers.controllers;

import crushers.App;
import crushers.model.Contact;
import crushers.model.PaymentAccount;
import crushers.model.Transaction;
import crushers.util.Http;
import crushers.util.Json;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
    @FXML
    private Button cancel;
    @FXML
    private Label makeAPayment, fromAccount, toAccount, amount, SEK, description, errorLabel;
    @FXML
    private ChoiceBox<PaymentAccount> fromAccountBox;
    @FXML
    private ChoiceBox<Contact> toAccountBox;
    @FXML
    private TextField amountField;
    @FXML
    private TextArea descriptionArea;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<PaymentAccount> userAccounts = App.currentCustomer.getAccountList();
        ArrayList<Contact> contacts = App.currentCustomer.getContactList();
        fromAccountBox.getItems().addAll(userAccounts);
        if(contacts != null){
            toAccountBox.getItems().addAll(contacts);
        }
    }

    public void confirmPayment(ActionEvent e) throws IOException, InterruptedException {
        if (fromAccountBox.getValue() == null) {
            errorLabel.setText("Must select an account to transfer funds from!");
        } else if (toAccountBox.getValue() == null) {
            errorLabel.setText("Must select an account to transfer funds to!");
        } else if (amountField.getText() == null) {
            errorLabel.setText("Must enter a payment amount!");
        } else if (!amountField.getText().matches("^[0-9]+$")) {
            errorLabel.setText("Please enter a valid numeric amount.");
        } else {
            
            PaymentAccount accountFrom = fromAccountBox.getValue();
            PaymentAccount accountTo = toAccountBox.getValue().getAccount();
            System.out.println(accountFrom);
            System.out.println(accountTo);
            double amountSEK;
            try{
                amountSEK = Double.parseDouble(amountField.getText());
            }catch(NumberFormatException nfe){
                amountSEK = 0;
            }
            String description = descriptionArea.getText();
            if (amountSEK > accountFrom.getBalance()) {
                errorLabel.setText("Insufficient funds!");
            } else if(amountSEK == 0){
                errorLabel.setText("Please enter sufficient funds!");
            } else {

                JsonNode toNode = Json.nodeWithFields("id", accountTo.getId(), "type", "payment");
                JsonNode fromNode = Json.nodeWithFields("id", accountFrom.getId(), "type", "payment");

                JsonNode transactionNode = Json.nodeWithFields("id", 0, "from", fromNode, "to", toNode, "amount", amountSEK, "description", description, "date", null);

                try{
                    Transaction transaction = Json.parse(Http.authPost("transactions", App.currentToken, transactionNode), Transaction.class);
                    System.out.println(transaction);
                    if(transaction.toString().contains("null Transaction")){
                        errorLabel.setText("Account does not exist!");
                        return;
                    }
                    AccountController.sysCtrl.addTransactionToTable(transaction);
                    Util.updateCustomer();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transfer successful!");
                    alert.setHeaderText("");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        Stage oldStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        oldStage.close();
                    }
                }catch(JsonParseException jpe){ // error response
                    errorLabel.setText("Account does not exist!");
                }

            }
        }
    }

    public void cancel(ActionEvent e) {
        Stage oldStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        oldStage.close();
    }
}
