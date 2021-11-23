package crushers;

import java.util.List;

public class Clerk extends User {

    private Bank employmentBank;

    public Clerk() {
        //empty for Jackson
    }

    public Clerk(String firstName, String lastName, String address, String emailAddress, String password, List<String> securityQuestions, Bank employmentBank) {
        super(firstName, lastName, address, emailAddress, password, securityQuestions);
        this.employmentBank = employmentBank;
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }
}