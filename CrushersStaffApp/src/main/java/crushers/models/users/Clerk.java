package crushers.models.users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crushers.models.Bank;
import crushers.models.accounts.*;

public class Clerk extends User {

    protected String staffType;

    @JsonIgnoreProperties({ "details", "manager" })
    private Bank worksAt;

    public Clerk() {
        this.staffType = "clerk"; // empty for Jackson
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
        this.staffType = "clerk";
        this.worksAt = worksAt;
    }

    public int getId() {
        return super.getId();
    }

    public void setId(int id) {
        super.setId(id);
    }

    public Bank getWorksAt() {
        return worksAt;
    }

    public void setWorksAt(Bank worksAt) {
        this.worksAt = worksAt;
    }

    public String getStaffType() {
        return staffType;
    }

    public Account createAccountForCustomer(Customer customer, double balance) {
        PaymentAccount paymentAccount = new PaymentAccount(this.worksAt, customer, balance);
        return paymentAccount;
    }

    public double seeCustomerBalance(int customerID, String accountID) {
        // GET http request returns desired customer
        Customer customer = new Customer(null, null, null, null, null, null);
        PaymentAccount paymentAccount = new PaymentAccount(null, customer, 0.0);
        return paymentAccount.getBalance();
    }
}
