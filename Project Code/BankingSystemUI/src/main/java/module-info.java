module com.example.bankingsystemui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bankingsystemui to javafx.fxml;
    exports com.example.bankingsystemui;
}