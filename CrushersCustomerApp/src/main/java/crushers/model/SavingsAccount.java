package crushers.model;

public class SavingsAccount extends PaymentAccount{

    // private double savingsGoal;

    public SavingsAccount(Bank bank, String type, String name, double interestRate) {
        super(bank, type, name);
        this.type = "Savings";
        this.interestRate = interestRate;
        // this.savingsGoal = savingsGoal;
    }

    public SavingsAccount(){
        super();
    }

    @Override
    public String toString(){
        return super.bank + " Savings account (ID" + super.id + "): " + super.balance + " SEK";
    }
    @Override
    public String getType(){
        return "Savings";
    }
    // public double getSavingsGoal(){
    //     return this.savingsGoal;
    // }
}
