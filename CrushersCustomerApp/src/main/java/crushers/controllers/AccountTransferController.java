package crushers.controllers;

import crushers.App;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
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
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountTransferController implements Initializable {
    
    @FXML
    private Label transferFundsLabel, fromLabel, toLabel, amountLabel, sekLabel, commentLabel, errorLabel;

    @FXML
    private ChoiceBox<BankAccount> accountFromBox, accountToBox;

    @FXML
    private TextField amountField;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button transferFunds, done;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<BankAccount> userAccounts = App.currentCustomerAccounts;
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
            BankAccount accountFrom = accountFromBox.getValue();
            BankAccount accountTo = accountToBox.getValue();
            System.out.println(accountFrom);
            System.out.println(accountTo);

            Transaction transaction = new Transaction();
            transaction.setFrom(accountFrom);
            transaction.setAmount(Double.parseDouble(amountField.getText()));
            transaction.setTo(accountTo);

            if (!commentArea.getText().isBlank()) {
                transaction.setDescription(commentArea.getText());
            }

            try {
                transaction = ServerFacade.instance.createTransaction(transaction);
                MainController.accCtrl.updateAccountList();
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
                errorLabel.setText("Oops, something went wrong! Could not transfer money!");
                ex.printStackTrace();
            }
        }
    }
    @FXML
    public void done(ActionEvent e) throws IOException {
        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();
    }
}



