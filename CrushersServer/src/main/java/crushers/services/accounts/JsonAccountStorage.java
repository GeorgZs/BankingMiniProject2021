package crushers.services.accounts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import crushers.models.Bank;
import crushers.models.accounts.Account;
import crushers.models.users.Customer;
import crushers.storage.JsonStorage;

public class JsonAccountStorage extends JsonStorage<Account> {

  private final Map<Customer, Collection<Account>> customerAccounts;
  private final Map<Bank, Collection<Account>> bankAccounts;

  public JsonAccountStorage(File jsonFile) throws IOException {
    super(jsonFile, Account.class);
    this.customerAccounts = new HashMap<>();
    this.bankAccounts = new HashMap<>();
  }

  @Override
  public Account create(Account newAccount) throws IOException {
    Account createdAccount = super.create(newAccount);

    if (!customerAccounts.containsKey(createdAccount.getOwner())) {
      customerAccounts.put(createdAccount.getOwner(), new ArrayList<>());
    }

    if (!bankAccounts.containsKey(createdAccount.getBank())) {
      bankAccounts.put(createdAccount.getBank(), new ArrayList<>());
    }

    customerAccounts.get(createdAccount.getOwner()).add(createdAccount);
    bankAccounts.get(createdAccount.getBank()).add(createdAccount);
    return createdAccount;
  }

  @Override
  public Account delete(int id) throws IOException {
    Account deletedAccount = super.delete(id);

    if (deletedAccount != null) {
      customerAccounts.get(deletedAccount.getOwner()).remove(deletedAccount);
      bankAccounts.get(deletedAccount.getBank()).remove(deletedAccount);
    }

    return deletedAccount;
  }

  public Collection<Account> getAccountsOfCustomer(Customer customer) {
    return customerAccounts.get(customer);
  }

  public Collection<Account> getAccountsOfBank(Bank bank) {
    return bankAccounts.get(bank);
  }
  
}
