package crushers.services.banks;


import crushers.models.Bank;
import crushers.models.users.Manager;
import crushers.server.httpExceptions.*;
import crushers.services.staff.StaffService;
import crushers.storage.Storage;

import java.util.Collection;

public class BankService {
    private final Storage<Bank> storage;
    private final StaffService staffService;

    public BankService(Storage<Bank> storage, StaffService staffService) {
        this.storage = storage;
        this.staffService = staffService;
    }

    public Bank get(int id) throws Exception {
        Bank bank = storage.get(id);
        if(bank == null){
            throw new NotFoundException("No Bank found with ID: " + id);
        }
        return bank;
    }

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
            Manager manager = new Manager(
                    "test@email.com",
                    "First",
                    "Last",
                    "Lindholmen 10",
                    "HelloWorld",
                    securityQandA,
                    null);
            staffService.create(manager);
            storage.create(new Bank(manager));
        }
        
        return storage.getAll();
    }

    public Bank create(Bank bank) throws Exception {
        if(bank == null){
            throw new BadRequestException("User invalid!");
        }

        Bank createdBank = storage.create(bank);
        staffService.create(createdBank, bank.getManager());
        return createdBank;
    }
}
