package crushers.services.accounts;

import java.util.ArrayList;
import java.util.Collection;

import crushers.models.Bank;
import crushers.models.accounts.Account;
import crushers.models.users.Customer;
import crushers.server.httpExceptions.*;
import crushers.services.customers.CustomerService;
import crushers.storage.Storage;

public class AccountService {
  
  private final Storage<Account> storage;
  private final CustomerService customerService;

  public AccountService(CustomerService customerService, Storage<Account> storage) {
    this.customerService = customerService;
    this.storage = storage;
  }

  public Account create(Account account) throws Exception {
    Customer owner = customerService.getLoggedIn();
    account.setOwner(owner);

    // general validation
    if (account.getBank() == null || account.getBank().getId() == -1) throw new BadRequestException("The bank the account should be opened at is required.");

    // TODO: check if the bank exists
    return storage.create(account);
  }

  public Account get(int id) throws Exception {
    // TODO: check authentication
    Account account = storage.get(id);
    if (account == null) throw new NotFoundException("No account found with id " + id);
    return account;
  }

  public Collection<Account> getOfOwner(Customer customer) throws Exception {
    // TODO
    return new ArrayList<>();
  }

  public Collection<Account> getOfBank(Bank bank) throws Exception {
    // TODO
    return new ArrayList<>();
  }
}
