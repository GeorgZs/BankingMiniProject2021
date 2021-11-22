package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

public class Transaction {
    private int ID;
    private Account accountFrom;
    private Account accountTo;
    private double amount;
    private String description;
    //For the date maybe use Time - to calculate age of transaction
    private String date;

    public Transaction(int ID, Account accountFrom, Account accountTo,
                       double amount, String description, String date){
        this.ID = ID;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
}
