package crushers.models.accounts;

import crushers.models.Bank;
import crushers.models.users.Customer;

public class SavingsAccount extends Account {
    //subject to change as this is set upon creation of bank
    private final double INTEREST_RATE = 0.2;

    public SavingsAccount(){
        super();
        this.type = "savings";
        // empty constructor for Jackson
    }

    public SavingsAccount(Bank bank, Customer owner, double balance){
        super(bank, owner, balance);
        this.type = "savings";
    }
}
