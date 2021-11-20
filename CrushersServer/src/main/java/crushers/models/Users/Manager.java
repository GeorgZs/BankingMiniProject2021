package crushers.models.Users;

import crushers.models.Users.Clerk;

import java.util.List;

public class Manager extends Clerk {
    public Manager(String firstName, String lastName,
                   String address, String emailAddress,
                   String password, List<String> securityQuestions,
                   String employmentBank){
        super(firstName, lastName, address,
              emailAddress, password, securityQuestions,
              employmentBank);
    }
}
