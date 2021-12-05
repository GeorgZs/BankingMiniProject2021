package crushers.model;

public class SavingsAccount extends PaymentAccount{

    private double savingsGoal;

    public SavingsAccount(String name, double balance, double savingsGoal, Bank bank) {
        super(name, balance, bank);
        this.savingsGoal = savingsGoal;
    }

    @Override
    public String toString(){
        return "Savings Account " + super.name + " (ID" + super.getId() + "): " + super.balance + " SEK";
    }
    public double getSavingsGoal(){
        return this.savingsGoal;
    }
}
