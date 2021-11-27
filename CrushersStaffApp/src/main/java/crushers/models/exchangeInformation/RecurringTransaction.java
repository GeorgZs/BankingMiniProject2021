package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

public class RecurringTransaction extends Transaction{
    //subject to change how interval interacts
    private String interval;
    public RecurringTransaction(int ID, Account accountFrom, Account accountTo,
                                double amount, String description, String date){
        super(ID, accountFrom, accountTo, amount, description, date);
    }
}
