package crushers.model;

import java.util.List;

public class StandardAccount {
    private double balance;
    private List<Transaction> transactions;

    public StandardAccount(double balance, List<Transaction> transactions) {
        this.balance = balance;
        this.transactions = transactions;
    }
}
