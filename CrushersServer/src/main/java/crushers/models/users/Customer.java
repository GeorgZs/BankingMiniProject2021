package crushers.models.users;

import crushers.models.exchangeInformation.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Customer extends User{
    // Later, not now
    // private List<StandardAccount> accountList;
    // private LinkedHashMap<Integer,Contact> contactList;
    private LinkedHashMap<LocalDateTime, String> notification;

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
        this.notification = new LinkedHashMap<>();
    }

    public void addNotification(Notification notification){
        Notification newNotification = new Notification(notification.getNotification());
        //                      its saying that the getter for time is null
        //                      which is weird bcs upon object creation it should set
        //                      the time to LocalDateTime.now()
        this.notification.put(newNotification.getTime(), newNotification.getNotification());
    }

    public LinkedHashMap<LocalDateTime, String> getNotification(){
        return this.notification;
    }

}
