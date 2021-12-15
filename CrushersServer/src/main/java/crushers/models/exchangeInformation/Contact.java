package crushers.models.exchangeInformation;

import crushers.models.accounts.Account;

public class Contact {
    private int id;
    private String name;
    private Account account;
    private String description;

    public Contact(){
        // for jackson -- needs the empty constructor
    }

    public Contact(String name,
                   Account account, String description){
        //if = ID is found in registry && name and account are found in User of that ID
        this.id = 0;
        this.name = name;
        this.account = account;
        this.description = description;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
