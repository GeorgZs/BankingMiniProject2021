package crushers.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private int id;
    private PaymentAccount accountFrom;
    private PaymentAccount accountTo;
    private double amount;
    private String description;
    private String date;

    public Transaction(PaymentAccount accountFrom, PaymentAccount accountTo, double amount, String description) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.description = description;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public String toString(){
        return "Amount: " + this.amount + " SEK.";
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentAccount getAccountTo() {
        return accountTo;
    }

    public PaymentAccount getAccountFrom() {
        return accountFrom;
    }
}
