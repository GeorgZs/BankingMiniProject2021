package crushers.model;

import java.util.ArrayList;
import java.util.List;

public class User extends Customer{
    

    public User(String firstName, String lastName, String address, String email, String password, ArrayList<String> securityQuestions, int id) {
        super(firstName, lastName, address, email, password, securityQuestions, id);
    }

}
