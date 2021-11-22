package crushers.models.users;

import crushers.models.Bank;

public class Manager extends Clerk {
    public Manager(){
        //empty for Jackson
    }
    public Manager(
        String emailAddress,
        String firstName, 
        String lastName,
        String address, 
        String password, 
        String[] securityQuestions,
        Bank worksAt
    ) {
        super(emailAddress, firstName, lastName, address, password, securityQuestions, worksAt);
    }

}
