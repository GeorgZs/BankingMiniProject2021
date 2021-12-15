package crushers.datamodels;

import java.time.LocalDateTime;

public class Transaction {
    private int id = -1;
    private BankAccount from;
    private BankAccount to;
    private double amount;
    private String description;
    private LocalDateTime date;

    public Transaction(){
        // empty for Jackson
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BankAccount getFrom() {
        return from;
    }

    public void setFrom(BankAccount accountFromID) {
        this.from = accountFromID;
    }

    public BankAccount getTo() {
        return to;
    }

    public void setTo(BankAccount accountToID) {
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
