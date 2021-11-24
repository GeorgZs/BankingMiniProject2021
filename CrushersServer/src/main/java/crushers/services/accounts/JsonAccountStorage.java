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

    for (Account account : this.data.values()) {
      addToMaps(account);
    }
  }

  @Override
  public Account create(Account newAccount) throws Exception {
    Account createdAccount = super.create(newAccount);
    addToMaps(createdAccount);
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
  

  protected void addToMaps(Account account) {
    if (!customerAccounts.containsKey(account.getOwner())) {
      customerAccounts.put(account.getOwner(), new ArrayList<>());
    }

    if (!bankAccounts.containsKey(account.getBank())) {
      bankAccounts.put(account.getBank(), new ArrayList<>());
    }

    customerAccounts.get(account.getOwner()).add(account);
    bankAccounts.get(account.getBank()).add(account);
  }
}
