package crushers.model;

import java.util.ArrayList;

public class Customer{

    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private ArrayList<String> securityQuestions;
    private int id;

    private ArrayList<PaymentAccount> accountList;
    private ArrayList<Contact> contactList;


    public Customer(String firstName, String lastName, String address, String email, String password,
    ArrayList<String> securityQuestions, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.id = id;

        this.accountList = new ArrayList<PaymentAccount>();
        this.contactList = new ArrayList<Contact>();
    }
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
    }

    public void resetPassword(){
        //User must input their current password.
        //If entered password matches current password, user may create a new password

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
    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<String> getSecurityQuestions(){
        return this.securityQuestions;
    }
    public int getId(){
        return this.id;
    }

    public String toString(){
        String result = this.firstName + " " + this.lastName + " lives at " + this.address + "\n" + this.email + " " + this.password +
        "\n" + securityQuestions.toString() + "\nAccounts:";
        for(PaymentAccount account: this.accountList){
            result += account;
        }
        return result;
    }
    public ArrayList<PaymentAccount> getAccountList(){
        return this.accountList;
    }
}
