package crushers.services.transactions;

import crushers.models.exchangeInformation.Transaction;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.models.users.User;
import crushers.server.Authenticator;
import crushers.server.httpExceptions.BadRequestException;
import crushers.services.accounts.AccountService;
import crushers.services.customers.CustomerService;
import crushers.services.staff.JsonClerkStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private final JsonTransactionStorage storage;
    private final AccountService accountService;
    public int ID = 1;


    public TransactionService(JsonTransactionStorage storage, AccountService accountService) throws IOException {
        this.storage = storage;
        this.accountService = accountService;
    }

    public Transaction create(User user, User target, Transaction transaction) throws Exception {
        List<String> invalidDataMessage = new ArrayList<>();
        if(transaction == null){
            throw new BadRequestException("Staff Member invalid!");
        }
        if(user instanceof Customer){
            Customer accountTo = (Customer) target;
            Customer customer = (Customer) user;
            if(!transaction.getAccountFrom().equals(accountService.getOfCustomer(customer))){
                invalidDataMessage.add("Account does not exist for selected customer");
            }
            if(!transaction.getAccountTo().equals(accountService.getOfCustomer(accountTo))){
                invalidDataMessage.add("Account does not exist for target customer");
            }
        }
        if(user instanceof Clerk){
            Customer accountTo = (Customer) target;
            Clerk clerk = (Clerk) user;
            //null bcs it's from the bank
            //1st case:   Bank -> Account
            if(transaction.getAccountFrom() != null){
                invalidDataMessage.add("Account does not exist for selected customer");
            }
            if(!transaction.getAccountTo().equals(accountService.getOfCustomer(accountTo))){
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
        /*
    id: int,
    from: AccountId,
    to: AccountId,
    amount: double,
    description: String,
    date: LocalDate,
         */
        return storage.create(transaction);
    }
}
