package crushers.common.models;

import java.time.LocalDate;

import crushers.common.utils.Storable;

public class Transaction implements Storable {
    private int id = -1;
    private BankAccount from;
    private BankAccount to;
    private double amount;
    private String description;
    private LocalDate date;
    private boolean recurring = false;
    private boolean suspicious = false;
    private String[] labels = new String[0];

    /**
     * Interval in days - recurring transactions only
     */
    private int interval = -1;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isSuspicious() {
        return suspicious;
    }

    public void setSuspicious(boolean suspicious) {
        this.suspicious = suspicious;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }


    // recurring transactions only
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
