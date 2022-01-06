package crushers.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.stage.Stage;

import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;

public class RegisterController {
    @FXML
    private Button registerButton;
    @FXML
    private Label invalidInputLabel;
    @FXML
    private TextField firstNameField, lastNameField, addressField, emailField;
    @FXML
    private PasswordField passwordField, repeatedPasswordField;

    public void register(ActionEvent e) throws IOException{
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String repeatedPassword = repeatedPasswordField.getText();

        if(firstName.isBlank()){
            invalidInputLabel.setText("First name cannot be empty!");
        }else if(lastName.isBlank()){
            invalidInputLabel.setText("Last name cannot be empty!");
        }else if(address.isBlank()){
            invalidInputLabel.setText("Address cannot be empty!");
        }else if(!email.contains("@")){
            invalidInputLabel.setText("Invalid E-Mail!");
        }else if(password.length() < 8){
            invalidInputLabel.setText("Password must be at least 8 characters");
        }else if(!password.equals(repeatedPassword)){
            invalidInputLabel.setText("Passwords don't match!");
        }else{
            User customer = new User();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPassword(password);

            try {
                ServerFacade.instance.createCustomer(customer);
                Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
                oldStage.close();
            }
            catch (HttpException ex) {
                invalidInputLabel.setText(ex.getError());
                System.out.println(ex.getError());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
