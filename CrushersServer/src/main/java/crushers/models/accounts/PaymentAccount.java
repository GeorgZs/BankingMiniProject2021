package crushers.models.accounts;

import crushers.models.Bank;
import crushers.models.users.Customer;

public class PaymentAccount extends Account {
    public PaymentAccount() {
        super();
        this.type = "payment";
        // empty constructor for Jackson
    }

    public PaymentAccount(Bank bank, Customer owner, double balance) {
        super(bank, owner, balance);
        this.type = "payment";
    }
}