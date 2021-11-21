package crushers.services.ducks.bankAndStaff;


import crushers.models.Bank;
import crushers.storage.Storage;

import java.util.Collection;

public class BankService {
    private final Storage<Bank> storage;

    public BankService(Storage<Bank> storage) {
        this.storage = storage;
    }

    public Bank get(int id) throws Exception{
        Bank bank = storage.get(id);
        if(bank == null){
            throw new Exception("No User found with ID: " + id);
        }
        return bank;
    }

    public Collection<Bank> getAll() throws Exception {
        if(storage.getAll().isEmpty()){
            throw new Exception("Empty User storage");
        }
        return storage.getAll();
    }

    public Bank create(Bank bank) throws Exception {
        if(bank == null){
            throw new Exception("User invalid!");
        }
        return storage.create(bank);
    }
}
