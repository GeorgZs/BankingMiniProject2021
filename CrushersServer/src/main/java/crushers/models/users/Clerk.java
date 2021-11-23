package crushers.models.users;

import crushers.models.Bank;

public class Clerk extends User {
    private Bank worksAt;

    public Clerk(){
        //empty for Jackson
    }

    public Clerk(
        String emailAddress,
        String firstName, 
        String lastName,
        String address, 
        String password, 
        String[] securityQuestions,
        Bank worksAt
    ){
        super(emailAddress, firstName, lastName, address, password, securityQuestions);
        this.worksAt = worksAt;
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }
    
    public Bank getWorksAt() {
        return worksAt;
    }

    public void setWorksAt(Bank worksAt) {
        this.worksAt = worksAt;
    }

}
