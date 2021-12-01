package crushers.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crushers.models.accounts.Account;
import crushers.models.accounts.PaymentAccount;
import crushers.models.exchangeInformation.Transaction;
import crushers.models.users.Customer;
import crushers.utils.HTTPUtils;


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

    public Account createAccountForCustomer(Customer customer) throws Exception {
        // GET http request returns desired customer
        Account account = new PaymentAccount(this.worksAt, customer, 0.00);
        HTTPUtils.post("/accounts", account, Account.class);
        return account;
    }

    public double seeCustomerBalance(int customerID) throws Exception {
        // GET http request returns desired customer
        Account account = HTTPUtils.get("/accounts/" + customerID, Account.class);
        return account.getBalance();
    }

    public Transaction depositToCustomerAccount(Account accountTo, Account accountFrom, String description, double balance) {
        // Give the server these infos
        Transaction transaction = new Transaction(null, accountTo, balance, description);
        return transaction;
    }

    public Transaction withdrawFromCustomerAccount(Account accountTo, Account accountFrom, String description, double balance) {
        Transaction transaction = new Transaction(accountFrom, accountTo, balance, description);
        return transaction;
    }
}
