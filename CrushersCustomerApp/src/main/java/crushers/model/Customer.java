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
        this.accountList = new ArrayList<PaymentAccount>();
        this.contactList = new ArrayList<Contact>();
    } // full constructor

    public Customer(String email, String firstName, String lastName, String address, String password, ArrayList<String> securityQuestions){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.accountList = new ArrayList<PaymentAccount>();
        this.contactList = new ArrayList<Contact>();
    } // creation constructor

    public Customer(){
    } // empty constructor for json

    public void createAccount(Object acc){
        if(acc instanceof SavingsAccount){
            this.accountList.add((SavingsAccount)acc);
        }else if(acc instanceof PaymentAccount){
            this.accountList.add((PaymentAccount)acc);
        }else{
            System.out.println("Invalid type");
        }
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
    public ArrayList<Contact> getContactList(){
        return this.contactList;
    }
    public void setContactList(ArrayList<Contact> contacts){
        this.contactList = contacts;
    }
    public void setAccountList(ArrayList<PaymentAccount> accounts){
        ArrayList<PaymentAccount> all = new ArrayList<PaymentAccount>();
        for(PaymentAccount account: accounts){
            if(account.getInterestRate() == 0.0){
                account.setType("Payment");
                all.add(account);
            }else{
                account.setType("Savings");
                all.add(account);
            }
        }
        this.accountList = all;
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
    public void addAccount(PaymentAccount account){
        if(account.getInterestRate() == 0.0){
            this.accountList.add(account);
        }else{
            this.accountList.add((SavingsAccount)account);
        }
    }
    
}
