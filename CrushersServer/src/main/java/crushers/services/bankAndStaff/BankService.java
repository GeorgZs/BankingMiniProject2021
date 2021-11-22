package crushers.services.bankAndStaff;


import crushers.models.Bank;
import crushers.models.users.Manager;
import crushers.storage.Storage;

import java.util.Collection;

public class BankService {
    private final Storage<Bank> storage;
    private final StaffService staffService;

    public BankService(Storage<Bank> storage, StaffService staffService) {
        this.storage = storage;
        this.staffService = staffService;
    }

    //this method gets the specific bank by its unique
    //id that is generated upon its creation
    public Bank get(int id) throws Exception{
        Bank bank = storage.get(id);
        if(bank == null){
            throw new Exception("No User found with ID: " + id);
        }
        return bank;
    }

    //when http://localhost:8080/banks is first called it uses the function
    //below to get all banks which, if there are no banks, creates a new
    //template bank --> also adds the manager to the list of staff
    public Collection<Bank> getAll() throws Exception {
        if(storage.getAll().isEmpty()){
            System.out.println("Empty Bank storage - creating random bank");
            String[] securityQandA = new String[6];
            securityQandA[0] = "Mother's Maiden name";
            securityQandA[1] = "Georg";
            securityQandA[2] = "Pet cat's name";
            securityQandA[3] = "Georg";
            securityQandA[4] = "Highschool name";
            securityQandA[5] = "Georg";
            Manager manager = new Manager("First",
                    "Last",
                    "Lindholmen 10",
                    "test@email.com",
                    "HelloWorld",
                    securityQandA,
                    null);
            staffService.create(manager);
            storage.create(new Bank(manager));
        }
        return storage.getAll();
    }

    //called to create a bank and this returns the bank
    //created and adds it to the storage
    public Bank create(Bank bank) throws Exception {
        if(bank == null){
            throw new Exception("User invalid!");
        }
        return storage.create(bank);
    }
}
