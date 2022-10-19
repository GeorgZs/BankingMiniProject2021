package crushers.common.models;

import crushers.common.utils.Storable;

public class Contact implements Storable {
    private int id = -1;
    private User of;
    private String name;
    private BankAccount account;
    private String description;

    public Contact() {
        // empty for Jackson
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOf() {
        return of;
    }

    public void setOf(User of) {
        this.of = of;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contact other = (Contact) obj;
        if (id != other.id)
            return false;
        return true;
    }    
}
