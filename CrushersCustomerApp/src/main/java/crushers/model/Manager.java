package crushers.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Manager extends User{

    private int id;
    private LocalDateTime lastLoginAt;
    private String staffType;

    public Manager(String firstName, String lastName, String address, String email, String password, ArrayList<String> securityQuestions, Bank bank){
        super(firstName, lastName, address, email, password, securityQuestions);
    }
    public Manager(int id, String email, String firstName, String lastName, String address, String password,
     ArrayList<String> securityQuestions, LocalDateTime lastLoginAt, String staffType){
         this.id = id;
         this.email = email;
         this.firstName = firstName;
         this.lastName = lastName;
         this.address = address;
         this.password = password;
         this.securityQuestions = securityQuestions;
         this.lastLoginAt = lastLoginAt;
         this.staffType = staffType;
    }
    public Manager(){

    }
    public int getId(){
        return this.id;
    }
    public LocalDateTime getLastLoginAt(){
        return this.lastLoginAt;
    }
    public String getStaffType(){
        return this.staffType;
    }
}
