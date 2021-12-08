package crushers.models;

import crushers.models.accounts.Account;

import java.time.LocalDateTime;

public class Withdraw {
    public Account from;
    public double amount;
    public String description;
    public LocalDateTime date;

    public Withdraw() {
        //Empty constructor for jackson
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
