package crushers.model;

import java.util.List;

public class SavingsAccount extends PaymentAccount{

    private double savingsGoal;

    public SavingsAccount(String name, double balance, double savingsGoal) {
        super(name, balance);
        this.savingsGoal = savingsGoal;
    }

    @Override
    public String toString(){
        return "Savings Account (ID" + super.ID + "): " + super.balance + " SEK";
    }
}
