package crushers.gui;

import crushers.utils.Util;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;

public class WithdrawController {
    @FXML
    private TextField amount;
    @FXML
    private ChoiceBox<String> accountFrom;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker dateOnWithdraw;
    @FXML
    private Button withdrawButton;
    @FXML
    private Button cancelButton;

    //Testing choicebox
    private String[] accounts = {
            "Account 1",
            "Account 2",
            "Account 3",
            "Account 4"
    };

    @FXML
    void initialize(){
        accountFrom.setItems(FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList(accounts))));
        accountFrom.setStyle("-fx-font-family: SansSerif");
    }

    @FXML
    public void onPressedCancel(javafx.event.ActionEvent actionEvent) throws Exception {}

    @FXML
    public void onPressedWithdraw(javafx.event.ActionEvent actionEvent) throws Exception {}

}
