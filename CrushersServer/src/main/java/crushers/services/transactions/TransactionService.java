package crushers.services.transactions;

import crushers.models.accounts.Account;
import crushers.models.exchangeInformation.Transaction;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.models.users.User;

import crushers.server.httpExceptions.BadRequestException;
import crushers.services.accounts.AccountService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransactionService {
    private final JsonTransactionStorage storage;
    private final AccountService accountService;
    public static int ID = 0;


    public TransactionService(JsonTransactionStorage storage, AccountService accountService) throws IOException {
        this.storage = storage;
        this.accountService = accountService;
    }

    public Transaction create(Transaction transaction, User user) throws Exception {
        List<String> invalidDataMessage = new ArrayList<>();
        if(transaction == null){
            throw new BadRequestException("Transaction invalid!");
        }
        if(user instanceof Customer){
            Customer customer = (Customer) user;
            if(transaction.getAccountToID().equals(transaction.getAccountFromID())){
                throw new BadRequestException("Accounts to and from are the Same");
            }
            if(!accountService.getIDs(customer).contains(transaction.getAccountFromID())){
                invalidDataMessage.add("Account does not exist for selected customer");
            }
            if(!accountService.getAllIDs().contains(transaction.getAccountToID())){
                invalidDataMessage.add("Account does not exist for target customer");
            }
            Account account = accountService.get(customer, transaction.getAccountFromID());
            if(account.getBalance() <= 0){
                invalidDataMessage.add("Cannot create Transaction: Account with id: "
                        + account.getId() + " does not have enough funds to create this Transaction");
                throw new BadRequestException(String.join("\n", invalidDataMessage));
            }
            account.setBalance(account.getBalance() - transaction.getAmount());
        }
        if(user instanceof Clerk){
            //null bcs it's from the bank
            //1st case:   Bank -> Account
            if(transaction.getAccountFromID() != null){
                invalidDataMessage.add("Account from should be the Bank!");
            }
            if(!accountService.getAllIDs().contains(transaction.getAccountToID())){
                invalidDataMessage.add("Account does not exist for target customer");
            }
        }

        if(transaction.getAmount() < 0){
            invalidDataMessage.add("Transaction amount cannot be less than 0");
        }
        if(transaction.getDescription().isBlank() || transaction.getDescription().isEmpty()){
            invalidDataMessage.add("Transaction description cannot be empty or blank");
        }
        if (!invalidDataMessage.isEmpty()) {
            throw new BadRequestException(String.join("\n", invalidDataMessage));
        }
        transaction.setDate(LocalDateTime.now().toString());
        transaction.setId(ID++);
        //Account accountTo = accountService.get(transaction.getAccountToID());
        // TODO: have to change the balance of the AccountTo account before transaction is created
        // TODO: create a class that gets the account based on the accountNumber
        //transaction.getAccountTo().setBalance(transaction.getAccountTo().getBalance() + transaction.getAmount());
        return storage.create(transaction);
    }
}
