package crushers.model;

import java.util.*;

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
    @Override
    public String toString(){
        return this.bank + " " + capitalize(this.type) + " account (ID" + this.id + "): " + this.balance + " SEK";
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Customer getOwner() {
        return this.owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public String getType() {
        return "payment";
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    public void addTransaction(Transaction transaction){
        if(this.transactions == null){
            this.transactions = new ArrayList<Transaction>();
        }
        this.transactions.add(transaction);
    }
    public void printTransactions(){
        String result = "Transactions for account " + getId() + ":\n";
        for(Transaction transaction: this.transactions){
            result += transaction;
        }
        System.out.println(result);
    }
}
