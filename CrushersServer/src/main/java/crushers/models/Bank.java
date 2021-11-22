package crushers.models;

import crushers.models.users.Manager;
import crushers.storage.Storable;

public class Bank implements Storable {
    private int id;
    private String name;
    private String logo;
    private String details;
    private Manager manager;

    public Bank() {
        // empty constructor for Jackson
    }

    public Bank(String name, String logo, String details, Manager manager) {
        this.id = (int) (Math.random() * 10);
        this.name = name;
        this.logo = logo;
        this.details = details;
        this.manager = manager;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
