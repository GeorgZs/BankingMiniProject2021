package crushers.controllers;

import crushers.datamodels.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;

public class TransactionListController {
    @FXML
    private TableView transactionTableView;
    @FXML
    private TableColumn<Transaction, Integer> iD;
    @FXML
    private TableColumn<Transaction, String> from;
    @FXML
    private TableColumn<Transaction, String> to;
    @FXML
    private TableColumn<Transaction, Double> amount;
    @FXML
    private TableColumn<Transaction, LocalDateTime> date;
    @FXML
    private Button search;
    @FXML
    private GridPane transactionOverview;
    @FXML
    private TextField searchInput;
}
