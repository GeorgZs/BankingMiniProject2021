package crushers.model;

public class Transaction {
    private int id;
    private PaymentAccount accountFrom;
    private PaymentAccount accountTo;
    private double amount;
    private String description;
    private String date;

    public Transaction(PaymentAccount accountFrom, PaymentAccount accountTo, double amount, String description, String date) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

}
