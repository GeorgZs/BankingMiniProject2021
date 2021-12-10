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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransactionService {
    private final JsonTransactionStorage storage;
    private final AccountService accountService;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public TransactionService(JsonTransactionStorage storage, AccountService accountService) throws IOException {
        this.storage = storage;
        this.accountService = accountService;
    }

    public Transaction create(Transaction transaction, User user) throws Exception {
        List<String> invalidDataMessage = new ArrayList<>();
        if (transaction == null) {
            throw new BadRequestException("Transaction invalid!");
        }

        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            //this if statement is for getting the interest rate
            //as it comes from the bank and so the accountFrom is null
            if(transaction.getFrom() != null){
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
        } else if (user instanceof Clerk) {
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
        loggedIn.addNotification(new Notification("Loan has been requested - money deposited"));
        Transaction transaction = new Transaction(null, loan.getAccount(), loan.getAmount(), loan.getPurpose());
        transaction.setLabel("Loan");
        loggedIn.addLoans(loan);
        transaction.setAmount(loan.getAmount());
        create(transaction, loggedIn);
        return transaction;
    }

    public Loan payBackLoan(Customer loggedIn, Transaction transaction) throws Exception{
        if(!loggedIn.getLoans().isEmpty()){
            for(Loan loan : loggedIn.getLoans()){
                if(transaction.getLabel().equals(loan.getPurpose())){
                    loggedIn.getLoans().remove(loan);
                    double newAmount = loan.getAmount() - transaction.getAmount();
                    loan.setAmount(newAmount);
                    loggedIn.getLoans().add(loan);
                    return loan;
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