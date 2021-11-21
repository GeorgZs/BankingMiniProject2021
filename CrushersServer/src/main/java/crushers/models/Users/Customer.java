package crushers.models.Users;

import crushers.models.Accounts.StandardAccount;
import crushers.models.ExchangeInformation.Contact;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Customer extends User{
    private List<StandardAccount> accountList;
    private LinkedHashMap<Integer,Contact> contactList;

    public Customer(String firstName, String lastName,
                    String address, String emailAddress,
                    String password, List<String> securityQuestions){
        super(firstName, lastName, address,
              emailAddress, password, securityQuestions);
        this.accountList = new ArrayList<>();
        this.contactList = new LinkedHashMap<>();
    }

}
