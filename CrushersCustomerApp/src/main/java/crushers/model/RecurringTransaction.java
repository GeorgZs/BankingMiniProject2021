package crushers.model;

import java.time.LocalDateTime;

public class RecurringTransaction extends Transaction{
    // Interval
    public RecurringTransaction(int id, PaymentAccount accountFrom, PaymentAccount accountTo, double amount, String description) {
        super(accountFrom, accountTo, amount, description);

    }
}
