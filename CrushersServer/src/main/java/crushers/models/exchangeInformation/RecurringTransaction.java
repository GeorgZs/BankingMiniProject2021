package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

public class RecurringTransaction extends Transaction {
    //subject to change how interval interacts
    private int interval;

    public RecurringTransaction() {
        //for fasterXML Jackson
    }
    public RecurringTransaction(Account from, Account to, double amount, String description) {
        //REMOVED ID from constructor as the ID increments everytime a transaction
        //is created using the endpoint /transactions
        super(from, to, amount, description);
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
