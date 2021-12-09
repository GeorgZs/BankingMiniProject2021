package crushers.model;

public class SavingsAccount extends PaymentAccount{

    private double savingsGoal;

    public SavingsAccount(Bank bank, String type, String name, double savingsGoal) {
        super(bank, type, name);
        this.type = "Savings";
        this.savingsGoal = savingsGoal;
    }

    public SavingsAccount(){

    }

    @Override
    public String toString(){
        return "Savings Account " + super.name + " (ID" + super.getId() + "): " + super.balance + " SEK";
    }
    @Override
    public String getType(){
        return "Savings";
    }
    public double getSavingsGoal(){
        return this.savingsGoal;
    }
}
