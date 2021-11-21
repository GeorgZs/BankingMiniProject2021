package crushers.models.ExchangeInformation;

import crushers.models.Accounts.StandardAccount;

public class Transaction {
    private int ID;
    private StandardAccount accountFrom;
    private StandardAccount accountTo;
    private double amount;
    private String description;
    //For the date maybe use Time - to calculate age of transaction
    private String date;

    public Transaction(int ID, StandardAccount accountFrom, StandardAccount accountTo,
                       double amount, String description, String date){
        this.ID = ID;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
}