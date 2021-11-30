package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

import java.time.LocalDateTime;

public class Transaction {
    private String accountFrom;
    private String accountTo;
    private double amount;
    private String description;

    public Transaction(String accountFrom, String accountTo,
                       double amount, String description){
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.description = description;
    }
}
