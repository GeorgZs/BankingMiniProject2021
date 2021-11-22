package crushers.model;

import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String address;
    private String emailAddress;
    private String password;
    private List<String> securityQuestions;
    // private List<standardAccount> accountList;
    private int id;
    // private int timeStamp;

    public User(String firstName, String lastName, String address, String emailAddress, String password, List<String> securityQuestions, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emailAddress = emailAddress;
        this.password = password;
        this.securityQuestions = securityQuestions;
        this.id = id;
    }



}
