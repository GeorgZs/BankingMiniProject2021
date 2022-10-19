package crushers.controllers;

import crushers.WindowManager;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        }         
        catch (HttpException ex) {
            WindowManager.showAlert(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void cancelPayment(ActionEvent actionEvent) throws Exception {
        WindowManager.closeModal();
    }
}
