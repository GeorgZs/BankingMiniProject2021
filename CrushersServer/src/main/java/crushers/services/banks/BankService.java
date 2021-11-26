package crushers.services.banks;


import crushers.models.Bank;
import crushers.models.users.Manager;
import crushers.server.httpExceptions.*;
import crushers.services.staff.StaffService;
import crushers.storage.Storage;

import java.util.Collection;

public class BankService {
    private final JsonBankStorage storage;
    private final StaffService staffService;

    public BankService(JsonBankStorage storage, StaffService staffService) {
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

    public Bank updateBank(int id, Bank bank) throws Exception {
        storage.update(id, bank);
        return bank;
    }
}
