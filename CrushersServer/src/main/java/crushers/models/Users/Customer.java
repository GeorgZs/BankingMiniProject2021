package crushers.models.Users;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Customer extends User{
    //private List<Account> accountList;
    //private LinkedHashMap<Contact> contactList;

    public Customer(String firstName, String lastName,
                    String address, String emailAddress,
                    String password, List<String> securityQuestions){
        super(firstName, lastName, address,
              emailAddress, password, securityQuestions);
        //this.accountList = new ArrayList<>();
        //this.contactList = new LinkedHashMap<>();
    }

}
