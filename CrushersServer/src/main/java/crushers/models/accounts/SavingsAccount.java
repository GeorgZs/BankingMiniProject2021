package crushers.models.accounts;

import com.fasterxml.jackson.annotation.JsonTypeName;

import crushers.models.Bank;
import crushers.models.users.Customer;

@JsonTypeName("savings")
public class SavingsAccount extends Account {
    //subject to change as this is set upon creation of bank
    private double interestRate = 0.20;
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

}
