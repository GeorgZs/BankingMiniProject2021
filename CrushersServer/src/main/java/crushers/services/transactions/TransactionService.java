package crushers.services.transactions;

import crushers.common.httpExceptions.BadRequestException;
import crushers.common.httpExceptions.ForbiddenException;
import crushers.common.models.*;
import crushers.services.accounts.AccountService;
import crushers.storage.JsonStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransactionService {
    private final JsonTransactionStorage storage;
    private final AccountService accountService;
    private final JsonStorage<Transaction> recurringTransactionStorage;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public TransactionService(AccountService accountService, JsonTransactionStorage storage, JsonStorage<Transaction> recurringTransactionStorage) throws IOException {
        this.storage = storage;
        this.accountService = accountService;
        this.recurringTransactionStorage = recurringTransactionStorage;

        for (Transaction transaction : recurringTransactionStorage.getAll()) {
            startRecurringDeposit(transaction);
        }
    }

    /**
     * @param transaction to be created
     * @param user logged-in who is creating the transaction
     * @return the Transaction given it passes all tests
     * @throws Exception if the Transaction data is invalid:
     * if user is instance of a Customer:
     * checks if the Account to and from are equal or if accounts are null
     * checks the null accounts based on whether customer is receiving from the bank (account from is null)
     * or if the customer is repaying a loan (account to is null) - otherwise checks if both accounts exist
     * if user is instance of a Clerk:
     * check if the account from is null (null = from the bank) as the Clerk sending money sends money from the bank to a customer
     * checks if the account to is null (null = to the bank) as this means the Bank is receiving money from a Customer
     *
     * After this it checks the transaction contains valid data such as Description missing or transaction amount being less than or equal to 0
     * then completes its Automated Suspicious Transaction Checker (trademark pending) to see whether the Transaction is suspicious or not
     */
    public Transaction create(Transaction transaction, User user) throws Exception {
        List<String> invalidDataMessage = new ArrayList<>();
        //Cannot continue if the transaction passed is null
        if (transaction == null) {
            throw new BadRequestException("Transaction invalid!");
        }

        //check if user creating transaction is a Customer
        if (user.isCustomer()) {
            //this if statement is for getting the interest rate
            //as it comes from the bank and so the accountFrom is null
            if(transaction.getFrom() != null){
                //Checks if the account to and from are the same as you cannot create
                //a Transaction that goes between the same accounts
                if (transaction.getFrom().equals(transaction.getTo())) {
                    throw new BadRequestException("Accounts to and from are the same");
                }

                //Checks if the account from exists and belongs to the customer
                accountService.get(user, transaction.getFrom().getId());

                if (!accountService.exists(transaction.getTo().getId())) {
                    invalidDataMessage.add("Account does not exist for target customer");
                }
            }
        
        } //check if user creating transaction is a Clerk
        else if (user.isClerk()) {
            if (transaction.getFrom() != null) { // withdraw
                if (!accountService.exists(transaction.getFrom().getId())) {
                    invalidDataMessage.add("Receiving account does not exist for target customer");
                }
            }
            else if (transaction.getTo() != null) { // deposit
                if (!accountService.exists(transaction.getTo().getId())) {
                    invalidDataMessage.add("Sending account does not exist for target customer");
                }
            }
        }

        //checks the transaction's amount which must not be less than or equal 0 as that does qualify as a valid transaction
        // banks are allowed to create transactions with a negative amount for loan interests
        if (transaction.getFrom() != null && transaction.getAmount() <= 0) {
            invalidDataMessage.add("Transaction amount must be greater than 0");
        }

        //Throws exceptions accumulated in invalidDataMessage arraylist
        if (!invalidDataMessage.isEmpty()) {
            throw new BadRequestException(String.join("\n", invalidDataMessage));
        }

        // fallback if no date got set
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDate.now());
        }

        //Checking the transactions field not for invalid data
        //but for suspicious data --> automatic marking of suspicious transactions

        /*
    - amount > 5000
    - transactions from this account in last 24h > 5
    - amount < 0.50
    - description contains "nigerian prince"
    - feel free to add some other BS rules lmao

    maybe send a notification to the user?
    can do a switch case if too large
         */
        /*
         * If statements below demonstrate the System marking transaction suspicious based on parameters set upon deployment
         * Amount: when greater than 5000 or less than 0.50 the system adds this transaction to a list of suspicious transactions
         * Description: when the description contains nigerian prince or nigerian princess the system also marks it as suspicious
         */
        /*
         * Checks recurring transaction's interval to see if transaction happens too frequently
         * which may imply a suspicious transaction
         */

        if (transaction.getAmount() > 5_000 || transaction.getAmount() < 0.5) {
            storage.addSusTransaction(transaction);
        }

        else if(transaction.getDescription().toLowerCase().contains("nigerian")
                || transaction.getDescription().toLowerCase().contains("prince")
                || transaction.getDescription().toLowerCase().contains("king")
                || transaction.getDescription().toLowerCase().contains("queen")){
            storage.addSusTransaction(transaction);
        }

        else if(transaction.getAmount() == 419 || transaction.getAmount() == 421 ||transaction.getAmount() == 68){
            storage.addSusTransaction(transaction);
        }

        if (transaction.isRecurring()){
            //check suspicious transaction for recurring
            // interval being counted in days in this instance (less than 7 day interval == sus)
            if(transaction.getInterval() < 7){
                storage.addSusTransaction(transaction);
            }
        }
        //sends transaction through the commit method which in turn withdraws money from both accounts (if applicable)
        accountService.commit(transaction);
        return storage.create(transaction);
    }

    /**
     * @param loggedInUser
     * @param id of the requested Transaction
     * @return the Transaction with ID specified in method signature
     * @throws Exception if the Transaction is null, meaning the transaction could not be found
     * or if, based on which instance of User is logged in, has access to this transaction
     */
    public Transaction get(User loggedInUser, int id) throws Exception {
        Transaction transaction = storage.get(id);

        if (transaction == null) {
            throw new ForbiddenException();
        }

        //check if user creating transaction is a Customer
        if (loggedInUser.isCustomer()) {
            if (!transaction.getFrom().getOwner().equals(loggedInUser) && !transaction.getTo().getOwner().equals(loggedInUser)) {
                throw new ForbiddenException();
            }
        }

        //check if user creating transaction is a Clerk
        if (loggedInUser.isClerk()) {
            if (!transaction.getFrom().getBank().equals(loggedInUser.getWorksAt()) && !transaction.getTo().getBank().equals(loggedInUser.getWorksAt())) {
                throw new ForbiddenException();
            }
        }

        return transaction;
    }

    /**
     * @param loggedInUser
     * @param accountId of the Account
     * @return the Collection of Transactions for the Account with ID specified in mehtod signature
     * @throws Exception
     */
    public Collection<Transaction> getAllOfAccount(User loggedInUser, int accountId) throws Exception {
        final BankAccount account = accountService.get(loggedInUser, accountId);
        return storage.getAllOfAccount(account);
    }

    /**
     * @param clerk who is logged in
     * @param transactionId of the Transaction being marked under suspicion of logged-in Clerk
     * @return the Transaction that was marked as suspicious
     * @throws Exception
     */
    public Transaction markSusTransaction(User clerk, int transactionId) throws Exception {
        Transaction transaction = storage.get(transactionId);
        return storage.addSusTransaction(transaction);
    }

    /**
     * @param customer who is logged in
     * @param id of the Account which should receive the Interest
     * @return a Transaction with the interest amount
     * @throws Exception if the account specified is not a Savings Account -- only Savings Accounts can receive Interest
     */
    public Transaction getInterest(User customer, int id) throws Exception {
        BankAccount account = accountService.get(customer, id);
        if (account.isSavings() || account.isLoan()) {
            //interest calculations: A = P(1 + rt) -> I = A - P                       1 here means after 1 year: 1/2 half year, 1/12 monthly
            double interestAmount = account.getBalance() * ((account.getInterestRate() * 1));
            //Transaction: from bank, to customer, amount interestCalculation, description: "yearly interest rate"
            Transaction transaction = new Transaction();
            transaction.setFrom(null);
            transaction.setTo(account);
            transaction.setAmount(interestAmount);
            transaction.setDescription("Yearly Interest Rate based on account balance");
            return create(transaction, customer);
        } else {
            throw new BadRequestException(String.join("\n", "Not a savings account - cannot receive interest"));
        }
    }

    /**
     * @param clerk who is logged-in
     * @return the Collection of Suspicious Transactions
     * @throws Exception
     */
    public Collection<Transaction> getAllSuspiciousTransactions(Bank bank) throws Exception {
        return storage.getSuspiciousTransactions(bank);
    }

    /**
     * @param customer who is logged-in
     * @param recurringTransaction which should be created based on the interval given
     * @return the recurring Transaction which is being created on the interval
     * @throws Exception
     */
    public Transaction createRecurringDeposit(User customer, Transaction recurringTransaction) throws Exception {
        //Checks if the account to and from are the same as you cannot create
        //a Transaction that goes between the same accounts
        if (recurringTransaction.getFrom().equals(recurringTransaction.getTo())) {
            throw new BadRequestException("Accounts to and from are the same");
        }

        if (recurringTransaction.getFrom() == null) {
            throw new BadRequestException("Accounts from must be set");
        }

        //Gets the account from the storage based on the ID of the Account which sent the Transaction
        BankAccount customerAccount = accountService.get(customer, recurringTransaction.getFrom().getId());

        if (!accountService.exists(recurringTransaction.getTo().getId())) {
            throw new BadRequestException("Account does not exist for target customer");
        }

        boolean isTransfer = false;

        try {
            // try to get your own account with the same id, throws if it doesn't exist or belongs to someone else
            accountService.get(customer, recurringTransaction.getTo().getId());
            isTransfer = true;
        } 
        catch (ForbiddenException ex) {
            isTransfer = false;
        }

        if (customerAccount.isSavings() && !isTransfer) {
            throw new BadRequestException(
                    "Cannot create Transaction: Account with id: " + customerAccount.getId() + " cannot pay with a savings account, only transfer to other accounts you own"
            );
        }

        if (recurringTransaction.getInterval() < 1) {
            throw new BadRequestException(String.join("\n", "Interval must be greater than 0"));
        }

        Transaction createdTransaction = recurringTransactionStorage.create(recurringTransaction);
        startRecurringDeposit(recurringTransaction);
        return createdTransaction;
    }

    public Transaction setLabels(User loggedInUser, int transactionId, TransactionLabels labels) throws Exception {
        Transaction transaction = get(loggedInUser, transactionId);
        transaction.setLabels(labels.getLabels());
        storage.update(transactionId, transaction);
        return transaction;
    }

    private void startRecurringDeposit(Transaction recurringTransaction) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    create(recurringTransaction, recurringTransaction.getFrom().getOwner());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // a day is 2 seconds for testing purposes
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, recurringTransaction.getInterval() * 2, TimeUnit.SECONDS);
    }
}
