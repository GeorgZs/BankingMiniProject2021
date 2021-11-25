package crushers.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import crushers.App;
import crushers.model.Bank;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class AccountCreationController implements Initializable{

    @FXML
    private ChoiceBox<Bank> bankSelection;

    @FXML
    private RadioButton paymentOption;

    @FXML
    private RadioButton savingsOption;

    @FXML
    private TextField accountDescriptionField;

    @FXML
    private TextField savingsGoalField;
    @FXML
    private Label savingsGoalLabel;
    @FXML
    private Label sekLabel;

    @FXML
    private Button createAccountButton;

    public void createAccount(ActionEvent e){
        
    }

    public void setGoalVisible(ActionEvent e){
        savingsGoalLabel.setVisible(true);
        savingsGoalField.setVisible(true);
        sekLabel.setVisible(true);
    }
    public void setGoalInvisible(ActionEvent e){
        savingsGoalLabel.setVisible(false);
        savingsGoalField.setVisible(false);
        sekLabel.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Bank bank: App.banks){
            bankSelection.getItems().add(bank);
        }
        savingsGoalLabel.setVisible(false);
        savingsGoalField.setVisible(false);
        sekLabel.setVisible(false);
    }
}
