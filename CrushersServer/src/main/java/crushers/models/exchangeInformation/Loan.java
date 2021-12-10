package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

public class Loan {

    private double amount;
    private String purpose;
    private Account account;

    public Loan(){
        //for jackson
    }

    public Loan(double amount, String purpose, Account account){
        this.amount = amount;
        this.purpose = purpose;
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public Account getAccount() {
        return account;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
