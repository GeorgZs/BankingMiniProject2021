package crushers.model;

import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private List<String> securityQuestions;
    private int id;
    // private int timeStamp;

    public User(String firstName, String lastName, String address, String email, String password, List<String> securityQuestions, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.id = id;
    }

    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getAddress(){
        return this.address;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
    public List<String> getSecurityQuestions(){
        return this.securityQuestions;
    }
    public int getId(){
        return this.id;
    }

    public String toString(){
        return this.firstName + " " + this.lastName + " lives at " + this.address + "\n" + this.email + " " + this.password +
        "\n" + securityQuestions.toString();
    }

}
