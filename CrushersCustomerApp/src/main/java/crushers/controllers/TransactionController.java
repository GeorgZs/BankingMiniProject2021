package crushers.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import crushers.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

public class TransactionController implements Initializable{
    
    @FXML
    private ChoiceBox accountFromChoiceBox, accountToChoiceBox;
    @FXML
    private TextField transactionIdField;
    @FXML
    private TextArea transactionDescriptionTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(App.currentCustomer.getContactList());
    }

    public void sendTransaction(ActionEvent e){
        
    }


}
