package crushers.model;

public class RecurringTransaction extends Transaction{
    // Interval
    public RecurringTransaction(int id, PaymentAccount accountFrom, PaymentAccount accountTo, double amount, String description, String date) {
        super(accountFrom, accountTo, amount, description, date);

    }
}
