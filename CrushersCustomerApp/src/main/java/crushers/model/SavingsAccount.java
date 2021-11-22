package crushers.model;

import java.util.List;

public class SavingsAccount extends PaymentAccount{
    private double interestRate;
    private double savingsGoal;

    public SavingsAccount(double balance, List<Transaction> transactions, double interestRate, double savingsGoal) {
        super(balance, transactions);
        this.interestRate = interestRate;
        this.savingsGoal = savingsGoal;
    }
}
