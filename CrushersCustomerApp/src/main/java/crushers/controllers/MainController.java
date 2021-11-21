package crushers.controllers;

import java.io.IOException;

import crushers.App;
import crushers.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label forgottenPasswordLabel;
    @FXML
    private Label requestHelpLabel;
    @FXML
    private ImageView loginScreenImage;
    @FXML
    private Label invalidLoginLabel;
    
    public void register(ActionEvent e) throws IOException{

        String email = usernameField.getText();
        String password = passwordField.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/RegisterView.fxml"));
        root = loader.load();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
        stage.setTitle("Registration Form");
        stage.show();

    }

    public void login(ActionEvent e) throws IOException{

        String email = usernameField.getText();
        String password = passwordField.getText();

        for(User user: App.bank.getUsers()){
            if(user.getEmail().equals(email) && user.getPassword().equals(password)){

            Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow(); // close upon login
            oldStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/SystemView.fxml"));
            root = loader.load();
            SystemController sysCtrl = loader.getController();
            sysCtrl.displayName(user.getFirstName() + " " + user.getLastName());
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
            stage.setTitle("Crushers System");
            stage.show();
            return;
            }
        }  
        invalidLoginLabel.setText("Invalid username or password!");
    }

    public void requestHelp(){
        System.out.println("cry about it");
        for(User user: App.bank.getUsers()){
            System.out.println(user);
        }
    }

    public void forgottenPassword(){
        System.out.println("lol dummy");
    }

}
