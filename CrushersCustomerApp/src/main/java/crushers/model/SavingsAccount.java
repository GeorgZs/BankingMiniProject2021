package crushers.model;

import java.util.List;

public class SavingsAccount extends PaymentAccount{
    private double interestRate;

    public SavingsAccount(double balance, List<Transaction> transactions, double interestRate) {
        super(balance, transactions);
        this.interestRate = interestRate;
    }
}
