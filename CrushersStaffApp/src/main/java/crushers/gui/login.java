package crushers.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class login {

    @FXML
    private Label wrongLogIn;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button register;

    @FXML
    public void UserLogin(ActionEvent event) throws IOException{
        MainController m = new MainController();
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            wrongLogIn.setText("Username/password wrong. Please try again.");
        } else if (!username.getText().equals("Ivan") || !password.getText().equals("1234")) {
            wrongLogIn.setText("Username/password wrong. Please try again.");

        }


        else if(username.getText().equals("Ivan") && password.getText().equals("1234")) {
            wrongLogIn.setText("Log in successfully!");
            m.changeScene("dashboard.fxml", event);
        }

        else {
            wrongLogIn.setText("Wrong username or password. Please try again.");
        }
    }

    @FXML
    public void register(ActionEvent event) throws IOException {
        MainController m = new MainController();
        m.changeScene("register.fxml", event);
    }

}
