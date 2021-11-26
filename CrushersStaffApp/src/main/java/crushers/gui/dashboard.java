package crushers.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class dashboard {
    @FXML
    private TextField clertkFirstName;
    @FXML
    private TextField clerkLastName;
    @FXML
    private TextField clerkStreetAddress;
    @FXML
    private TextField city;
    @FXML
    private TextField clerkPostal;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private ChoiceBox clerkSecurityQuestion;
    @FXML
    private TextField clerkAnswer;
    @FXML
    private HBox staff;
    @FXML
    private HBox logout;
    @FXML
    private Pane registerPane;

    private String[] clerkQuestion = {
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
        clerkSecurityQuestion.setItems(FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList(clerkQuestion))));
        clerkSecurityQuestion.setStyle("-fx-font-family: SansSerif");
    }

    @FXML
    private void onHover(MouseEvent mouseEvent) {
        try{
            HBox staff = (HBox)mouseEvent.getSource();
            staff.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onHoverEnded(MouseEvent mouseEvent) {
        try{
            HBox staff = (HBox)mouseEvent.getSource();
            staff.setOpacity(1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickedStaff(MouseEvent mouseEvent) {
        registerPane.setVisible(true);
    }

    @FXML
    public void onClickedLogout(MouseEvent mouseEvent) throws IOException {
        MainController m = new MainController();
        m.changeScene("Login.fxml", mouseEvent);
    }

    @FXML
    public void registerClerk(javafx.event.ActionEvent event) {
        MainController m = new MainController();
        String clerkFirst = clertkFirstName.getText();
        String clerkLast = clerkLastName.getText();
        String clerkAddress = clerkStreetAddress.getText();
        String clerkCity = city.getText();
        String clerkZip = clerkPostal.getText();
        String clerkEmail = email.getText();
        String clerkPassword = password.getText();

        if(clerkFirst.isEmpty() || clerkFirst.isBlank()) {
            clertkFirstName.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clertkFirstName.setStyle("-fx-border-color: transparent");
        } if(clerkLast.isEmpty() || clerkLast.isBlank()) {
            clerkLastName.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clerkLastName.setStyle("-fx-border-color: transparent");
        } if(clerkAddress.isEmpty() || clerkAddress.isBlank()) {
            clerkStreetAddress.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clerkStreetAddress.setStyle("-fx-border-color: transparent");
        } if(clerkCity.isBlank() || clerkCity.isEmpty()) {
            city.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            city.setStyle("-fx-border-color: transparent");
        } if(clerkZip.isEmpty() || clerkZip.isBlank()) {
            clerkPostal.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clerkPostal.setStyle("-fx-border-color: transparent");
        } if(clerkEmail.isEmpty() || clerkEmail.isBlank() || !clerkEmail.contains("@")) {
            email.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            email.setStyle("-fx-border-color: transparent");
        } if(clerkPassword.isEmpty() || clerkPassword.isBlank() || clerkPassword.length() < 8) {
            password.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            password.setStyle("-fx-border-color: transparent");
        }
    }
}
