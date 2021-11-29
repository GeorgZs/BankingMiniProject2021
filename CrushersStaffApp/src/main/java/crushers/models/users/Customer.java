package crushers.models.users;
import crushers.models.User;

public class Customer extends User {
    // Later, not now
    // private List<StandardAccount> accountList;
    // private LinkedHashMap<Integer,Contact> contactList;

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

}
