package crushers.model;

import java.util.ArrayList;

public class Customer extends User{

    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private ArrayList<String> securityQuestions;

    private ArrayList<PaymentAccount> accountList;
    private ArrayList<Contact> contactList;


    public Customer(String firstName, String lastName, String address, String email, String password,
    ArrayList<String> securityQuestions) {
        super(firstName, lastName, address, email, password, securityQuestions);
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
    @Override
    public String toString(){
        String result = super.firstName + " " + super.lastName + " lives at " + super.address + "\n" + super.email + " " + super.password +
        "\n" + super.securityQuestions.toString() + "\nAccounts:\n";
        for(PaymentAccount account: this.accountList){
            result += account + "\n";
        }
        return result;
    }

    public ArrayList<PaymentAccount> getAccountList(){
        return this.accountList;
    }
}
