package crushers.model;

public class SavingsAccount extends StandardAccount {
    
    double interestRate;

    public SavingsAccount(double interestRate){
        super();
        this.interestRate = interestRate;
    }

    public SavingsAccount(double balance, double interestRate){
        super(balance);
        this.interestRate = interestRate;
    }
}
