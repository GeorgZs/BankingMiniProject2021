package crushers.common.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import crushers.common.utils.Storable;

public class User implements Storable {
    private int id = -1;
    private String type = "INVALID"; // clerk / manager (clerk) / customer
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String password;
    private LocalDateTime lastLoginAt;

    // Clerks only
    @JsonIgnoreProperties(value = {"manager"}, allowSetters = true)
    private Bank worksAt;

    public User() {
        // empty for Jackson
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailAddress) {
        this.email = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime timeStamp) {
        this.lastLoginAt = timeStamp;
    }


    // clerks only
    public Bank getWorksAt() {
        return worksAt;
    }

    public void setWorksAt(Bank worksAt) {
        this.worksAt = worksAt;
    }


    @JsonIgnore
    public boolean isCustomer() {
        return this.type.equals("customer");
    }

    @JsonIgnore
    public boolean isManager() {
        return this.type.equals("manager");
    }

    @JsonIgnore
    public boolean isClerk() {
        return this.type.equals("clerk") || isManager();
    }

    @JsonIgnore
    public boolean isInvalidType() {
        return type.equals("INVALID");
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
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + email + ")";
    }
}
