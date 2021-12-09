package crushers.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer extends User{

    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private ArrayList<String> securityQuestions;
    private LocalDateTime lastLoginAt;
    private Notification notification;

    private ArrayList<PaymentAccount> accountList;
    private ArrayList<Contact> contactList;


    public Customer(int id, String firstName, String lastName, String address, String email, String password,
    ArrayList<String> securityQuestions, LocalDateTime lastLoginAt, Notification notification) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.lastLoginAt = lastLoginAt;
        this.notification = notification;
        //super(firstName, lastName, address, email, password, securityQuestions);
        this.accountList = new ArrayList<PaymentAccount>();
        this.contactList = new ArrayList<Contact>();
    } // default constructor

    // public Customer(int id, String email, String firstName, String lastName, String address, String password, ArrayList<String> securityQuestions, LocalDateTime lastLoginAt){
    //     super(firstName, lastName, address, email, password, securityQuestions);
    //     this.id = id;
    //     this.lastLoginAt = lastLoginAt;
    //     this.accountList = new ArrayList<PaymentAccount>();
    //     this.contactList = new ArrayList<Contact>();
    // } // response customer

    public Customer(){
    } // empty constructor for json

    public void createPaymentAccount(String name, Bank bank) {
        this.accountList.add(new PaymentAccount(name, 0, bank));
    }
    public void createSavingsAccount(String name, double savingsGoal, Bank bank) {
        PaymentAccount savingsAccount = new SavingsAccount(name, 0, savingsGoal, bank);
        this.accountList.add(savingsAccount);
    }

    public void createAccount(Object acc){
        if(acc instanceof SavingsAccount){
            this.accountList.add((SavingsAccount)acc);
        }else if(acc instanceof PaymentAccount){
            this.accountList.add((PaymentAccount)acc);
        }else{
            System.out.println("Invalid type");
        }
        System.out.println("Created account");
    }

    public void resetPassword(){
        //User must input their current password.
        //If entered password matches current password, user may create a new password

    }
    @Override
    public String toString(){
        String result = this.id + ": " + this.firstName + " " + this.lastName + " lives at " + this.address + "\n" + this.email + " " + this.password +
        "\n" + this.securityQuestions + "\nAccounts:\n";
        for(PaymentAccount account: this.accountList){
            result += account + "\n";
        }
        return result;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getAddress(){
        return this.address;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
    public ArrayList<String> getSecurityQuestions(){
        return this.securityQuestions;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public ArrayList<PaymentAccount> getAccountList(){
        return this.accountList;
    }
    public void setAccountList(ArrayList<PaymentAccount> accounts){
        this.accountList = accounts;
    }
    public int getId(){
        return this.id;
    }
    public LocalDateTime getLastLoginAt(){
        return this.lastLoginAt;
    }
    public Notification getNotification(){
        return this.notification;
    }
    
}
