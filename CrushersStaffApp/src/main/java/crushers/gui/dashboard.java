package crushers.gui;

import crushers.models.users.Clerk;
import crushers.models.users.ClerkTableView;
import crushers.utils.HTTPUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class dashboard {
    @FXML
    private VBox sidebar;
    @FXML
    private Button addStaff;
    @FXML
    private Button accountInformation;
    @FXML
    private Button registerStaff;
    @FXML
    private HBox logout;
    @FXML
    private GridPane staffOverview;
    @FXML
    private Pane staffInformation;
    @FXML
    private Pane addClerk;
    @FXML
    private Pane creatingStaff;
    @FXML
    private TextField clerkFirstName;
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
    private ChoiceBox<String> clerkSecurityQuestion;
    @FXML
    private TextField clerkAnswer;
    @FXML
    private ImageView plus;
    @FXML
    private ImageView plus2;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TableColumn<Clerk, Integer> clerkID;
    @FXML
    private TableColumn<Clerk, String>firstNameClerk;
    @FXML
    private TableColumn<Clerk, String>lastNameClerk;
    @FXML
    private TableColumn<Clerk, String>emailClerk;
    @FXML
    private TableColumn<Clerk, String>addressClerk;
    @FXML
    private TableView<ClerkTableView> tableView;

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
    void initializeTable() {
        clerkID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        firstNameClerk.setCellValueFactory(new PropertyValueFactory<>("First name"));
        lastNameClerk.setCellValueFactory(new PropertyValueFactory<>("Last name"));
        emailClerk.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressClerk.setCellValueFactory(new PropertyValueFactory<>("street address"));

        tableView.setItems(createList());

    }

    private ObservableList<ClerkTableView> createList() {

            try {
                Collection<Clerk> clerkList = HTTPUtils.get("/staff/@bank", Collection.class);
                ObservableList<ClerkTableView> informationClerk = FXCollections.observableArrayList();;
                for (Clerk clerk: clerkList) {
                    informationClerk.add(new ClerkTableView(clerk.getId(),clerk.getFirstName(),clerk.getLastName(),clerk.getEmail(),clerk.getAddress()));
                }
                return informationClerk;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }


    @FXML
    void initialize(){
        clerkSecurityQuestion.setItems(FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList(clerkQuestion))));
        clerkSecurityQuestion.setStyle("-fx-font-family: SansSerif");
    }

    @FXML
    private void onHover(MouseEvent mouseEvent) {
        try{
            HBox logout = (HBox) mouseEvent.getSource();
            logout.setOpacity(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickedStaff(javafx.event.ActionEvent event) throws Exception {
        if(addClerk.isVisible() || creatingStaff.isVisible()){
            addClerk.setVisible(false);
            creatingStaff.setVisible(false);
            plus.setImage(new Image("file:src/main/resources/crushers/gui/pesalogin/icons8-plus-48.png"));


        } else {
            addClerk.setVisible(true);
            creatingStaff.setVisible(true);
            plus.setImage(new Image("file:src/main/resources/crushers/gui/pesalogin/icons8-minus-48.png"));
        }
    }

    @FXML
    private void onClickedInformation(javafx.event.ActionEvent event) throws Exception {
        if(staffOverview.isVisible() || staffInformation.isVisible()) {
            staffOverview.setVisible(false);
            staffInformation.setVisible(false);
            plus2.setImage(new Image("file:src/main/resources/crushers/gui/pesalogin/icons8-plus-48.png"));
        } else {
            staffOverview.setVisible(true);
            staffInformation.setVisible(true);
            plus2.setImage(new Image("file:src/main/resources/crushers/gui/pesalogin/icons8-minus-48.png"));
        }
    }

    @FXML
    public void onClickedLogout(MouseEvent mouseEvent) throws IOException {
        MainController m = new MainController();
        m.changeScene("Login.fxml", mouseEvent);
    }

    @FXML
    private void onHoverEnded(MouseEvent mouseEvent) {
        try{
            HBox logout = (HBox)mouseEvent.getSource();
            logout.setOpacity(1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registerClerk(javafx.event.ActionEvent event) throws Exception {
        String clerkFirst = clerkFirstName.getText();
        String clerkLast = clerkLastName.getText();
        String clerkAddress = clerkStreetAddress.getText();
        String clerkCity = city.getText();
        String clerkZip = clerkPostal.getText();
        String clerkEmail = email.getText();
        String clerkPassword = password.getText();

        if(clerkFirst.isEmpty() || clerkFirst.isBlank()) {
            clerkFirstName.setStyle("-fx-border-color: red ; -fx-border-width: 1px");
        } else {
            clerkFirstName.setStyle("-fx-border-color: transparent");
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

        Clerk clerk = new Clerk(clerkEmail, clerkFirst,clerkLast,clerkAddress, clerkPassword, null, null);
        HTTPUtils.post("/staff", clerk, Clerk.class);
    }

    @FXML
    public void searchStaff() {

    }



    /*
        String[] security = new String[10];
        //security[0] = clerkPassword;
        //need question at index 0 and answer index 1

    }

     */
}
