package crushers.controllers;

import crushers.WindowManager;
import crushers.api.ServerFacade;
import crushers.datamodels.Credentials;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label wrongLogIn;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button register;
    @FXML
    private HBox forgotPassword;

    @FXML
    public void UserLogin(ActionEvent event) throws Exception {
        try {
            Credentials credentials = new Credentials();
            credentials.setEmail(username.getText());
            credentials.setPassword(password.getText());
            ServerFacade.instance.loginUser(credentials);

            // throws if invalid credentials, thus only continues if valid:
            wrongLogIn.setText("Log in successfully!");
            WindowManager.showPage(WindowManager.Pages.Dashboard);
        }
        catch (Exception ex) {
            wrongLogIn.setText("Username/password wrong. Please try again.");
            ex.printStackTrace();
        }
    }

    @FXML
    public void register(ActionEvent event) throws IOException {
        WindowManager.showPage(WindowManager.Pages.Register);
    }

    @FXML
    public void onClickedForgotPassword(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        WindowManager.showPage(WindowManager.Pages.ForgotPassword);
    }

    @FXML
    public void onHoverForgot(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            HBox forgotPassword = (HBox) mouseEvent.getSource();
            forgotPassword.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHoverForgotExit(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            HBox forgotPassword = (HBox) mouseEvent.getSource();
            forgotPassword.setOpacity(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
