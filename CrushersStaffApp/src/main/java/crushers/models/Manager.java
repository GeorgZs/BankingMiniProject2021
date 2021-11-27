<<<<<<< HEAD:CrushersStaffApp/src/main/java/crushers/models/users/Manager.java
package crushers.models.users;

import crushers.models.Bank;
=======
package crushers.models;

import crushers.models.Bank;
import crushers.models.Clerk;
>>>>>>> 6c000bc38e2f35a8114287d5c077d0c757c2cc04:CrushersStaffApp/src/main/java/crushers/models/Manager.java

public class Manager extends Clerk {

    public Manager(){
        //empty for Jackson
        this.staffType = "manager";
    }

    public Manager(String emailAddress, String firstName, String lastName, String address, String password, String[] securityQuestions, Bank worksAt) {
        super(emailAddress, firstName, lastName, address, password, securityQuestions, worksAt);
        this.staffType = "manager";
    }
}
