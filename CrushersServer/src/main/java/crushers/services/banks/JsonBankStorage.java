package crushers.services.banks;

import crushers.models.Bank;
import crushers.server.httpExceptions.BadRequestException;
import crushers.storage.JsonStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonBankStorage extends JsonStorage<Bank> {

    private final Map<Integer, Bank> bankMap;

    public JsonBankStorage(File jsonStorage) throws Exception {
        super(jsonStorage, Bank.class);
        this.bankMap = new HashMap<>();

        for(Bank bank : this.data.values()){
            addToMap(bank);
        }
    }

    /**
     * @param bank being created
     * @return the bank after it passes each of the tests
     * @throws Exception if the manager is null or the manager's details are empty
     * indicating that there are missing fields in the manager
     */
    public Bank create(Bank bank) throws Exception{
        if(bank.getManager() == null){
            throw new BadRequestException("Manager invalid");
        }
        else if(bank.getManager().getFirstName().isBlank() || bank.getManager().getFirstName().isEmpty()){
            throw new BadRequestException("Manager name invalid");
        }
        else if(bank.getManager().getAddress().isBlank() || bank.getManager().getAddress().isEmpty()){
            throw new BadRequestException("Manager name invalid");
        }
        else{
            super.create(bank);
            return bank;
        }
    }

    /**
     * @param bank to be added to the map
     * places the bank in the map with the key being the bank's ID
     */
    protected void addToMap(Bank bank) {
        if (!bankMap.containsKey(bank.getId())) {
            bankMap.put(bank.getId(), bank);
        }
    }
}
