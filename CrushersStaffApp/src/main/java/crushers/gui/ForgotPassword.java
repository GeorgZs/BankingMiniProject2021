package crushers.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;

public class ForgotPassword {
    @FXML
    private TextField emailAddress;
    @FXML
    private Button confirm;
    @FXML
    private Button cancel;
    @FXML
    private PasswordField newPassword;
    @FXML
    private TextField answerToQuestion;
    @FXML
    private Pane enterEmail;
    @FXML
    private Pane enterPassword;

    @FXML
    public void onPressedContinue(javafx.event.ActionEvent actionEvent) throws Exception {
        enterPassword.setVisible(true);
    }

    @FXML
    public void onPressedCancel(javafx.event.ActionEvent actionEvent) throws Exception {
        MainController main = new MainController();
        main.changeScene("Login.fxml", actionEvent);

    }
}
