package crushers.models.users;

import crushers.models.exchangeInformation.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Customer extends User{
    // Later, not now
    // private List<StandardAccount> accountList;
    // private LinkedHashMap<Integer,Contact> contactList;
    private LinkedList<Notification> notification;

    public Customer() {
        // empty constructor for Jackson
    }

    public Customer(
        String emailAddress,
        String firstName, 
        String lastName,
        String address, 
        String password, 
        String[] securityQuestions
    ){
        super(emailAddress, firstName, lastName, address, password, securityQuestions);
        // this.accountList = new ArrayList<>();
        // this.contactList = new LinkedHashMap<>();
        this.notification = new LinkedList<>();
    }

    public void addNotification(Notification notification){
        this.notification.add(notification);
    }

}
