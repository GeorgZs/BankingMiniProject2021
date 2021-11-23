package crushers.model;

import java.util.List;

public class SavingsAccount extends PaymentAccount{
    private double interestRate;
    private double savingsGoal;

<<<<<<< HEAD
    public SavingsAccount(double balance, double interestRate) {
        super(balance);
=======
    public SavingsAccount(double balance, List<Transaction> transactions, double interestRate, double savingsGoal) {
        super(balance, transactions);
>>>>>>> 7bedd37bb6e5606c0a6e701c143bfbf01b16f21a
        this.interestRate = interestRate;
        this.savingsGoal = savingsGoal;
    }

    @Override
    public String toString(){
        return "Savings Account (" + super.ID + "): " + super.balance;
    }
}
