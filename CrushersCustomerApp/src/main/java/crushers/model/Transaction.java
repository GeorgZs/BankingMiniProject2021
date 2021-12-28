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
    public Transaction(){
    }

    @Override
    public String toString(){
        String fromString = this.from == null ? "Bank" : String.valueOf(this.from.getId());
        String toString = this.to == null ? "Bank" : String.valueOf(this.to.getId());
        return String.format("%s Transaction (%d): %d SEK from %s to %s: %s\n", this.date, this.id, (int)this.amount, fromString, toString, this.description);
    }

    public String getFromString(){
        return this.from == null ? "Bank" : String.valueOf(this.from.getId());
    }
    public String getToString(){
        return this.to == null ? "Bank" : String.valueOf(this.to.getId());
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return this.fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return this.toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PaymentAccount getFrom() {
        return this.from;
    }

    public void setFrom(PaymentAccount from) {
        this.from = from;
    }

    public PaymentAccount getTo() {
        return this.to;
    }

    public void setTo(PaymentAccount to) {
        this.to = to;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
