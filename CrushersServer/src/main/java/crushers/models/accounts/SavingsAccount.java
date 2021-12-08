package crushers.models.accounts;

import com.fasterxml.jackson.annotation.JsonTypeName;

import crushers.models.Bank;
import crushers.models.users.Customer;

@JsonTypeName("savings")
public class SavingsAccount extends Account {
    //subject to change as this is set upon creation of bank
    private String name;

    public SavingsAccount(){
        super(); // empty constructor for Jackson
    }

    public SavingsAccount(Bank bank, Customer owner, double balance){
        super(bank, owner, balance);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInterestRate() {
        if(super.getInterestRate() == 0.00){
            super.setInterestRate(0.20);
            return super.getInterestRate();
        }
        else{
            return super.getInterestRate();
        }
    }

    public void setInterestRate(double interestRate){
        super.setInterestRate(interestRate);
    }
}
