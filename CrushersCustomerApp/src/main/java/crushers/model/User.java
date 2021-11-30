package crushers.model;

import java.util.ArrayList;

public class User{
    
    protected String firstName;
    protected String lastName;
    protected String address;
    protected String email;
    protected String password;
    protected ArrayList<String> securityQuestions;
    
    public User(String firstName, String lastName, String address, String email, String password, ArrayList<String> securityQuestions){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.securityQuestions = securityQuestions;
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
    public void setPassword(String password){
        this.password = password;
    }

    public String toString(){
        String result = this.firstName + " " + this.lastName + " lives at " + this.address + "\n" + this.email + " " + this.password +
        "\n" + securityQuestions.toString();
        return result;
    }
}
