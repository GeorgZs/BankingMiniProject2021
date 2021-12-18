package crushers.services.transactions;

import crushers.models.accounts.Account;
import crushers.models.accounts.SavingsAccount;
import crushers.models.exchangeInformation.*;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.models.users.User;

import crushers.server.httpExceptions.BadRequestException;
import crushers.server.httpExceptions.ForbiddenException;
import crushers.services.accounts.AccountService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransactionService {
    private final JsonTransactionStorage storage;
    private final AccountService accountService;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public TransactionService(JsonTransactionStorage storage, AccountService accountService) throws IOException {
        this.storage = storage;
        this.accountService = accountService;
    }

    public Transaction create(Transaction transaction, User user) throws Exception {
        List<String> invalidDataMessage = new ArrayList<>();
        //Cannot continue if the transaction passed is null
        if (transaction == null) {
            throw new BadRequestException("Transaction invalid!");
        }

        //check if user creating transaction is a Customer
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            //this if statement is for getting the interest rate
            //as it comes from the bank and so the accountFrom is null
            if(transaction.getFrom() != null){
                //Checks if the account to and from are the same as you cannot create
                //a Transaction that goes between the same accounts
                if (transaction.getFrom().equals(transaction.getTo())) {
                    throw new BadRequestException("Accounts to and from are the same");
                }

                //Gets the account from the storage based on the ID of the Account which sent the Transaction
                Account customerAccount = accountService.get(customer, transaction.getFrom().getId());

                //Checks whether the customer's account is null = in this case the account doesn't exist in the system
                if (customerAccount == null) {
                    invalidDataMessage.add("Account does not exist for selected customer");
                }

                //This check is supposed to block customers from reducing their bank accounts to a zero value
                //as this goes against company policy -- if the account is requested to be deleted it must be done
                //by a Clerk or a Manager
                if (customerAccount.getBalance() <= transaction.getAmount()) {
                    throw new BadRequestException(
                            "Cannot create Transaction: Account with id: " + customerAccount.getId() + " does not have enough funds to create this Transaction"
                    );
                }
                //Checking if the loans are empty allows us to make sure that there aren't issues when paying back
                //the loan (the account to must be null if a loan is being repaid)
                if(customer.getLoans().isEmpty()){
                    if (!accountService.exists(transaction.getTo().getId())) {
                    //reason being is that the transaction goes from account to NULL account (Bank)
                    //paying back the bank requires us to break the pre-existing checker for accounts
                        invalidDataMessage.add("Account does not exist for target customer");
                    }
                }
            }
        //check if user creating transaction is a Customer
        } else if (user instanceof Clerk) {
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
        if (transaction.getAmount() <= 0) {
            invalidDataMessage.add("Transaction amount must be greater than 0");
        }

        //checks the transaction's description to see if its empty or blank which is not allowed
        if (transaction.getDescription().isBlank() || transaction.getDescription().isEmpty()) {
            invalidDataMessage.add("Transaction description cannot be empty or blank");
        }

        //Throws exceptions accumulated in invalidDataMessage arraylist
        if (!invalidDataMessage.isEmpty()) {
            throw new BadRequestException(String.join("\n", invalidDataMessage));
        }

        // fallback if no date got set
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
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

        if (transaction.getAmount() > 5_000 || transaction.getAmount() < 0.5 || transaction.getDescription() == "nigerian prince") {
            storage.addSusTransaction(transaction);
        }

        if(transaction instanceof RecurringTransaction){
            RecurringTransaction recurringTransaction = (RecurringTransaction) transaction;
            //check suspicious transaction for recurring
            // interval being counted in days in this instance (less than 7 day interval == sus)
            if(recurringTransaction.getInterval() < 7){
                storage.addSusTransaction(recurringTransaction);
            }
        }
        //sends transaction through the commit method which in turn withdraws money from both accounts (if applicable)
        accountService.commit(transaction);
        return storage.create(transaction);
    }

    public Transaction get(User loggedInUser, int id) throws Exception {
        Transaction transaction = storage.get(id);

        if (transaction == null) {
            throw new ForbiddenException();
        }

        //check if user creating transaction is a Customer
        if (loggedInUser instanceof Customer) {
            Customer customer = (Customer) loggedInUser;
            if (!transaction.getFrom().getOwner().equals(customer) && !transaction.getTo().getOwner().equals(customer)) {
                throw new ForbiddenException();
            }
        }

        //check if user creating transaction is a Clerk
        if (loggedInUser instanceof Clerk) {
            Clerk clerk = (Clerk) loggedInUser;
            if (!transaction.getFrom().getBank().equals(clerk.getWorksAt()) && !transaction.getTo().getBank().equals(clerk.getWorksAt())) {
                throw new ForbiddenException();
            }
        }

        return transaction;
    }

    public Collection<Transaction> getAllOfAccount(User loggedInUser, int accountId) throws Exception {
        final Account account = accountService.get(loggedInUser, accountId);
        return storage.getAllOfAccount(account);
    }

    public Transaction markSusTransaction(Clerk clerk, int transactionId) throws Exception {
        Transaction transaction = storage.get(transactionId);
        return storage.addSusTransaction(transaction);
    }

    public Transaction getInterest(Customer customer, int id) throws Exception {
        Account account = accountService.get(customer, id);
        if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            if(savingsAccount.getBalance() <= 500.00){
                throw new BadRequestException(String.join("\n", "Cannot receive interest - account doesnt meet requirements: balance too low"));
            }
            //interest calculations: A = P(1 + rt) -> I = A - P                       1 here means after 1 year: 1/2 half year, 1/12 monthly
            double placeHolder = savingsAccount.getBalance() * (1 + (savingsAccount.getInterestRate() * 1));
            double interestAmount = placeHolder - savingsAccount.getBalance();
            //Transaction: from bank, to customer, amount interestCalculation, description: "yearly interest rate"
            Transaction transaction = new Transaction(null, accountService.get(customer, id),
                                    interestAmount, "Yearly Interest Rate based on account balance");
                                    // apparently when creating this transaction it does not work bcs the
                                    // the description is BlAnK or eMpTy -- fucker
            return create(transaction, customer);
        } else {
            throw new BadRequestException(String.join("\n", "Not a savings account - cannot receive interest"));
        }
    }

    public Collection<Transaction> getAllSuspiciousTransactions(Clerk clerk) throws Exception {
        return storage.getSuspiciousTransactions();
    }

    public RecurringTransaction createRecurringDeposit(Customer customer, RecurringTransaction recurringTransaction) throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    create(recurringTransaction, customer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, recurringTransaction.getInterval(), TimeUnit.SECONDS);
        return recurringTransaction;
    }



    public Transaction getLoan(Customer loggedIn, Loan loan) throws Exception{
        if(loan.getAmount() > 25000 || loan.getAmount() < 1000){
            throw new BadRequestException("Loan amount must be in excess of 1000kr and no more than 25,000kr");
        }
        loggedIn.addNotification(new ManagerNotification("Loan has been requested - money deposited"));
        Transaction transaction = new Transaction(null, loan.getAccount(), loan.getAmount(), loan.getPurpose());
        transaction.setLabel("Loan");
        loggedIn.addLoans(loan);
        create(transaction, loggedIn);
        return transaction;
    }

    public Loan payBackLoan(Customer loggedIn, Transaction transaction) throws Exception{
        if(!loggedIn.getLoans().isEmpty()){
            for(Loan loan : loggedIn.getLoans()){
                if(transaction.getDescription().equals(loan.getPurpose())){
                    loggedIn.getLoans().remove(loan);
                    double newAmount = loan.getAmount() - transaction.getAmount();
                    loan.setAmount(newAmount);
                    loggedIn.getLoans().add(loan);
                    if(loan.getAmount() >= transaction.getAmount()){
                        create(transaction, loggedIn);
                        return loan;
                    }
                    else{
                        throw new BadRequestException("Transaction amount is greater than Loan amount");
                    }
                }
                else{
                    throw new BadRequestException("Transaction label did not match Loan");
                }
            }
        }
        else {
            throw new BadRequestException("Customer does not have Loans");
        }
        return null;
    }

    public Transaction setLabel(Customer loggedIn, TransactionLabel transactionLabel) throws Exception{
        Transaction transaction = storage.get(transactionLabel.getTransaction().getId());
        storage.delete(transaction.getId());
        transaction.setLabel(transactionLabel.getLabel());
        storage.create(transaction);
        return transaction;
    }
}
