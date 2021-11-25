package crushers.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountCreationController implements Initializable {

    @FXML
    private ChoiceBox bankSelection;

    @FXML
    private RadioButton paymentOption;

    @FXML
    private RadioButton savingsOption;

    @FXML
    private TextArea accountDescription;

    @FXML
    private TextField savingsGoalField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bankSelection.setStyle("-fx-font-family: SansSerif");
    }




}
