package crushers.gui;

import crushers.models.Clerk;
import crushers.models.ResetPassword;
import crushers.models.User;
import crushers.utils.HTTPUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLOutput;
import java.util.Collection;

public class ForgotPassword {
    @FXML
    private TextField emailAddress;
    @FXML
    private Button confirm;
    @FXML
    private Button cancel;
    @FXML
    private Button reset;
    @FXML
    private PasswordField newPassword;
    @FXML
    private TextField answerToQuestion;
    @FXML
    private Pane enterEmail;
    @FXML
    private Pane enterPassword;
    @FXML
    private Label emailError;
    @FXML
    private Label securityQuestion;

    @FXML
    public void onPressedContinue(javafx.event.ActionEvent actionEvent) throws Exception {
        Clerk clerk = new Clerk(emailAddress.getText(), null, null, null, null, null, null);
        Collection<crushers.models.Clerk> clerkList = HTTPUtils.getCollection("/staff", Clerk.class);
        int count = 0;
        for (Clerk clerk1: clerkList) {
            System.out.println(clerk1.getEmail());
            if(clerk.getEmail().equals(clerk1.getEmail())) {
                enterPassword.setVisible(true);
                break;
            } else {
                count++;
            }
        }
        if(count == clerkList.size()) {
            emailError.setVisible(true);
            emailAddress.setStyle("-fx-border-color: red; -fx-border-width: 1px");
        }
        //Collection<Clerk> clerks = HTTPUtils.getCollection("/staff", clerk);

    }

    @FXML
    public void onPressedCancel(javafx.event.ActionEvent actionEvent) throws Exception {
        MainController main = new MainController();
        main.changeScene("Login.fxml", actionEvent);
    }

    @FXML
    public void onPressedReset(javafx.event.ActionEvent actionEvent) throws Exception {
        Clerk clerk = HTTPUtils.get("/banks/" + emailAddress.getText(), Clerk.class);
        String[] securityQuestion1 = clerk.getSecurityQuestions();
        securityQuestion.setText(securityQuestion1[0]);
    }
      //String[] questions = new String[10];
      //questions[0]
      //ResetPassword reset = new ResetPassword(emailAddress.getText(), newPassword.getText(), answerToQuestion());

   // }
}
