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
        //meant to check if any of the manager fields are missing upon construction
        else if(bank.getManager().getClass().getDeclaredFields().length < 7){
            throw new BadRequestException("Error - one of the Manager's Fields are missing");
        }
        else{
            return bank;
        }
    }
    protected void addToMap(Bank bank) {
        if (!bankMap.containsKey(bank.getId())) {
            bankMap.put(bank.getId(), bank);
        }
    }
}
