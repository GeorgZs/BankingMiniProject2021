package crushers.services.transactions;

import crushers.models.accounts.Account;
import crushers.models.exchangeInformation.Transaction;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.models.users.User;

import crushers.server.httpExceptions.BadRequestException;
import crushers.server.httpExceptions.ForbiddenException;
import crushers.services.accounts.AccountService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransactionService {
    private final JsonTransactionStorage storage;
    private final AccountService accountService;

    public TransactionService(JsonTransactionStorage storage, AccountService accountService) throws IOException {
        this.storage = storage;
        this.accountService = accountService;
    }

    public Transaction create(Transaction transaction, User user) throws Exception {
        List<String> invalidDataMessage = new ArrayList<>();
        if (transaction == null){
            throw new BadRequestException("Transaction invalid!");
        }

        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            
            if (transaction.getFrom().equals(transaction.getTo())) {
                throw new BadRequestException("Accounts to and from are the same");
            }

            Account customerAccount = accountService.get(customer, transaction.getFrom().getId());

            if (customerAccount == null) {
                invalidDataMessage.add("Account does not exist for selected customer");
            }

            if (customerAccount.getBalance() <= transaction.getAmount()) {
                throw new BadRequestException(
                    "Cannot create Transaction: Account with id: " + customerAccount.getId() + " does not have enough funds to create this Transaction"
                );
            }
        }
        else if (user instanceof Clerk) {
            if (transaction.getFrom() != null) {
                invalidDataMessage.add("Account from should be the Bank!");
            }            
        }

        if (!accountService.exists(transaction.getTo().getId())) {
            invalidDataMessage.add("Account does not exist for target customer");
        }

        if (transaction.getAmount() < 0) {
            invalidDataMessage.add("Transaction amount cannot be less than 0");
        }

        if (transaction.getDescription().isBlank() || transaction.getDescription().isEmpty()) {
            invalidDataMessage.add("Transaction description cannot be empty or blank");
        }
        
        if (!invalidDataMessage.isEmpty()) {
            throw new BadRequestException(String.join("\n", invalidDataMessage));
        }

        // fallback if no date got set
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }

        accountService.commit(transaction);
        return storage.create(transaction);
    }

    public Transaction get(User loggedInUser, int id) throws Exception {
        Transaction transaction = storage.get(id);

        if (transaction == null) {
            throw new ForbiddenException();
        }

        if (loggedInUser instanceof Customer) {
            Customer customer = (Customer) loggedInUser;
            if (!transaction.getFrom().getOwner().equals(customer) && !transaction.getTo().getOwner().equals(customer)) {
                throw new ForbiddenException();
            }
        }

        if (loggedInUser instanceof Clerk) {
            Clerk clerk = (Clerk) loggedInUser;
            if (!transaction.getFrom().getBank().equals(clerk.getWorksAt()) && !transaction.getTo().getBank().equals(clerk.getWorksAt())) {
                throw new ForbiddenException();
            }
        }

        return transaction;
    }

    public Collection<Transaction> getAllOfAccount(User loggedInUser, int acccountId) throws Exception {
        final Account account = accountService.get(loggedInUser, acccountId);
        return storage.getAllOfAccount(account);
    }

    public Transaction markSusTransaction(Clerk clerk, int transactionId) throws Exception{
        Transaction transaction = storage.get(transactionId);
        storage.markSusTransaction(transaction);
        return transaction;
    }
}
