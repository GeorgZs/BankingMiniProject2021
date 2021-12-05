package crushers.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentAccount {
    protected int id;
    protected String number;
    protected Customer owner;
    protected String type;

    protected Bank bank;
    protected String name;
    protected double balance;
    protected Map<String ,Transaction> transactions;

    public PaymentAccount(String name, double balance, Bank bank) {
        this.name = name;
        this.balance = balance;
        this.bank = bank;
        this.transactions = new HashMap<>();
    }
    public PaymentAccount(Bank bank, String type, String name){ // http post constructor
        this.bank = bank;
        this.type = type;
        this.name = name;
    }
    public PaymentAccount(int id, Bank bank, Customer owner, String type, String number, double balance){ // http get constructor
        this.id = id;
        this.bank = bank;
        this.owner = owner;
        this.type = type;
        this.number = number;
        this.balance = balance;
    }
    public PaymentAccount(){ // for http
        
    }

    public String toString(){
        return "Payment Account " + this.name + " (ID" + this.getId() + "): " + this.balance + " SEK";
    }
    public Bank getBank(){
        return this.bank;
    }
    public int getId(){
        return this.id;
    }
    public String getNumber(){
        return this.number;
    }
    public Customer getOwner(){
        return this.owner;
    }
    public String getName(){
        return this.name;
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
        return this.type;
    }
}
