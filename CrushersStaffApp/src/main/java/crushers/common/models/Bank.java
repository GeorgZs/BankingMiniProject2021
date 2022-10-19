package crushers.common.models;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import crushers.common.utils.Storable;

public class Bank implements Storable {
    private int id = -1;
    private String name;
    private String details;

    @JsonIgnoreProperties(value = {"worksAt"}, allowSetters = true)
    private User manager;

    public Bank() {
        // empty for Jackson
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public User getManager() {
        return manager;
    }


    public String generateAccountNumber() {
        return String.format("SE %d %s %d", this.id, LocalTime.now().toString().split("[.]")[1], (int)(10000 + Math.random() * 90000));
    }

    @Override
    public String toString() {
        return name;
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
