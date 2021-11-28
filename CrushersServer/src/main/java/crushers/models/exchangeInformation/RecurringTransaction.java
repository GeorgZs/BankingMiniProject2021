package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

public class RecurringTransaction extends Transaction{
    //subject to change how interval interacts
    private String interval;

    public RecurringTransaction(){
        //for fasterXML Jackson
    }
    public RecurringTransaction(String accountFromID, String accountToID,
                                double amount, String description, String date){
        //REMOVED ID from constructor as the ID increments everytime a transaction
        //is created using the endpoint /transactions
        super(accountFromID, accountToID, amount, description, date);
    }
}
