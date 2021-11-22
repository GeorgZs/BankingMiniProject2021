package crushers.model;

import java.util.List;

public class PaymentAccount {
    private double balance;
    private List<Transaction> transactions;

    public PaymentAccount(double balance, List<Transaction> transactions) {
        this.balance = balance;
        this.transactions = transactions;
    }
}
