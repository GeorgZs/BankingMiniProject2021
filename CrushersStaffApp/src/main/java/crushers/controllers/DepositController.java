package crushers.controllers;

import crushers.WindowManager;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class DepositController {
    @FXML
    private TextField amount;
    @FXML
    private ChoiceBox<BankAccount> accountsTo;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker dateOnDeposit;
    @FXML
    private Button depositButton;
    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        List<BankAccount> accounts = null;
        try {
            accounts = ServerFacade.instance.listAllBankAccountsAtThisBank();
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountsTo.setItems(FXCollections.observableArrayList(accounts));
        accountsTo.setStyle("-fx-font-family: SansSerif");
        dateOnDeposit.setStyle("-fx-font-family: SansSerif");
    }


    @FXML
    private void onPressedDeposit(javafx.event.ActionEvent actionEvent) throws Exception {
        Transaction deposit = new Transaction();
        accountsTo.getValue().setType("payment");
        deposit.setTo(accountsTo.getValue());
        deposit.setAmount(Double.parseDouble(amount.getText()));
        deposit.setDescription(description.getText());
        deposit.setDate(dateOnDeposit.getValue());

        try {
            ServerFacade.instance.createTransaction(deposit);
            System.out.println("Deposited: " + amount.getText());
            WindowManager.closeModal();
        } 
        catch (HttpException ex) {
            WindowManager.showAlert(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onPressedCancel(javafx.event.ActionEvent actionEvent) throws Exception {
        WindowManager.closeModal();
    }
}
