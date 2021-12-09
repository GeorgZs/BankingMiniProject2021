package crushers.controllers;

import crushers.model.PaymentAccount;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
@FXML
    private Button confirmPayment, cancel;
@FXML
    private Label makeAPayment, fromAccount, toAccount, amount, SEK, description;
@FXML
    private ChoiceBox<PaymentAccount> fromAccountBox, toAccountBox;
@FXML
    private TextField amountField;
@FXML
    private TextArea descriptionField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
