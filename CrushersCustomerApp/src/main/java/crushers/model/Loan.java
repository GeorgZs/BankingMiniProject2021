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
    public Loan(){

    }
    public double getAmount(){
        return this.amount;
    }
    public String getPurpose(){
        return this.purpose;
    }
    public PaymentAccount getAccount(){
        return this.account;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }
    public void setPurpose(String purpose){
        this.purpose = purpose;
    }
    public void setAccount(PaymentAccount account){
        this.account = account;
    }
    public String toString(){
        return "Loan: " + this.amount + " SEK (" + this.purpose + ").";
    }
    public int getAccountId(){
        return this.account.getId();
    }
}

