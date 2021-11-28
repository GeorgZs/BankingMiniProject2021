package crushers.services.transactions;

import crushers.models.exchangeInformation.Transaction;
import crushers.models.users.User;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonTransactionStorage extends JsonStorage<Transaction> {

    private final Map<User, Collection<Transaction>> transactionStorage;

    public JsonTransactionStorage(File jsonFile) throws IOException {
        super(jsonFile, Transaction.class);
        this.transactionStorage = new HashMap<>();
    }


}
