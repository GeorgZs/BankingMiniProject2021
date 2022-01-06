package crushers.services.accounts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import crushers.common.httpExceptions.*;
import crushers.common.models.*;
import crushers.services.banks.BankService;
import crushers.services.customers.CustomerService;

public class AccountService {
  
  private final JsonAccountStorage storage;
  private final CustomerService customerService;
  private final BankService bankService;

  public AccountService(CustomerService customerService, BankService bankService, JsonAccountStorage storage) {
    this.customerService = customerService;
    this.bankService = bankService;
    this.storage = storage;
  }

  /**
   *
   * @param creator is the logged-in User
   * @param account is the account passed by the creator for creation
   * creates an account as specified by the User, allowing for creation by Staff and Customers alike
   *
   * @example {
   *     id: int,
   *     bank: {
   *         id: int,
   *         name: String,
   *         logo: String,
   *     },
   *     owner: {
   *         id: int,
   *         email: String,
   *         firstName: String,
   *         lastName: String,
   *         address: String,
   *     },
   *     type: "savings" | "payment",
   *     number: String,
   *     balance: double,
   * }
   *
   * @return the account created
   * @throws Exception if the account details are invalid
   */
  public BankAccount create(User creator, BankAccount account) throws Exception {
    if (account == null) {
      throw new BadRequestException("Account cannot be null");
    }

    if (account.isInvalidType()) {
      throw new BadRequestException("Account type is invalid");
    }

    if (creator.isClerk()) {
      account.setBank(bankService.get(creator.getWorksAt().getId())); // set the bank the account is opened at
      account.setOwner(customerService.get(account.getOwner().getId())); // check if the customer exists
    }
    else if (creator.isCustomer()) {
      account.setBank(bankService.get(account.getBank().getId())); // check if the bank exists
      account.setOwner(customerService.get(creator.getId())); // set the owner of the account
    }

    if (account.isLoan()) {
      if (account.getName() == null || account.getName().isEmpty()) {
        throw new BadRequestException("Loan account name cannot be null");
      }

      if(account.getBalance() < -25000 || account.getBalance() > -1000) {
        throw new BadRequestException("Loan amount must be in excess of 1000kr and no more than 25,000kr");
      }
    }

    account.setNumber(account.getBank().generateAccountNumber());
    return storage.create(account);
  }

  /**
   *
   * @param loggedInUser
   * @param id
   * @return account with the matching ID
   * @throws Exception if the ID is invalid or the logged-in User doesnt have correct access to the account specified
   */
  public BankAccount get(User loggedInUser, int id) throws Exception {
    BankAccount account = storage.get(id);
    if (account == null) throw new ForbiddenException();

    boolean isBankStaff = (loggedInUser.isClerk() && account.getBank().equals(loggedInUser.getWorksAt()));

    if (!account.getOwner().equals(loggedInUser) && !isBankStaff) {
      throw new ForbiddenException();
    }

    return account;
  }

  /**
   *
   * @param loggedInUser
   * @param id
   * @return account with the matching ID
   * @throws Exception if the ID is invalid or the logged-in User doesnt have correct access to the account specified
   */
  public BankAccount getForContact(int id) throws Exception {
    BankAccount account = storage.get(id);
    
    BankAccount accountForContact = new BankAccount();
    accountForContact.setId(account.getId());
    accountForContact.setType(account.getType());
    accountForContact.setNumber(account.getNumber());
    accountForContact.setBank(account.getBank());
    accountForContact.setOwner(account.getOwner());
    accountForContact.setName(account.getName());
    return accountForContact;
  }

  /**
   * @param id of the Account in question
   * @return true or false based on whether the account exists or not
   * @throws Exception
   */
  public boolean exists(int id) throws Exception{
    return (storage.get(id) != null);
  }

  /**
   * @param transaction
   * takes the transaction and, once checked for errors, withdraws and deposits from Accounts specified in the Transaction
   * @throws Exception if the Accounts in the transactions are null or if the transaction's amount is greater than or equal to the sender balance
   */
  public void commitTransaction(Transaction transaction) throws Exception {
    if (transaction == null) throw new ForbiddenException();

    if(transaction.getFrom() != null) {
      BankAccount sender = storage.get(transaction.getFrom().getId());

      if (sender.isLoan()) {
        throw new BadRequestException(
                "Cannot pay with a loan"
        );
      }

      if (sender.getBalance() <= transaction.getAmount()) {
        throw new BadRequestException(
                "Account does not have enough funds to create this transaction"
        );
      }

      if(transaction.getTo() != null) {	
        BankAccount receiver = storage.get(transaction.getTo().getId());

        if (sender.isSavings() && !receiver.getOwner().equals(sender.getOwner())) {
          throw new BadRequestException(
                  "Cannot pay with a savings account, only transfer to other accounts you own"
          );
        }

        if (receiver.isLoan() && receiver.getBalance() + transaction.getAmount() > 0) {
          throw new BadRequestException(
                  "You would pay back more than the loan amount"
          );
        }

        receiver.setBalance(receiver.getBalance() + transaction.getAmount());
        storage.update(receiver.getId(), receiver);

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        storage.update(sender.getId(), sender);
      }
      else { // withdraw       
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        storage.update(sender.getId(), sender);
      }
    }
    else { // deposit or interests
      BankAccount receiver = storage.get(transaction.getTo().getId());

      if (receiver.isLoan() && receiver.getBalance() + transaction.getAmount() > 0) {
        throw new BadRequestException(
                "You would pay back more than the loan amount"
        );
      }

      receiver.setBalance(receiver.getBalance() + transaction.getAmount());
      storage.update(receiver.getId(), receiver);
    }
  }

  /**
   * @param customer who is logged-in
   * @return the collection of accounts for logged-in customer
   */
  public Collection<BankAccount> getOfCustomer(User customer) throws Exception {
    Collection<BankAccount> accounts = storage.getAccountsOfCustomer(customer);
    if (accounts == null) accounts = new ArrayList<>();
    return accounts.stream()
      .filter(account -> !account.isFullyPaidBack())
      .collect(Collectors.toList());
  }

  /**
   * @param bank of the logged-in Staff member
   * @return the collection of Accounts registered to the Bank
   */
  public Collection<BankAccount> getOfBank(Bank bank) throws Exception {
    Collection<BankAccount> accounts = storage.getAccountsOfBank(bank);
    if (accounts == null) accounts = new ArrayList<>();
    return accounts.stream()
      .filter(account -> !account.isFullyPaidBack())
      .collect(Collectors.toList());
  }

  /**
   * @param bank of the logged-in Staff member
   * @return the Collection of customers registered to the Bank
   */
  public Collection<User> getCustomersAtBank(Bank bank) {
    return storage.getCustomersAtBank(bank);
  }

  /**
   * @param manager who is logged-in
   * @param newInterestRate in the form of the InterestRate class
   * @return the new Interest Rate in the form of a double
   * @throws Exception if the interest Rate is the same as the already existing Interest Rate number
   */
  public InterestRate changeInterestRate(User manager, InterestRate newInterestRate) throws Exception {
    Collection<BankAccount> accounts = storage.getAccountsOfBank(manager.getWorksAt());
    for (BankAccount account : accounts) {
      account.setInterestRate(newInterestRate.getRate());
      storage.update(account.getId(), account);
    }
    return newInterestRate;
  }

}
