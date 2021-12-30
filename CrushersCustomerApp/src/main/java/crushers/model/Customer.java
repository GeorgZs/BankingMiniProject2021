package crushers.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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
    private LinkedHashMap<LocalDateTime, String> notifications;

    private ArrayList<PaymentAccount> accountList;
    private ArrayList<Contact> contactList;
    private ArrayList<Loan> loans;


    // public Customer(int id, String firstName, String lastName, String address, String email, String password,
    // ArrayList<String> securityQuestions, LocalDateTime lastLoginAt, LinkedHashMap<LocalDateTime, String> notifications) {
    //     this.id = id;
    //     this.firstName = firstName;
    //     this.lastName = lastName;
    //     this.address = address;
    //     this.email = email;
    //     this.password = password;
    //     this.securityQuestions = securityQuestions;
    //     this.lastLoginAt = lastLoginAt;
    //     // this.notifications = notifications;
    //     this.accountList = new ArrayList<PaymentAccount>();
    //     this.contactList = new ArrayList<Contact>();
    //     this.loans = new ArrayList<Loan>();
    //     this.notifications = new LinkedHashMap<LocalDateTime, String>();

    // } // full constructor

    public Customer(int id, String email, String firstName, String lastName, String address, String password, ArrayList<String> securityQuestions,
    LocalDateTime lastLoginAt, ArrayList<Loan> loans, LinkedHashMap<LocalDateTime, String> notifications){
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.lastLoginAt = lastLoginAt;
        this.loans = loans;
        this.loans = new ArrayList<Loan>();
        this.notifications = notifications;
    }

    public Customer(String email, String firstName, String lastName, String address, String password, ArrayList<String> securityQuestions){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.accountList = new ArrayList<PaymentAccount>();
        this.contactList = new ArrayList<Contact>();
        this.notifications = new LinkedHashMap<LocalDateTime, String>();
        this.loans = new ArrayList<Loan>();
    } // creation constructor

    public Customer(){
        this.contactList = new ArrayList<Contact>();
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
    public PaymentAccount getAccountWithId(int id){
        for(PaymentAccount account: this.accountList){
            if(account.getId() == id){
                return account;
            }
        }
        return null;
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
    public void addContact(Contact contact){
        if(this.contactList == null){
            this.contactList = new ArrayList<Contact>();
        }
        this.contactList.add(contact);
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
    public void setLastLoginAt(LocalDateTime lastLoginAt){
        this.lastLoginAt = lastLoginAt;
    }
    public LinkedHashMap<LocalDateTime, String> getNotifications(){
        return this.notifications;
    }
    public void setNotifications(LinkedHashMap<LocalDateTime, String> notifications){
        this.notifications = notifications;
    }
    public ArrayList<Loan> getLoans(){
        return this.loans;
    }
    public void setLoans(ArrayList<Loan> loans){
        this.loans = loans;
    }
    public void addAccount(PaymentAccount account){
        if(account.getInterestRate() == 0.0){
            this.accountList.add(account);
        }else{
            this.accountList.add((SavingsAccount)account);
        }
    }
    public ArrayList<Loan> getLoansWithAccountId(int id){
        ArrayList<Loan> neededLoans = new ArrayList<Loan>();
        if(this.loans == null){
            this.loans = new ArrayList<Loan>();
        }
        for(Loan loan: this.loans){
            PaymentAccount acc = loan.getAccount();
            if(acc != null){
                if(loan.getAccount().getId() == id){
                    neededLoans.add(loan);
                }
            }
        }
        return neededLoans;
    }
    
}
