package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;
import crushers.storage.Storable;

public class Transaction implements Storable {
    private int ID;
    private Account accountFrom;
    private Account accountTo;
    private double amount;
    private String description;
    //For the date maybe use Time - to calculate age of transaction
    private String date;

    public Transaction(int ID, Account accountFrom, Account accountTo,
                       double amount, String description, String date){
        this.ID = ID;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    @Override
    public int getId() {
        return this.ID;
    }

    @Override
    public void setId(int id) {
        this.ID = id;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
