package crushers.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import crushers.App;
import crushers.model.Customer;
import crushers.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.stage.Stage;

public class RegisterController implements Initializable {

    @FXML
    private Button registerButton;
    @FXML
    private Label invalidInputLabel;
    @FXML
    private ChoiceBox<String> firstQuestionBox;
    @FXML
    private ChoiceBox<String> secondQuestionBox;
    @FXML
    private ChoiceBox<String> thirdQuestionBox;
    @FXML
    private TextField firstAnswerField;
    @FXML
    private TextField secondAnswerField;
    @FXML
    private TextField thirdAnswerField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatedPasswordField;



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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        firstQuestionBox.setStyle("-fx-font-family: SansSerif");
        secondQuestionBox.setStyle("-fx-font-family: SansSerif");
        thirdQuestionBox.setStyle("-fx-font-family: SansSerif");
        firstQuestionBox.getItems().addAll(questions);
        secondQuestionBox.getItems().addAll(questions);
        thirdQuestionBox.getItems().addAll(questions);

        firstQuestionBox.setOnAction(this::firstSelection);
        secondQuestionBox.setOnAction(this::secondSelection);
    }

    public void firstSelection(ActionEvent e){
        String firstQuestion = firstQuestionBox.getValue();
        secondQuestionBox.getItems().remove(firstQuestion);
        thirdQuestionBox.getItems().remove(firstQuestion);
    }
    public void secondSelection(ActionEvent e){
        String secondQuestion = secondQuestionBox.getValue();
        thirdQuestionBox.getItems().remove(secondQuestion);
    }

    public void register(ActionEvent e) throws IOException{
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String repeatedPassword = repeatedPasswordField.getText();
        String firstAnswer = firstAnswerField.getText();
        String secondAnswer = secondAnswerField.getText();
        String thirdAnswer = thirdAnswerField.getText();

        if(firstName.isBlank()){
            invalidInputLabel.setText("First name cannot be empty!");
        }else if(lastName.isBlank()){
            invalidInputLabel.setText("Last name cannot be empty!");
        }else if(address.isBlank()){
            invalidInputLabel.setText("Address cannot be empty!");
        }else if(!email.contains("@")){
            invalidInputLabel.setText("Invalid E-Mail!");
        }else if(password.length() < 6){
            invalidInputLabel.setText("Password must be at least 6 characters");
        }else if(!password.equals(repeatedPassword)){
            invalidInputLabel.setText("Passwords don't match!");
        }else if(firstAnswer.isBlank() || secondAnswer.isBlank() || thirdAnswer.isBlank()){
            invalidInputLabel.setText("You must answer all security questions!");
        }else{
            int userID = (int) Math.floor(Math.random()*10000000);
            ArrayList<String> securityQuestionsAnswers = new ArrayList<String>();
            securityQuestionsAnswers.add(firstQuestionBox.getValue());
            securityQuestionsAnswers.add(firstAnswer);
            securityQuestionsAnswers.add(secondQuestionBox.getValue());
            securityQuestionsAnswers.add(secondAnswer);
            securityQuestionsAnswers.add(thirdQuestionBox.getValue());
            securityQuestionsAnswers.add(thirdAnswer);

            User registeredUser = new User(firstName, lastName, address, email, password, securityQuestionsAnswers, userID);
            Customer registeredCustomer = new Customer(firstName, lastName, address, email, password, securityQuestionsAnswers, userID);
            App.crushersBank.registerUser(registeredUser);

            Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
            oldStage.close();
            
        }
    }
}
