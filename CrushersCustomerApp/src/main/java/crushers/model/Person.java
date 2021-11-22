package crushers.model;

import java.util.List;

public class Person extends User {

    public Person(String firstName, String lastName, String address, String emailAddress, String password, List<String> securityQuestions, int id) {
        super(firstName, lastName, address, emailAddress, password, securityQuestions, id);
    }

    public void openStandardAccount() {


    }
}
