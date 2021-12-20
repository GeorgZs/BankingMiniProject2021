package crushers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WindowManager {

  private static Stage MainWindow;
  private static Stage ModalWindow;

  public static class Pages {
    private final static String VIEWS_FOLDER = "views/";
    private final static String JAVAFX = ".fxml";

    public final static String Login = VIEWS_FOLDER + "Login" + JAVAFX;
    public final static String Register = VIEWS_FOLDER + "Register" + JAVAFX;
    public final static String ForgotPassword = VIEWS_FOLDER + "ForgotPassword" + JAVAFX;

    public final static String Dashboard = VIEWS_FOLDER + "Dashboard" + JAVAFX;
    public final static String Deposit = VIEWS_FOLDER + "Deposit" + JAVAFX;
    public final static String Withdraw = VIEWS_FOLDER + "Withdraw" + JAVAFX;
    public final static String Payment = VIEWS_FOLDER + "Payment" + JAVAFX;
    public final static String Savings = VIEWS_FOLDER + "Savings" + JAVAFX;
    public final static String Interest = VIEWS_FOLDER + "InterestRate" + JAVAFX;
    public final static String TransactionList = VIEWS_FOLDER + "TransactionList" + JAVAFX;
    public final static String SuspiciousTransaction = VIEWS_FOLDER + "SuspiciousTransaction" + JAVAFX;
  }

  public static void setMainWindow(Stage window) {
    if (MainWindow != null) throw new IllegalStateException("Main window already set");
    MainWindow = window;
  }

  public static void showPage(String page) throws IOException {
    Parent root = FXMLLoader.load(App.class.getResource(page));
    
    Scene scene = new Scene(root);
    scene.getRoot().setStyle("-fx-font-family: SansSerif"); // fixes apple font

    MainWindow.setScene(scene);
  }

  public static void showModal(String page, String title) throws IOException {
    Parent root = FXMLLoader.load(App.class.getResource(page));
    
    ModalWindow = new Stage();
    // ModalWindow.getIcons().add(new Image("crushers/imgs/logo.jpg"));
    ModalWindow.setScene(new Scene(root));
    ModalWindow.setTitle(title);
    ModalWindow.initModality(Modality.APPLICATION_MODAL);
    ModalWindow.show();
  }

  public static void closeModal() {
    ModalWindow.close();
  }
}
