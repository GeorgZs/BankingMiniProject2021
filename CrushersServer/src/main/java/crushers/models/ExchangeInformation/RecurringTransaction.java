package crushers.models.ExchangeInformation;

import crushers.models.Accounts.StandardAccount;

public class RecurringTransaction extends Transaction{
    //subject to change how interval interacts
    private String interval;
    public RecurringTransaction(int ID, StandardAccount accountFrom, StandardAccount accountTo,
                                double amount, String description, String date){
        super(ID, accountFrom, accountTo, amount, description, date);
    }
}
