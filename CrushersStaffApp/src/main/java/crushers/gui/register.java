package crushers.gui;

import crushers.models.Bank;
import crushers.models.Manager;
import crushers.utils.HTTPUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class register {

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
    private TextField passwordField;
    @FXML
    private ChoiceBox securityQuestions;
    @FXML
    private TextField answerQuestions;
    @FXML
    private Button register;
    @FXML
    private Button cancel;

    private String[] questions = {
        "What's the name of your first pet?",
        "What's the name of your home-town?",
        "What's your favorite movie?",
        "Which high-school did you graduate?",
        "What's your mother's maiden name?",
        "What's the name of your first school?",
        "What was your favorite food as a child?",
        "What's your favorite book?"

    };

    @FXML
    void initialize(){
        securityQuestions.setItems(FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList(questions))));
        securityQuestions.setStyle("-fx-font-family: SansSerif");
    }

    @FXML
    private void cancelRegistration(ActionEvent event) throws IOException {
        MainController h = new MainController();
        h.changeScene("Login.fxml", event);
    }

    @FXML
    public void registerUser(ActionEvent event) throws Exception {
        MainController m = new MainController();
        String bankName = bankNameField.getText();
        String logo = logoField.getText();
        String details = bankDetails.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String streetAddress = streetAddressField.getText();
        String city = cityField.getText();
        String postalCode = postalCodeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String question = securityQuestions.getValue().toString();
        //String answer = answerQuestions.getText();

        if(bankName.isEmpty() || bankName.isBlank()){
            showAlert("Bank name cannot be empty");
            bankNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            //bankNameField.setStyle("-fx-text-fill: red");
        }else{
            bankNameField.setStyle("-fx-border-color: black");
        }
        if(logo.isEmpty() || logo.isBlank()) {
            showAlert("logo cannot be empty");
            logoField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            logoField.setStyle("-fx-border-color: black");
        }
        if(details.isEmpty() || details.isBlank()){
            showAlert("Details cannot be empty");
            bankDetails.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            bankDetails.setStyle("-fx-border-color: black");
        }
        if(firstName.isEmpty() || firstName.isBlank()) {
            showAlert("First name cannot be empty");
            firstNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            firstNameField.setStyle("-fx-border-color: black");
        }
        if(lastName.isEmpty() || lastName.isBlank()) {
            showAlert("Last name cannot be empty");
            lastNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            lastNameField.setStyle("-fx-border-color: black");
        }
        if(streetAddress.isEmpty() || streetAddress.isBlank()) {
            showAlert("Street address cannot be empty");
            streetAddressField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            streetAddressField.setStyle("-fx-border-color: black");
        }
        if(city.isEmpty() || city.isBlank()) {
            showAlert("City cannot be empty");
            cityField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            cityField.setStyle("-fx-border-color: black");
        }
        if(postalCode.isBlank() || postalCode.isEmpty()) {
            showAlert("Postal code cannot be empty");
            postalCodeField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else{
            postalCodeField.setStyle("-fx-border-color: black");
        }
        if(email.isEmpty() || email.isBlank()) {
            showAlert("E-mail cannot be empty");
            emailField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }else if(!emailField.getText().contains("@")){
            emailField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            showAlert("Email must contain an @ sign");
        }
        else{
            emailField.setStyle("-fx-border-color: black");
        }
        if(password.isBlank() || password.isEmpty()) {
            showAlert("Password cannot be empty");
            passwordField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        else{
            passwordField.setStyle("-fx-border-color: black");
        }

        //resetAllBorders("-fx-border-color: black ; -fx-border-width: 1px ;");
        Bank bank = new Bank(new Manager(firstName, lastName,  streetAddress,  email,  password, null, null));
        bank.setName(bankName);
        bank.setDetails(details);
        bank.setLogo(logo);
        HTTPUtils.post("/banks", bank, Bank.class);
    }

    private void showAlert(String message){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Warning");
        a.setHeaderText(message);
        a.getDialogPane().setStyle("-fx-font-family: SansSerif");
        a.show();
    }

    /*
    private void resetAllBorders(String message) {
        bankNameField.setStyle(message);
        logoField.setStyle(message);
        bankDetails.setStyle(message);
        firstNameField.setStyle(message);
        lastNameField.setStyle(message);
        streetAddressField.setStyle(message);
        cityField.setStyle(message);
        postalCodeField.setStyle(message);
        emailField.setStyle(message);
        passwordField.setStyle(message);

    }
      */

}
