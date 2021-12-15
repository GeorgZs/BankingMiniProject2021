package crushers.controllers;

import crushers.WindowManager;
import crushers.api.HttpError;
import crushers.api.ServerFacade;
import crushers.datamodels.BankAccount;
import crushers.datamodels.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PaymentController {
    @FXML
    private TextField customerID;
    @FXML
    private Button create;
    @FXML
    private Button cancel;

    @FXML
    private void registerPayment(ActionEvent actionEvent) throws Exception {
        BankAccount payment = new BankAccount();
        payment.setType("payment");

        User owner = new User();
        owner.setId(Integer.parseInt(customerID.getText()));
        payment.setOwner(owner);

        try {
            BankAccount createdAccount = ServerFacade.instance.createBankAccount(payment);
            System.out.println("BANK ACCOUNT: " + createdAccount.getNumber());

            WindowManager.closeModal();
        } catch (Exception e) {
            if(e instanceof HttpError) showAlert(((HttpError)e).getError());
            e.printStackTrace();
        }

    }

    @FXML
    private void cancelPayment(ActionEvent actionEvent) throws Exception {
        WindowManager.closeModal();
    }

    private void showAlert(String message){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Warning");
        a.setHeaderText(message);
        a.getDialogPane().setStyle("-fx-font-family: SansSerif");
        a.show();
    }

}
