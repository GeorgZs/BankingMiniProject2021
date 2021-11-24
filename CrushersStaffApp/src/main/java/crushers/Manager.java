package crushers;

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
