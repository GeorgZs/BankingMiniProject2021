package crushers.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crushers.App;
import crushers.util.Http;
import crushers.util.Json;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentAccount {
    protected int id;
    protected String number;
    protected Customer owner;
    protected String type;

    protected Bank bank;
    protected String name;
    protected double balance;
    protected ArrayList<Transaction> transactions;
    protected double interestRate;

    
    public PaymentAccount(Bank bank, String type, String name){ // http post constructor
        this.bank = bank;
        this.type = type;
        this.name = name;
        this.transactions = new ArrayList<Transaction>();
    }
    public PaymentAccount(int id, Bank bank, Customer owner, String type, String number, double balance, double interestRate){ // http get constructor
        this.id = id;
        this.bank = bank;
        this.owner = owner;
        this.type = type;
        this.number = number;
        this.balance = balance;
        this.interestRate = interestRate;
        this.transactions = new ArrayList<Transaction>();
    }
    public PaymentAccount(){ // for http
        this.transactions = new ArrayList<Transaction>();
    }
    public PaymentAccount(int id, String type){
        this.id = id;
        this.type = type;
    } // Constructor for contacts
    public PaymentAccount(int id, String type, double balance){
        this.id = id;
        this.type = type;
        this.balance = balance;
    } // Constructor for transactions

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
    public void setBalance(double balance){
        this.balance = balance;
    }
    public ArrayList<Transaction> getTransactions() throws IOException, InterruptedException {
        return this.transactions;
    }
    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }
    public double getInterestRate(){
        return this.interestRate;
    }

    public String getType(){
        return "payment";
    }
    public void setType(String type){
        this.type = type;
    }
}
