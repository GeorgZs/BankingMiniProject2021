package crushers.controllers;

import crushers.App;
import crushers.model.Bank;
import crushers.model.Customer;
import crushers.model.PaymentAccount;
import crushers.model.Transaction;
import crushers.util.Http;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountTransferController implements Initializable {
    private Customer currentCustomer = App.currentCustomer;

    private Stage stage;
    private Parent root;



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
           PaymentAccount paymentAccountFrom = accountFromBox.getValue();
           PaymentAccount paymentAccountTo =  accountToBox.getValue();
           double amountSek = Double.parseDouble(amountField.getText());
           String comment = commentArea.getText();
           if (Double.parseDouble(amountField.getText()) > paymentAccountFrom.getBalance()) {
               errorLabel.setText("Insufficient funds!");
           } else {
                
                // errorLabel.setStyle("-fx-text-fill: green");
                // errorLabel.setText("Transfer successful!");
                // Http.authPost("transactions",App.currentToken ,transaction);

                Alert alert = new Alert(AlertType.INFORMATION, "Transfer successful!");
                alert.setHeaderText("");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK){
                    Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
                    oldStage.close();
                }
                // add custom alerts to util
            //    System.out.println(Http.post("transactions", transaction));
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



