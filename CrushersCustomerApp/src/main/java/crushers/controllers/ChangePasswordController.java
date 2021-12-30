package crushers.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import crushers.App;
import crushers.model.Customer;
import crushers.util.Http;
import crushers.util.Json;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class ChangePasswordController {
    @FXML
    private TextField emailField, firstAnswerField, secondAnswerField, thirdAnswerField;
    @FXML
    private PasswordField passwordField, repeatedPasswordField;
    @FXML
    private Label errorLabel;

    public void resetPassword(ActionEvent e) throws Exception {

        String email = emailField.getText();
        String password = passwordField.getText();
        String repeatedPassword = repeatedPasswordField.getText();
        if(password != repeatedPassword){
            errorLabel.setText("Passwords don't match!");
        }
        ArrayList<String> securityQuestions = new ArrayList<>(List.of(firstAnswerField.getText(), secondAnswerField.getText(), thirdAnswerField.getText()));
        JsonNode dataNode = Json.nodeWithFields("email", emailField.getText(), "password", password, "securityQuestions", securityQuestions);
        String res = Http.emptyPut("auth/password", dataNode);
        System.out.println(res);
        // if (emailField.getText().isBlank()) {
        //     errorLabel.setText("Must enter a valid email address.");
        // } else if (newPass.getText().isBlank()) {
        //     errorLabel.setText("Must enter a new password.");
        // } else if (repPass.getText().isBlank()) {
        //     errorLabel.setText("Must repeat new password.");
        // } else if (!newPass.getText().matches(repPass.getText())) {
        //     errorLabel.setText("Passwords must match.");
        // } else if (newPass.getText().length() < 8) {
        //     errorLabel.setText("Password must be at least 8 characters.");
        // } else if (ans1Field.getText().isBlank()) {
        //     errorLabel.setText("Please enter your answer to security question 1.");
        // } else if (ans2Field.getText().isBlank()) {
        //     errorLabel.setText("Please enter your answer to security question 2.");
        // } else if (ans3Field.getText().isBlank()) {
        //     errorLabel.setText("Please enter your answer to security question 3.");
        // } else {
        //     String email = emailField.getText();
        //     String password = newPass.getText();
        //     String[] securityQuestions = {ans1Field.getText(), ans2Field.getText(), ans3Field.getText()};
        //     JsonNode resetPassNode = Json.nodeWithFields("email",email,"password",password,"securityQuestions",securityQuestions);
        //     Http.put("auth/password",resetPassNode,null);
        // }
    }










/*
    public void submitAnswers(ActionEvent event) {
        if(currentCustomer.getSecurityQuestions().get(1).equals(answer1Field.getText())) {
            if(currentCustomer.getSecurityQuestions().get(3).equals(answer2Field.getText())) {
                if(currentCustomer.getSecurityQuestions().get(5).equals(answer3Field.getText())) {
                    passwordBox.setVisible(true);
                    return;
                }
            }
        }
        invalidInput.setText("Incorrect answers.");
    }
    public void submitEmail(ActionEvent event) {
        // String email = emailField.getText();
        // ArrayList<Customer> customers = App.crushersBank.getCustomers();
        // for(Customer customer : customers) {
        //     if(email.equals(customer.getEmail())) {
        //         currentCustomer = customer;
        //         questionsBox.setVisible(true);
        //         question1Label.setText(customer.getSecurityQuestions().get(0));
        //         question2Label.setText(customer.getSecurityQuestions().get(2));
        //         question3Label.setText(customer.getSecurityQuestions().get(4));
        //         return;
        //     }
        // }
        // invalidInput.setText("Email not found.");
        
        // send get request to auth/login and see if email exists
    }
    public void changePassword(ActionEvent event) {
        String newPassword = newPasswordField.getText();
        String passwordConfirm = passwordConfirmField.getText();
        if (newPassword.trim().isEmpty()) {
            invalidInput.setText("Password can not be empty");
        }
        else if (newPassword.length() < 8) {
            invalidInput.setText("Password can not be shorter than 6 characters.");
        }
        else if (!passwordConfirm.equals(newPassword)) {
            invalidInput.setText("Passwords do not match.");
        } else {
            currentCustomer.setPassword(newPassword);
            //Json.nodeWithFields("email",)
            //Http.put("auth/password",)
            System.out.println("New password has been saved.");
            Stage oldStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            oldStage.close();
        }
    }
 */
}
