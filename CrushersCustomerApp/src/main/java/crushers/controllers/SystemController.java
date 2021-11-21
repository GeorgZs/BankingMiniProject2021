package crushers.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class SystemController {
    
    private Stage stage;
    private Parent root;

    @FXML
    private Label welcomeLabel;
    @FXML
    private Button logoutButton;

    public void displayName(String username){
        welcomeLabel.setText("Welcome, " + username);
    }

    public void logout(ActionEvent e) throws IOException{
        
        Stage oldStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        oldStage.close();

        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("crushers/views/MainView.fxml"));
        root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setTitle("Crushers Bank");
        stage.getIcons().add(new Image("crushers/imgs/logo.jpg"));
    }

}
