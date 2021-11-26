package crushers.model;

import java.util.ArrayList;
import java.util.List;

public class PaymentAccount {
    protected Bank bank;
    protected String name;
    protected String ID;
    protected double balance;
    protected List<Transaction> transactions;

    public PaymentAccount(String name, double balance, Bank bank) {
        this.name = name;
        this.ID = String.valueOf((int)Math.floor((Math.random()*100000)));
        this.balance = balance;
        this.bank = bank;
        this.transactions = new ArrayList<Transaction>();
    }

    public String toString(){
        return "Payment Account (ID" + this.ID + "): " + this.balance + " SEK";
    }
    public Bank getBank(){
        return this.bank;
    }
    public String getName(){
        return this.name;
    }
    public String getID(){
        return this.ID;
    }
    public double getBalance(){
        return this.balance;
    }
    public List<Transaction> getTransactions(){
        return this.transactions;
    }
    public String getType(){
        return "Payment";
    }
}
