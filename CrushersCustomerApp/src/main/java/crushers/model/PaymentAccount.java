package crushers.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentAccount {
    protected Bank bank;
    protected String name;
    protected String ID;
    protected double balance;
    protected Map<String ,Transaction> transactions;

    public PaymentAccount(String name, double balance, Bank bank) {
        this.name = name;
        this.ID = String.valueOf((int)Math.floor((Math.random()*100000)));
        this.balance = balance;
        this.bank = bank;
        this.transactions = new HashMap<>();
    }

    public String toString(){
        return "Payment Account (ID" + this.name + "): " + this.balance + " SEK";
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
    public Map<String,Transaction> getTransactions(){
        return this.transactions;
    }
    public void withdraw(double amountToWithdraw){
        this.balance = this.balance - amountToWithdraw;
    }
    public void deposit(double amountToDeposit) {
        this.balance = this.balance + amountToDeposit;
    }
    public void addTransactionToMap(Transaction transaction){
        transactions.put(LocalDateTime.now().toString(), transaction);
    }

    public String getType(){
        return "Payment";
    }
}
