package crushers;

import java.util.List;

public class Manager extends Clerk {

    private Bank bank;

    public Manager() {
        //empty for Jackson
    }

    public Manager(String firstName, String lastName, String address, String emailAddress, String password, List<String> securityQuestions, Bank employmentBank) {
        super(firstName, lastName, address, emailAddress, password, securityQuestions, employmentBank);
    }

    public Clerk createClerkAccount(String firstName, String lastName, String address, String emailAddress, String password, List<String> securityQuestions, Bank employmentBank) {
        Clerk clerk = new Clerk(firstName, lastName, address, emailAddress, password, securityQuestions, employmentBank);
        return clerk;
    }

    public void setBankName(String name) {
        bank.setName(name);
    }

    public void setBankLogo(String logo) {
        bank.setLogo(logo);
    }

    public void setBankDetails(String details) {
        bank.setDetails(details);
    }

    public Bank getEmploymentBank() {
        return this.bank;
    }
}