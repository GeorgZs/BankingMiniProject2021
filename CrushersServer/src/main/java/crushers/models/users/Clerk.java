package crushers.models.users;

import java.util.List;

public class Clerk extends User {
    private String employmentBank;
    public Clerk(String firstName, String lastName,
                 String address, String emailAddress,
                 String password, List<String> securityQuestions,
                 String employmentBank){
        super(firstName, lastName, address,
              emailAddress, password, securityQuestions);
        this.employmentBank = employmentBank;
    }

}
