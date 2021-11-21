package crushers.model;

import java.util.ArrayList;

public class Customer extends User{

    private ArrayList<StandardAccount> accountList;
    private ArrayList<Contact> contactList;

    public Customer(String firstName, String lastName, String address, String email, String password,
    ArrayList<String> securityQuestions, String ID){
        super(firstName, lastName, address, email, password, securityQuestions, ID);
        this.accountList = new ArrayList<StandardAccount>();
        this.contactList = new ArrayList<Contact>();

    }
    
}
