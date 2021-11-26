package crushers.services.banks;

import crushers.models.Bank;
import crushers.storage.JsonStorage;

import java.io.File;
import java.util.*;

public class JsonBankStorage extends JsonStorage<Bank> {
    private final Collection<Bank> bankStorage;

    public JsonBankStorage(File jsonStorage) throws Exception {
        super(jsonStorage, Bank.class);
        this.bankStorage = new ArrayList<>();
        for(Bank bank : this.data.values()){
            addToMap(bank);
        }
    }

    protected void addToMap(Bank bank){
        if(!bankStorage.contains(bank)){
            bankStorage.add(bank);
        }

        bankStorage.add(bank);
    }
}
