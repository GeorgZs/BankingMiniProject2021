package crushers.model;

import java.util.ArrayList;

public class StandardAccount {
    
    private double balance;
    ArrayList<Transaction> transactions;

    public StandardAccount(){
        this.balance = 0;
        transactions = new ArrayList<Transaction>();
    }

    public StandardAccount(double balance){
        this.balance = balance;
        transactions = new ArrayList<Transaction>();
    }

}
