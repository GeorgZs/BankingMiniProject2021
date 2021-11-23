package crushers.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User{
    private final double interestRate = 0.02; // for now
    private double savingsGoal = 0; // for now
    private ArrayList<PaymentAccount> accountList;
    private ArrayList<Contact> contactList;


    public Customer(String firstName, String lastName, String address, String email, String password,
    ArrayList<String> securityQuestions, int ID) {
        super(firstName, lastName, address, email, password, securityQuestions, ID);
        this.accountList = new ArrayList<PaymentAccount>();
        this.contactList = new ArrayList<Contact>();
    }
    public void createPaymentAccount() {
        List<Transaction> transactionList = new ArrayList<>();
        PaymentAccount paymentAccount = new PaymentAccount(0,transactionList);
        accountList.add(paymentAccount);
    }
    public void createSavingsAccount() {
        List<Transaction> transactionList = new ArrayList<>();
        PaymentAccount savingsAccount = new SavingsAccount(0,transactionList,interestRate,savingsGoal);
        accountList.add(savingsAccount);
    }
}
