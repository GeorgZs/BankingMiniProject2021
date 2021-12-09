package crushers.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Account {
    protected int id;
    protected String number;
    protected Customer owner;
    protected String type;

    protected Bank bank;
    protected String name;
    protected double balance;
    protected Map<String ,Transaction> transactions;
    protected double interestRate;

    public Account(Bank bank, String type, String name){ // http post constructor
        this.bank = bank;
        this.type = type;
        this.name = name;
    }
    public Account(int id, Bank bank, Customer owner, String type, String number, double balance, double interestRate){ // http get constructor
        this.id = id;
        this.bank = bank;
        this.owner = owner;
        this.type = type;
        this.number = number;
        this.balance = balance;
        this.interestRate = interestRate;
    }
    public Account(){ // for http
        
    }
    public String toString(){
        return this.bank + " [...] account (ID" + this.id + "): " + this.balance + " SEK";
    }
    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
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
    public void addTransactionToMap(Transaction transaction){
        transactions.put(LocalDateTime.now().toString(), transaction);
    }
    public void deposit(double amount){
        this.balance += amount;
    }
    public void withdraw(double amount){
        this.balance -= amount;
    }
    public double getInterestRate(){
        return this.interestRate;
    }
}
