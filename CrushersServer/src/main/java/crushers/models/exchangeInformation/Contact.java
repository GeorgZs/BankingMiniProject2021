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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
