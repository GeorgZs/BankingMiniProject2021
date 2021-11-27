package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

public class Contact {
    private int ID;
    private String name;
    private Account account;
    private String description;

    public Contact(int ID, String name,
                   Account account, String description){
        //if = ID is found in registry && name and account are found in User of that ID
        this.ID = ID;
        this.name = name;
        this.account = account;
        this.description = description;
    }
}
