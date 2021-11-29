package crushers.controllers;

import java.io.IOException;

import crushers.util.Util;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class SystemController {
    
    public void logout(ActionEvent e) throws IOException{
        Util.closeAndShow("MainView", "Crushers Bank", e);
    }
    public void selectDifferentAccount(ActionEvent e) throws IOException{
        Util.closeAndShow("AccountView", "Select an Account", e);
    }
}