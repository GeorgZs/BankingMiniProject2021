package crushers.controllers;

import crushers.WindowManager;
import crushers.api.HttpError;
import crushers.api.ServerFacade;
import crushers.datamodels.BankAccount;
import crushers.datamodels.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InterestRateController {
    @FXML
    private Button update;
    @FXML
    private Button cancel;
    @FXML
    private TextField newInterest;

    @FXML
    void initialize() {
        newInterest.setStyle("-fx-font-family: SansSerif");
    }

    @FXML
    private void onPressedCancel(javafx.event.ActionEvent event) throws Exception {
        WindowManager.closeModal();
    }

    @FXML
    private void onPressedUpdate(javafx.event.ActionEvent event) throws Exception {
        try {
            ServerFacade.instance.updateBankInterestRate(Double.parseDouble(newInterest.getText()));
            System.out.println("Interest rate updated");
            WindowManager.closeModal();
        } catch (Exception e) {
            if(e instanceof HttpError) showAlert(((HttpError)e).getError());
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
