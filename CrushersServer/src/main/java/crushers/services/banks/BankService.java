package crushers.services.banks;

import crushers.common.httpExceptions.*;
import crushers.common.models.*;
import crushers.storage.JsonStorage;

import java.util.*;

public class BankService {
    private final JsonStorage<Bank> storage;

    public BankService(JsonStorage<Bank> storage) {
        this.storage = storage;
    }

    /**
     * @param id of the Bank
     * @return the Bank with the specified ID
     * @throws Exception if the ID does not match any Bank's ID
     */
    public Bank get(int id) throws Exception {
        Bank bank = storage.get(id);
        if(bank == null) {
            throw new NotFoundException("No Bank found with ID: " + id);
        }
        return bank;
    }

    /**
     * @return the Collection of Banks in the Bank Storage
     * @throws Exception
     */
    public Collection<Bank> getAll() throws Exception {
        return storage.getAll();
    }

    /**
     * @param bank to be created
     * @return the Bank that
     * @throws Exception
     */
    public Bank create(Bank bank) throws Exception {
        if (bank == null){
            throw new BadRequestException("Bank invalid!");
        }
        else if (bank.getName() == null || bank.getName().isEmpty()){
            throw new BadRequestException("Bank name invalid!");
        }

        return storage.create(bank);
    }

    /**
     * @param id of the bank
     * @param bank with updated information
     * @return the updated bank, given there were no exceptions
     * @throws Exception
     */
    public Bank updateBank(int id, Bank bank) throws Exception {
        storage.update(id, bank);
        return bank;
    }
}
