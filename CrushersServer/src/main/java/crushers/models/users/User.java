package crushers.models.users;

import crushers.storage.Storable;

import java.time.LocalDateTime;

public abstract class User implements Storable {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String password;
    private String[] securityQuestions;
    private LocalDateTime lastLoginAt;

    public User() {
        //empty for Jackson
    }

    public User(
        String emailAddress, String firstName,
        String lastName, String address,
        String password, String[] securityQuestions
    ) {
        this.id = -1;
        this.email = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.lastLoginAt = null;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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

    //needs to be here for Jackson but isnt a good solution: not secure
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(String[] securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime timeStamp) {
        this.lastLoginAt = timeStamp;
    }
}
