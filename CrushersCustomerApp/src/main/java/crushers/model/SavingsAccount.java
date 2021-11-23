package crushers.model;

import java.util.List;

public class SavingsAccount extends PaymentAccount{
    private double interestRate;

    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }

    @Override
    public String toString(){
        return "Savings Account (" + super.ID + "): " + super.balance;
    }
}
