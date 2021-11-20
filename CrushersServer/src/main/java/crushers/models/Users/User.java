package crushers.models.Users;

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

    public User(String firstName, String lastName,
                    String address, String emailAddress,
                    String password, List<String> securityQuestions){
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
}
