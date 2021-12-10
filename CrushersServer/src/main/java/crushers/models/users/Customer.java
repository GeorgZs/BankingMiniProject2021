package crushers.models.users;

import crushers.models.exchangeInformation.Loan;
import crushers.models.exchangeInformation.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Customer extends User{
    // Later, not now
    // private List<StandardAccount> accountList;
    // private LinkedHashMap<Integer,Contact> contactList;
    private LinkedHashMap<LocalDateTime, String> notifications = new LinkedHashMap<>();
    private ArrayList<Loan> loans = new ArrayList<Loan>();

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
    }

    public void addNotification(Notification notification){
        notification.setTime(LocalDateTime.now());
        //                      its saying that the getter for time is null
        //                      which is weird bcs upon object creation it should set
        //                      the time to LocalDateTime.now()
        this.notifications.put(notification.getTime(), notification.getNotification());
    }

    public LinkedHashMap<LocalDateTime, String> getNotification(){
        return this.notifications;
    }

    public ArrayList<Loan> getLoans() {
        return loans;
    }

    public void addLoans(Loan loan){
        this.loans.add(loan);
    }
}
