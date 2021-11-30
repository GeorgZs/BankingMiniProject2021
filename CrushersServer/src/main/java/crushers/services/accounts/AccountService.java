package crushers.services.accounts;

import java.util.ArrayList;
import java.util.Collection;

import crushers.models.Bank;
import crushers.models.accounts.Account;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.models.users.User;
import crushers.server.httpExceptions.*;
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

  public Account create(User creator, Account account) throws Exception {
    if (account.getBank() == null) {
      throw new BadRequestException("The bank, the account should be opened at, is required.");
    }

    if (creator instanceof Clerk) {
      Clerk clerk = (Clerk) creator;
      account.setBank(bankService.get(clerk.getWorksAt().getId())); // set the bank the account is opened at
      account.setOwner(customerService.get(account.getOwner().getId())); // check if the customer exists
    }
    else if (creator instanceof Customer) {
      Customer customer = (Customer) creator;
      account.setBank(bankService.get(account.getBank().getId())); // check if the bank exists
      account.setOwner(customerService.get(customer.getId())); // set the owner of the account
    }

    account.setNumber(account.getBank().generateAccountNumber());
    return storage.create(account);
  }

  public Account get(User loggedInUser, int id) throws Exception {
    Account account = storage.get(id);
    if (account == null) throw new ForbiddenException();

    System.out.println(loggedInUser.getClass());
    System.out.println(account.getBank().getId());
    boolean isBankStaff = (loggedInUser instanceof Clerk && account.getBank().equals(((Clerk)loggedInUser).getWorksAt()));
    if (!account.getOwner().equals(loggedInUser) && !isBankStaff) {
      throw new ForbiddenException();
    }

    return account;
  }

  public Account get(Customer loggedInCustomer, String accountNum) throws Exception{
    Collection<Account> customerAccounts = getOfCustomer(loggedInCustomer);
    for(Account account : customerAccounts){
      if(account.getNumber().equals(accountNum)){
        return account;
      }
    }
    return null;
  }

  public Account get(String accountNum) throws Exception{
    Collection<Account> customerAccounts = storage.getAll();
    for(Account account : customerAccounts){
      if(account.getNumber().equals(accountNum)){
        return account;
      }
    }
    return null;
  }

  public Collection<Account> getOfCustomer(Customer customer) throws Exception {
    Collection<Account> accounts = storage.getAccountsOfCustomer(customer);
    if (accounts == null) accounts = new ArrayList<>();
    return accounts;
  }

  public Collection<Account> getOfBank(Bank bank) throws Exception {
    Collection<Account> accounts = storage.getAccountsOfBank(bank);
    if (accounts == null) accounts = new ArrayList<>();
    return accounts;
  }

  public Collection<String> getIDs(Customer customer) throws Exception{
    Collection<String> ids = storage.getIDsOfSpecificAccounts(customer);
    return ids;
  }

  public Collection<String> getAllIDs() throws Exception{
    Collection<String> ids = storage.getIDsFromAllAccounts();
    return ids;
  }
}
