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

public class AccountService {
  
  private final JsonAccountStorage storage;
  private final BankService bankService;

  public AccountService(BankService bankService, JsonAccountStorage storage) {
    this.bankService = bankService;
    this.storage = storage;
  }

  public Account create(Customer owner, Account account) throws Exception {
    if (account.getBank() == null) {
      throw new BadRequestException("The bank, the account should be opened at, is required.");
    }
    
    // check if the bank exists
    account.setBank(bankService.get(account.getBank().getId()));

    account.setOwner(owner);
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

  public Account get(Bank bank, String accountNum) throws Exception{
    Collection<Account> customerAccounts = getOfBank(bank);
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
