package crushers.services.transactions;

import crushers.common.models.*;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonTransactionStorage extends JsonStorage<Transaction> {

    private final Map<BankAccount, Set<Transaction>> accountTransactions;
    private final Map<Bank, Set<Transaction>> suspiciousTransactions;

    public JsonTransactionStorage(File jsonFile) throws IOException {
        super(jsonFile, Transaction.class);
        this.accountTransactions = new HashMap<>();
        this.suspiciousTransactions = new HashMap<>();

        for (Transaction transaction : this.data.values()) {
            this.addToMaps(transaction);
        }
    }

    /**
     * @param newObj which is of the Transaction Class
     * @return the Transaction after its creation
     * @throws IOException
     * @throws Exception
     */
    @Override
    public Transaction create(Transaction newObj) throws IOException, Exception {
        final Transaction createdObj = super.create(newObj);
        addToMaps(createdObj);
        return createdObj;
    }

    /**
     * @param id of the Transaction that is being deleted
     * @return the deleted Transaction
     * @throws IOException
     */
    @Override
    public Transaction delete(int id) throws IOException {
        final Transaction deletedObj = super.delete(id);

        if (deletedObj != null) {
            accountTransactions.get(deletedObj.getFrom()).remove(deletedObj);
            accountTransactions.get(deletedObj.getTo()).remove(deletedObj);
        }

        return deletedObj;
    }

    /**
     * @param transaction to be created
     * adds the Transaction to Map based on the data found in the JSON file
     * if the accountTransactions map does not contain the key of the account from and to of the transaction,
     * it puts a new item with key transaction.getFrom()/transaction.getTo()
     * [gets the account who is sending the transaction or the account who is receiving the transaction] and the value is an empty HashSet
     */
    protected void addToMaps(Transaction transaction) {
        if (!accountTransactions.containsKey(transaction.getFrom())) {
            accountTransactions.put(transaction.getFrom(), new HashSet<>());
        }
    
        if (!accountTransactions.containsKey(transaction.getTo())) {
            accountTransactions.put(transaction.getTo(), new HashSet<>());
        }
    
        accountTransactions.get(transaction.getFrom()).add(transaction);
        accountTransactions.get(transaction.getTo()).add(transaction);

        if (transaction.isSuspicious() && transaction.getFrom() != null) {
            if (!suspiciousTransactions.containsKey(transaction.getFrom().getBank())) {
                suspiciousTransactions.put(transaction.getFrom().getBank(), new HashSet<>());
            }

            suspiciousTransactions.get(transaction.getFrom().getBank()).add(transaction);
        }
    }

    /**
     * @param account containing Transactions
     * @return the Set of Transaction for the specified Account
     */
    public Set<Transaction> getAllOfAccount(BankAccount account) {
      return accountTransactions.get(account) == null ? new HashSet<>() : accountTransactions.get(account);
    }

    /**
     * @param transaction which is being marked as suspicious
     * @return the transaction that was marked as suspicious
     */
    public Transaction addSusTransaction(Transaction transaction) {
        transaction.setSuspicious(true);
        return transaction;
    }

    /**
     * @return the Collection of Suspicious Transactions
     */
    public Collection<Transaction> getSuspiciousTransactions(Bank bank) {
        return suspiciousTransactions.get(bank) == null ? new HashSet<>() : suspiciousTransactions.get(bank);
    }
}
