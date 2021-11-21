package crushers.models.users;

import crushers.storage.Storable;

import java.util.List;

public class Manager extends Clerk implements Storable{
    public Manager(){
        //empty for Jackson
    }
    public Manager(String firstName, String lastName,
                   String address, String emailAddress,
                   String password, List<String> securityQuestions,
                   String employmentBank){
        super(firstName, lastName, address,
              emailAddress, password, securityQuestions,
              employmentBank);
    }

}
