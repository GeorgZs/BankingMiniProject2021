package crushers.model;

import java.util.ArrayList;
import java.util.Date;

public class User {
    
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private ArrayList<String> securityQuestions;
    private String ID;
    private Date lastLogin;

    public User(String firstName, String lastName, String address, String email, String password,
    ArrayList<String> securityQuestions, String ID){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.ID = ID;
        this.lastLogin = new Date(); // intialize to account creation and update l8r
    }
    public String toString(){
        return this.firstName + " " + this.lastName + " lives at " + this.address + "\n" + this.email + " " + this.password;
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
    public ArrayList<String> getSecurityQuestions(){
        return this.securityQuestions;
    }
    public String getID(){
        return this.ID;
    }
    public Date getLastLogin(){
        return this.lastLogin;
    }

}
