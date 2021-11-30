package crushers.services.banks;

import crushers.models.Bank;
import crushers.storage.JsonStorage;

import java.io.File;

public class JsonBankStorage extends JsonStorage<Bank> {

    public JsonBankStorage(File jsonStorage) throws Exception {
        super(jsonStorage, Bank.class);
    }

    protected void addToMap(Bank bank) {

    }
}
