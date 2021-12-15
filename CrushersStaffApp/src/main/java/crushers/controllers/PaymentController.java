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
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField customerID;
    @FXML
    private TextField bank;
    @FXML
    private TextField accountNumber;
    @FXML
    private TextField balance;
    @FXML
    private Button create;
    @FXML
    private Button cancel;

    @FXML
    private void registerPayment(ActionEvent actionEvent) throws Exception {
        /*if(firstName.getText().isEmpty() || firstName.getText().isBlank()) {
            firstName.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            firstName.setStyle("-fx-border-color: transparent");
        }
        if (lastName.getText().isEmpty() || lastName.getText().isBlank()) {
            lastName.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            lastName.setStyle("-fx-border-color: transparent");
        }
        if (customerID.getText().isEmpty() || customerID.getText().isBlank()) {
            customerID.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            customerID.setStyle("-fx-border-color: transparent");
        }
        if (bank.getText().isEmpty() || bank.getText().isBlank()) {
            bank.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            bank.setStyle("-fx-border-color: transparent");
        }

         */
        //accountNumber.setText(String.valueOf(Math.random()));
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
