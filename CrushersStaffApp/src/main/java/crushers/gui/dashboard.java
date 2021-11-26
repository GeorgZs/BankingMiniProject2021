package crushers.gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class dashboard {
    @FXML
    private HBox staff;
    @FXML
    private HBox logout;
    @FXML
    private Pane rightPane;
    @FXML
    private TextField id;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

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
        rightPane.setVisible(true);
    }

    @FXML
    public void onClickedLogout(MouseEvent mouseEvent) throws IOException {
        MainController m = new MainController();
        m.changeScene("crushers/gui/Login.fxml", mouseEvent);
    }





}
