package crushers.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.awt.*;

public class AccountTransferController {
    @FXML
    private Label transferFundsLabel, fromLabel, toLabel, amountLabel, sekLabel, commentLabel;

    @FXML
    private ChoiceBox accountFrom, accountTo;

    @FXML
    private TextField amountField;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button transfer, cancel;


}
