package crushers.controllers;

import crushers.App;
import crushers.model.Contact;
import crushers.model.PaymentAccount;
import crushers.model.Transaction;
import crushers.util.Http;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
@FXML
    private Button confirmPayment, cancel;
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
        toAccountBox.getItems().addAll(contacts);
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
            PaymentAccount accountTo = toAccountBox.getValue().getContactAccount();
            double amountSEK = Double.parseDouble(amountField.getText());
            String description = descriptionArea.getText();
            if (Double.parseDouble(amountField.getText()) > accountFrom.getBalance()) {
                errorLabel.setText("Insufficient funds!");
            } else {
                Transaction transaction = new Transaction(accountFrom, accountTo, amountSEK, description);
                accountFrom.withdraw(amountSEK);
                accountTo.deposit(amountSEK);
                accountFrom.addTransaction(transaction);
                accountTo.addTransaction(transaction);
                Http.authPost("/transactions", App.currentToken, transaction);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transfer successful!");
                alert.setHeaderText("");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Stage oldStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    oldStage.close();
                }
            }
        }
    }

    public void cancel(ActionEvent e) {
        Util.showModal("SystemView", "System view", e);
    }
}
