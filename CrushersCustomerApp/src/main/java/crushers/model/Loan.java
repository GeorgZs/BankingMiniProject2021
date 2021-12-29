package crushers.model;

public class Loan {

    private double amount;
    private String purpose;
    private PaymentAccount account;
    private int accountId;

    
    public Loan(double amount, String purpose, PaymentAccount account){
        this.amount = amount;
        this.purpose = purpose;
        this.account = account;
        this.accountId = this.account.getId();
    }

    public Loan(){

    }


    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public PaymentAccount getAccount() {
        return this.account;
    }

    public void setAccount(PaymentAccount account) {
        this.account = account;
    }

    public int getAccountId() {
        return this.account.getId();
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }


    
}

