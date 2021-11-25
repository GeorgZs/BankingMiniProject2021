package crushers.model;

import java.util.ArrayList;
import java.util.List;

public class PaymentAccount {
    protected String name;
    protected String ID;
    protected double balance;
    protected List<Transaction> transactions;

    public PaymentAccount(String name, double balance) {
        this.name = name;
        this.ID = String.valueOf(Math.floor((Math.random()*100000)) + "CB21");
        this.balance = balance;
        this.transactions = new ArrayList<Transaction>();
    }

    public String toString(){
        return "Payment Account (" + this.ID + "): " + this.balance + " SEK";
    }
}
