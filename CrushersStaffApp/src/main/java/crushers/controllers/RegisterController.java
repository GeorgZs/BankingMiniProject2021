package crushers.controllers;

import crushers.WindowManager;
import crushers.common.ServerFacade;
import crushers.common.httpExceptions.HttpException;
import crushers.common.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField bankNameField;
    @FXML
    private TextField logoField;
    @FXML
    private TextArea bankDetails;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetAddressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button register;
    @FXML
    private Button cancel;

    @FXML
    private void cancelRegistration(ActionEvent event) throws IOException {
        WindowManager.showPage(WindowManager.Pages.Login);
    }

    @FXML
    public void registerUser(ActionEvent event) throws Exception {
        String bankName = bankNameField.getText();
        String details = bankDetails.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String streetAddress = streetAddressField.getText();
        String city = cityField.getText();
        String postalCode = postalCodeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if(bankName.isEmpty() || bankName.isBlank()){
            WindowManager.showAlert("Bank name cannot be empty");
            bankNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }else{
            bankNameField.setStyle("-fx-border-color: black");
        }
        if(firstName.isEmpty() || firstName.isBlank()) {
            WindowManager.showAlert("First name cannot be empty");
            firstNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            firstNameField.setStyle("-fx-border-color: black");
        }
        if(lastName.isEmpty() || lastName.isBlank()) {
            WindowManager.showAlert("Last name cannot be empty");
            lastNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            lastNameField.setStyle("-fx-border-color: black");
        }
        if(streetAddress.isEmpty() || streetAddress.isBlank()) {
            WindowManager.showAlert("Street address cannot be empty");
            streetAddressField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            streetAddressField.setStyle("-fx-border-color: black");
        }
        if(city.isEmpty() || city.isBlank()) {
            WindowManager.showAlert("City cannot be empty");
            cityField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            cityField.setStyle("-fx-border-color: black");
        }
        if(postalCode.isBlank() || postalCode.isEmpty()) {
            WindowManager.showAlert("Postal code cannot be empty");
            postalCodeField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            postalCodeField.setStyle("-fx-border-color: black");
        }
        if(email.isEmpty() || email.isBlank()) {
            WindowManager.showAlert("E-mail cannot be empty");
            emailField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }else if(!emailField.getText().contains("@")){
            emailField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            WindowManager.showAlert("Email must contain an @ sign");
        }
        else{
            emailField.setStyle("-fx-border-color: black");
        }
        if(password.isBlank() || password.isEmpty()) {
            WindowManager.showAlert("Password cannot be empty");
            passwordField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        else{
            passwordField.setStyle("-fx-border-color: black");
        }

        User manager = new User();
        manager.setFirstName(firstName);
        manager.setLastName(lastName);
        manager.setEmail(email);
        manager.setPassword(password);
        manager.setAddress(streetAddress);

        Bank bank = new Bank();
        bank.setName(bankName);
        bank.setDetails(details);
        bank.setManager(manager);

        try {
            ServerFacade.instance.createBank(bank);
            WindowManager.showPage(WindowManager.Pages.Login);
        } 
        catch (HttpException ex) {
            WindowManager.showAlert(ex.getError());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
