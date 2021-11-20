package crushers.models.Accounts;

import crushers.models.exchangeInformation.Transaction;

import java.sql.Time;
import java.util.LinkedHashMap;

public class StandardAccount {
    private double balance;
    private LinkedHashMap<Time, Transaction> transactions;

    public StandardAccount(){
        this.balance = 0.0;
        this.transactions = new LinkedHashMap<>();
    }
}
