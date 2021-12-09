package crushers.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    protected double interestRate;

    
    public PaymentAccount(Bank bank, String type, String name){ // http post constructor
        this.bank = bank;
        this.type = type;
        this.name = name;
        this.transactions = new HashMap<String, Transaction>();
    }
    public PaymentAccount(int id, Bank bank, Customer owner, String type, String number, double balance, double interestRate){ // http get constructor
        this.id = id;
        this.bank = bank;
        this.owner = owner;
        this.type = type;
        this.number = number;
        this.balance = balance;
        this.interestRate = interestRate;
        this.transactions = new HashMap<String, Transaction>();
    }
    public PaymentAccount(){ // for http
        this.transactions = new HashMap<String, Transaction>();
    }

    public String capitalize(String str){ // the creation of this method shows just how little I give a f... i mean smile and wave :) /
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    public String toString(){
        return this.bank + " " + capitalize(this.type) + " account (ID" + this.id + "): " + this.balance + " SEK";
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
        transactions.put(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), transaction);
    }
    public double getInterestRate(){
        return this.interestRate;
    }

    public String getType(){
        return "Payment";
    }
    public void setType(String type){
        this.type = type;
    }
}
