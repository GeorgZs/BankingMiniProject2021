package crushers.controllers;

import crushers.model.PaymentAccount;
import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
@FXML
    private Button confirmPayment, cancel;
@FXML
    private Label makeAPayment, fromAccount, toAccount, amount, SEK, description, errorLabel;
@FXML
    private ChoiceBox<PaymentAccount> fromAccountBox, toAccountBox;
@FXML
    private TextField amountField;
@FXML
    private TextArea descriptionArea;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void confirmPayment(ActionEvent e) throws IOException {

    }

    public void cancel(ActionEvent e) {
        Util.showModal("SystemView", "System view", e);
    }
}
