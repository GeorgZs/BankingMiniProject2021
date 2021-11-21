package crushers.models.exchangeInformation;

import crushers.models.accounts.StandardAccount;

public class Contact {
    private int ID;
    private String name;
    private StandardAccount account;
    private String description;

    public Contact(int ID, String name,
                   StandardAccount account, String description){
        //if = ID is found in registry && name and account are found in User of that ID
        this.ID = ID;
        this.name = name;
        this.account = account;
        this.description = description;
    }
}