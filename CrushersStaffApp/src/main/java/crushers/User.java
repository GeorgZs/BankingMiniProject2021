package crushers;

import java.time.LocalTime;
import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String address;
    private String emailAddress;
    private String password;
    private List<String> securityQuestions;
    private int ID;
    private String timeStamp;

    public User(){
        //empty for Jackson
    }
    public User(String firstName, String lastName, String address, String emailAddress, String password, List<String> securityQuestions){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emailAddress = emailAddress;
        this.password = password;
        this.securityQuestions = securityQuestions;
        //ID subject to change depending on how we want the IDs to be created
        this.ID = (int) (Math.random() * 100);
        this.timeStamp = LocalTime.now().toString();
    }

    public int getId() {
        return this.ID;
    }

    public void setId(int id) {
        this.ID = id;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    //needs to be here for Jackson but isnt a good solution: not secure
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(List<String> securityQuestions) {
        this.securityQuestions = securityQuestions;
    }


}