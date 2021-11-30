package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;
import crushers.storage.Storable;

import java.time.LocalDateTime;

public class Transaction implements Storable {
    private int id = -1;
    private Account from;
    private Account to;
    private double amount;
    private String description;
    private LocalDateTime date;

    public Transaction(){
        //for fasterXML Jackson
    }

    public Transaction(Account accountFromID, Account accountToID,
                       double amount, String description){
        this.from = accountFromID;
        this.to = accountToID;
        this.amount = amount;
        this.description = description;
        this.date = LocalDateTime.now();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account accountFromID) {
        this.from = accountFromID;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account accountToID) {
        this.to = accountToID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
