package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

import java.time.LocalDateTime;

public class Transaction {
    private Account accountFrom;
    private Account accountTo;
    private double amount;
    private String description;

    public Transaction(Account accountFrom, Account accountTo,
                       double amount, String description){
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.description = description;
    }
}
