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
import java.util.Objects;


public class ChangePasswordController {
    @FXML
    private TextField emailField, firstAnswerField, secondAnswerField, thirdAnswerField;
    @FXML
    private PasswordField passwordField, repeatedPasswordField;
    @FXML
    private Label errorLabel;

    public void resetPassword(ActionEvent e) throws Exception {

        if (emailField.getText().isBlank()) {
             errorLabel.setText("Must enter a valid email address.");
         } else if (passwordField.getText().isBlank()) {
             errorLabel.setText("Must enter a new password.");
         } else if (passwordField.getText().isBlank()) {
             errorLabel.setText("Must repeat new password.");
         } else if (!passwordField.getText().matches(repeatedPasswordField.getText())) {
            errorLabel.setText("Passwords must match.");
         } else if (passwordField.getText().length() < 8) {
             errorLabel.setText("Password must be at least 8 characters.");
         } else if (firstAnswerField.getText().isBlank()) {
             errorLabel.setText("Please enter your answer to security question 1.");
         } else if (secondAnswerField.getText().isBlank()) {
             errorLabel.setText("Please enter your answer to security question 2.");
         } else if (thirdAnswerField.getText().isBlank()) {
             errorLabel.setText("Please enter your answer to security question 3.");
         } else {
            String email = emailField.getText();
            String password = passwordField.getText();
            String repeatedPassword = repeatedPasswordField.getText();
            if(!Objects.equals(password, repeatedPassword)){
                errorLabel.setText("Passwords don't match!");
            }
            String[] securityQuestions = {firstAnswerField.getText(), secondAnswerField.getText(), thirdAnswerField.getText()};
            JsonNode dataNode = Json.nodeWithFields("email", email, "password", password, "securityQuestions", securityQuestions);
            String res = Http.emptyPut("auth/password", dataNode);
            System.out.println(res);
         }
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
