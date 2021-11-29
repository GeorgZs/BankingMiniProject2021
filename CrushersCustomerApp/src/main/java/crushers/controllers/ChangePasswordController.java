package crushers.controllers;

import crushers.App;
import crushers.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;


public class ChangePasswordController {
    private Customer currentCustomer;
    @FXML
    private TextField answer1Field, answer2Field, answer3Field, emailField, newPasswordField;
    @FXML
    private Label question1Label,question2Label, question3Label;
    @FXML
    private VBox questionsBox, passwordBox;


    public void submitAnswers(ActionEvent event) {
        if(currentCustomer.getSecurityQuestions().get(1).equals(answer1Field.getText())) {
            if(currentCustomer.getSecurityQuestions().get(3).equals(answer2Field.getText())) {
                if(currentCustomer.getSecurityQuestions().get(5).equals(answer3Field.getText())) {
                    passwordBox.setVisible(true);
                    return;
                }
            }
        }
            System.out.println("Incorrect answers.");
    }
    public void submitEmail(ActionEvent event) {
        String email = emailField.getText();
        ArrayList<Customer> customers = App.crushersBank.getCustomers();
        for(Customer customer : customers) {
            if(email.equals(customer.getEmail())) {
                currentCustomer = customer;
                questionsBox.setVisible(true);
                question1Label.setText(customer.getSecurityQuestions().get(0));
                question2Label.setText(customer.getSecurityQuestions().get(2));
                question3Label.setText(customer.getSecurityQuestions().get(4));
                return;
            }
        }
        System.out.println("Email not found.");
    }
    public void changePassword(ActionEvent event) {
        String newPassword = newPasswordField.getText();
        currentCustomer.setPassword(newPassword);
        System.out.println("New password has been saved.");
    }

}
