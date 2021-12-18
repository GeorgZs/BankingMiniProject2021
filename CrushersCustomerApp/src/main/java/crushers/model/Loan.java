package crushers.model;

public class Loan {

    private double amount;
    private String purpose;
    private PaymentAccount account;

    public Loan(double amount, String purpose, PaymentAccount account){
        this.amount = amount;
        this.purpose = purpose;
        this.account = account;
    }

}

