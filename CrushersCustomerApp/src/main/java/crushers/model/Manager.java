package crushers.model;

import java.util.ArrayList;

public class Manager extends User{

    public Manager(String firstName, String lastName, String address, String email, String password, ArrayList<String> securityQuestions, Bank bank){
        super(firstName, lastName, address, email, password, securityQuestions);
    }
}
