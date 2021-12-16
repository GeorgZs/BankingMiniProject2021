package crushers.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private int id;
    private int fromId;
    private int toId;
    private String label = "";
    private PaymentAccount from;
    private PaymentAccount to;
    private double amount;
    private String description;
    private String date;

    public Transaction(PaymentAccount from, PaymentAccount to, double amount, String description) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.description = description;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public Transaction(int id, int fromId, int toId, double amount, String description){
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.description = description;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String toString(){
        return this.amount + " SEK." ;
    }

    public String getDate() {
        return this.date;
    }
    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public String getDescription() {
        return this.description;
    }

    public double getAmount() {
        return this.amount;
    }

    public PaymentAccount getTo() {
        return this.to;
    }

    public PaymentAccount getFrom() {
        return this.from;
    }
    public void setFromId(int fromId){
        this.fromId = fromId;
    }
    public int getFromId(){
        return this.fromId;
    }
    public void setToId(int toId){
        this.toId = toId;
    }
    public int getToId(){
        return this.toId;
    }
}
