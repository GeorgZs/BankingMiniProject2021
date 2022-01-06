package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import crushers.App;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import crushers.util.Util;
import javafx.event.ActionEvent;

import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

public class RecurringController implements Initializable{
    
    @FXML
    private ChoiceBox<BankAccount> fromAccountBox;
    @FXML
    private ChoiceBox<Contact> toAccountBox;
    @FXML
    private TextField amountField, intervalField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label errorLabel;

    public void confirmPayment(ActionEvent e) throws IOException, InterruptedException{
        if (fromAccountBox.getValue() == null) {
            errorLabel.setText("Must select an account to transfer funds from!");
        } else if (toAccountBox.getValue() == null) {
            errorLabel.setText("Must select an account to transfer funds to!");
        } else if (amountField.getText() == null) {
            errorLabel.setText("Must enter a payment amount!");
        } else if (!amountField.getText().matches("^[0-9]+$")) {
            errorLabel.setText("Please enter a valid amount");
        } else if(!intervalField.getText().matches("^[0-9]+$")){
            errorLabel.setText("Please enter a valid interval");
        } else {
            
            BankAccount accountFrom = fromAccountBox.getValue();
            BankAccount accountTo = toAccountBox.getValue().getAccount();
            System.out.println(accountFrom);
            System.out.println(accountTo);

            Transaction transaction = new Transaction();
            transaction.setFrom(accountFrom);
            transaction.setAmount(Double.parseDouble(amountField.getText()));
            transaction.setTo(accountTo);
            transaction.setRecurring(true);
            transaction.setInterval(Integer.parseInt(intervalField.getText()));

            if (!descriptionArea.getText().isBlank()) {
                transaction.setDescription(descriptionArea.getText());
            }

            try {
                transaction = ServerFacade.instance.createRecurringTransaction(transaction);
                AccountController.sysCtrl.addTransactionToTable(transaction);
                Util.updateCustomer();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transfer successful!");
                alert.getDialogPane().setStyle("-fx-font-family: SansSerif");
                alert.setHeaderText("");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Stage oldStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    oldStage.close();
                }
            }
            catch (HttpException ex) {
                errorLabel.setText(ex.getError());
                System.out.println(ex.getError());
            }
            catch (Exception ex) {
                errorLabel.setText("Oops, something went wrong! Could not start recurring payment!");
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<BankAccount> userAccounts = App.currentCustomerAccounts;
        List<Contact> contacts = App.currentCustomerContacts;
        fromAccountBox.getItems().addAll(userAccounts);
        toAccountBox.getItems().addAll(contacts);
    }
}
