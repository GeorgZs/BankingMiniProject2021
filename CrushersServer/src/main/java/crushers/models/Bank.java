package crushers.models;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import crushers.models.users.Manager;
import crushers.storage.Storable;

public class Bank implements Storable {
    private int id;
    private String name;
    private String logo;
    private String details;

    @JsonIgnoreProperties({ "worksAt" })
    private Manager manager;

    public Bank() {
        // empty constructor for Jackson
    }

    public Bank(Manager manager) {
        this.id = (int) (Math.random() * 10);
        this.name = "Empty - Please Fill";
        this.logo = "Empty - Please Fill";
        this.details = "Empty - Please Fill";
        this.manager = manager;
    }

    public String generateAccountNumber() {
        return String.format("SE %d %s %d", this.id, LocalTime.now().toString().split("[.]")[1], (int)(10000 + Math.random() * 90000));
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public Manager getManager() {
        return manager;
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
        Bank other = (Bank) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
