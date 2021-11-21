package crushers.model;

public class RecurringTransaction extends Transaction {

    String interval;

    public RecurringTransaction(String ID, StandardAccount accountFrom, StandardAccount accountTo, double amount, String description, String interval){
        super(ID, accountFrom, accountTo, amount, description);
        this.interval = interval;
    }
}
