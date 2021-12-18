package crushers.models.accounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import crushers.models.Bank;
import crushers.models.users.Customer;

@JsonTypeName("payment")
@JsonIgnoreProperties({ "interestRate" })
public class PaymentAccount extends Account {
    private String name;

    public PaymentAccount() {
        super(); // empty constructor for Jackson
    }

    public PaymentAccount(String name, Bank bank, Customer owner, double balance) {
        super(bank, owner, balance);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
