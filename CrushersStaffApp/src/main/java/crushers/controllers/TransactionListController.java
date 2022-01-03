package crushers.controllers;

import crushers.common.ServerFacade;
import crushers.common.models.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransactionListController implements Initializable {
    @FXML
    private TableView<Transaction> transactionTableView;
    @FXML
    private TableColumn<Transaction, Integer> id;
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
    private TextField searchInput;
    
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private int accountId = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        from.setCellValueFactory(new PropertyValueFactory<>("from"));
        to.setCellValueFactory(new PropertyValueFactory<>("to"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        executor.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                try {
                    if (accountId < 0) return;

                    List<Transaction> transactions = ServerFacade.instance.listAllTransactionsOfAccount(accountId);
                    transactionTableView.getItems().clear();
                    transactionTableView.getItems().addAll(transactions);
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }, 0, 5, TimeUnit.SECONDS);
    }

    @FXML
    public void search() {
        try {
            accountId = Integer.parseInt(searchInput.getText());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
