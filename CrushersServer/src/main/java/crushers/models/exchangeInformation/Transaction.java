package crushers.models.exchangeInformation;

import crushers.storage.Storable;

import java.time.LocalDateTime;

public class Transaction implements Storable {
    private int ID;
    private String accountFromID;
    private String accountToID;
    private double amount;
    private String description;
    private String date;

    public Transaction(){
        //for fasterXML Jackson
    }

    public Transaction(String accountFromID, String accountToID,
                       double amount, String description){
        this.ID = 1;
        this.accountFromID = accountFromID;
        this.accountToID = accountToID;
        this.amount = amount;
        this.description = description;
        this.date = LocalDateTime.now().toString();
    }

    @Override
    public int getId() {
        return this.ID;
    }

    @Override
    public void setId(int id) {
        this.ID = id;
    }

    public String getAccountFromID() {
        return accountFromID;
    }

    public void setAccountFromID(String accountFromID) {
        this.accountFromID = accountFromID;
    }

    public String getAccountToID() {
        return accountToID;
    }

    public void setAccountToID(String accountToID) {
        this.accountToID = accountToID;
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
