package crushers.model;

import java.util.ArrayList;

public class Customer extends User{

    private ArrayList<PaymentAccount> accountList;
    private ArrayList<Contact> contactList;

    public Customer(String firstName, String lastName, String address, String email, String password,
    ArrayList<String> securityQuestions, int ID){
        super(firstName, lastName, address, email, password, securityQuestions, ID);
        this.accountList = new ArrayList<PaymentAccount>();
        this.contactList = new ArrayList<Contact>();

    }
    
}
