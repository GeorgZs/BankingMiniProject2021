package crushers.controllers;

import crushers.WindowManager;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        } 
        catch (Exception ex) {
            ex.printStackTrace();
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
        withdraw.setTo(accountFrom.getValue());
        withdraw.setAmount(Double.parseDouble(amount.getText())*-1);
        withdraw.setDescription(description.getText());
        withdraw.setDate(dateOnWithdraw.getValue());

        try {
            ServerFacade.instance.createTransaction(withdraw);
            System.out.println("Withdrew: " + amount.getText());
            WindowManager.closeModal();
        } 
        catch (HttpException ex) {
            WindowManager.showAlert(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
