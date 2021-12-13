package crushers.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private int id;
    private String label = "";
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
        return this.amount + " SEK." ;
    }

    public String getDate() {
        return date;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
