package crushers.controllers;

import crushers.WindowManager;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import javafx.fxml.FXML;
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
            InterestRate interestRate = new InterestRate();
            interestRate.setRate(Double.parseDouble(newInterest.getText()) / 100);
            ServerFacade.instance.updateBankInterestRate(interestRate);
            System.out.println("Interest rate updated");
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
