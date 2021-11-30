package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

public class RecurringTransaction extends Transaction{
    //subject to change how interval interacts
    private String interval;
    public RecurringTransaction(String accountFrom, String accountTo,
                                double amount, String description){
        super(accountFrom, accountTo, amount, description);
    }
}
