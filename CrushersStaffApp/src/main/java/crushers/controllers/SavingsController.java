package crushers.controllers;

import crushers.WindowManager;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SavingsController {
    @FXML
    private TextField name;
    @FXML
    private TextField customerID;
    @FXML
    private Button create;
    @FXML
    private Button cancel;

    @FXML
    private void registerSavings(ActionEvent actionEvent) throws Exception {
        /*if (firstName.getText().isEmpty() || firstName.getText().isBlank()) {
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
        BankAccount savings = new BankAccount();
        savings.setType("savings");

        User owner = new User();
        owner.setId(Integer.parseInt(customerID.getText()));
        savings.setOwner(owner);



        try {
            BankAccount createdAccount = ServerFacade.instance.createBankAccount(savings);
            System.out.println("SAVINGS ACCOUNT: " + createdAccount.getNumber());
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
    private void cancelSavings(ActionEvent actionEvent) throws Exception {
        WindowManager.closeModal();
    }
}
