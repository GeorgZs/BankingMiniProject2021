package crushers.model;

import java.util.Date;

public class Transaction {
    
    private String ID;
    private StandardAccount accountFrom;
    private StandardAccount accountTo;
    private double amount;
    private String description;
    private Date date;

    public Transaction(String ID, StandardAccount accountFrom, StandardAccount accountTo, double amount, String description){
        this.ID = ID;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.description = description;
        this.date = new Date();
    }
}
