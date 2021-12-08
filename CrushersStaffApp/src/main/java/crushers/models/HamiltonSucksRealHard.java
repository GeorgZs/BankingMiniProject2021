package crushers.models;

import crushers.models.accounts.Account;

import java.time.LocalDateTime;

public class HamiltonSucksRealHard {
    public Account to;
    public double amount;
    public String description;
    public LocalDateTime date;

    public HamiltonSucksRealHard() {
        //Empty for jackson
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
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
