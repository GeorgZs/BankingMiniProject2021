package crushers.controllers;

import crushers.WindowManager;
import crushers.api.Http;
import crushers.models.Clerk;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collection;

public class ForgotPasswordController {
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
        Collection<crushers.models.Clerk> clerkList = new ArrayList<>(); // Http.getCollection("/staff", Clerk.class);
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
        WindowManager.showPage(WindowManager.Pages.Login);
    }

    @FXML
    public void onPressedReset(javafx.event.ActionEvent actionEvent) throws Exception {
        // Clerk clerk = Http.get("/banks/" + emailAddress.getText(), Clerk.class);
        // String[] securityQuestion1 = clerk.getSecurityQuestions();
        // securityQuestion.setText(securityQuestion1[0]);
    }
      //String[] questions = new String[10];
      //questions[0]
      //ResetPassword reset = new ResetPassword(emailAddress.getText(), newPassword.getText(), answerToQuestion());

   // }
}
