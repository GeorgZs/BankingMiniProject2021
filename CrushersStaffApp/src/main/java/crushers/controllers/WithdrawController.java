package crushers.controllers;

import crushers.WindowManager;
import crushers.api.HttpError;
import crushers.api.ServerFacade;
import crushers.datamodels.BankAccount;
import crushers.datamodels.Transaction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WithdrawController {
    @FXML
    private TextField amount;
    @FXML
    private ChoiceBox<BankAccount> accountFrom;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker dateOnWithdraw;
    @FXML
    private Button withdrawButton;
    @FXML
    private Button cancelButton;


    @FXML
    void initialize(){
        List<BankAccount> accounts = null;
        try {
            accounts = ServerFacade.instance.listAllBankAccountsAtThisBank();
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountFrom.setItems(FXCollections.observableArrayList(accounts));
        accountFrom.setStyle("-fx-font-family: SansSerif");
    }

    @FXML
    public void onPressedCancel(javafx.event.ActionEvent actionEvent) throws Exception {
        WindowManager.closeModal();

    }

    @FXML
    public void onPressedWithdraw(javafx.event.ActionEvent actionEvent) throws Exception {
        Transaction withdraw = new Transaction();
        accountFrom.getValue().setType("payment");
        withdraw.setTo(accountFrom.getValue());
        withdraw.setAmount(Double.parseDouble(amount.getText())*-1);
        withdraw.setDescription(description.getText());
        //deposit.setDate(dateOnDeposit.get);

        try {
            ServerFacade.instance.withdraw(withdraw);
            System.out.println("Withdrew: " + amount.getText());
            WindowManager.closeModal();
        } catch (Exception e) {
            if (e instanceof HttpError) showAlert(((HttpError)e).getError());
            e.printStackTrace();
        }

    }

    private void showAlert(String message){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Warning");
        a.setHeaderText(message);
        a.getDialogPane().setStyle("-fx-font-family: SansSerif");
        a.show();
    }
}
