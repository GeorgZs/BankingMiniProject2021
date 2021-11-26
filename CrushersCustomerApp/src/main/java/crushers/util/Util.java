package crushers.util;

import javafx.fxml.FXMLLoader;

public class Util {
    
    public static final FXMLLoader mainLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/MainView.fxml"));
    public static final FXMLLoader registerLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/RegisterView.fxml"));
    public static final FXMLLoader accountLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/AccountView.fxml"));
    public static final FXMLLoader accountCreationLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/AccountCreationView.fxml"));
    public static final FXMLLoader systemLoader = new FXMLLoader(Util.class.getClassLoader().getResource("crushers/views/SystemView.fxml"));

    // maybe static controller references?
    
}
