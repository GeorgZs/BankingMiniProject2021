package crushers.services.transactions;

import crushers.models.accounts.Account;
import crushers.models.exchangeInformation.Transaction;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonTransactionStorage extends JsonStorage<Transaction> {

    private final Map<Account, Set<Transaction>> accountTransactions;
    private final Collection<Transaction> suspiciousTransactions;

    public JsonTransactionStorage(File jsonFile) throws IOException {
        super(jsonFile, Transaction.class);
        this.accountTransactions = new HashMap<>();
        this.suspiciousTransactions = new HashSet<>();

        for (Transaction transaction : this.data.values()) {
            this.addToMaps(transaction);
        }
    }

    @Override
    public Transaction create(Transaction newObj) throws IOException, Exception {
        final Transaction createdObj = super.create(newObj);
        addToMaps(createdObj);
        return createdObj;
    }

    @Override
    public Transaction delete(int id) throws IOException {
        final Transaction deletedObj = super.delete(id);

        if (deletedObj != null) {
            accountTransactions.get(deletedObj.getFrom()).remove(deletedObj);
            accountTransactions.get(deletedObj.getTo()).remove(deletedObj);
        }

        return deletedObj;
    }

    protected void addToMaps(Transaction transaction) {
        if (!accountTransactions.containsKey(transaction.getFrom())) {
            accountTransactions.put(transaction.getFrom(), new HashSet<>());
        }
    
        if (!accountTransactions.containsKey(transaction.getTo())) {
            accountTransactions.put(transaction.getTo(), new HashSet<>());
        }
    
        accountTransactions.get(transaction.getFrom()).add(transaction);
        accountTransactions.get(transaction.getTo()).add(transaction);
    }

    public Set<Transaction> getAllOfAccount(Account account) {
      return accountTransactions.get(account);
    }

    public Transaction addSusTransaction(Transaction transaction){
        suspiciousTransactions.add(transaction);
        return transaction;
    }
}
