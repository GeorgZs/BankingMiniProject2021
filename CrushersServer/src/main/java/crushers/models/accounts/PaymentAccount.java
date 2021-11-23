package crushers.models.accounts;

import com.fasterxml.jackson.annotation.JsonTypeName;

import crushers.models.Bank;
import crushers.models.users.Customer;

@JsonTypeName("payment")
public class PaymentAccount extends Account {
    public PaymentAccount() {
        super(); // empty constructor for Jackson
    }

    public PaymentAccount(Bank bank, Customer owner, double balance) {
        super(bank, owner, balance);
    }
}
