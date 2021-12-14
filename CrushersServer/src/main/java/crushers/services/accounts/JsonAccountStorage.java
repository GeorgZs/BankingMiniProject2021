package crushers.services.accounts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import crushers.models.Bank;
import crushers.models.accounts.Account;
import crushers.models.users.Customer;
import crushers.storage.JsonStorage;

public class JsonAccountStorage extends JsonStorage<Account> {

  private final Map<Customer, Set<Account>> customerAccounts;
  private final Map<Bank, Set<Account>> bankAccounts;
  private final Map<Bank, Set<Customer>> bankCustomers;

  public JsonAccountStorage(File jsonFile) throws IOException {
    super(jsonFile, Account.class);
    this.customerAccounts = new HashMap<>();
    this.bankAccounts = new HashMap<>();
    this.bankCustomers = new HashMap<>();

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

  public Set<Account> getAccountsOfCustomer(Customer customer) {
    for(Account account : customerAccounts.get(customer)){
      account.setOwner(customer);
    }
    return customerAccounts.get(customer);
  }

  public Set<Account> getAccountsOfBank(Bank bank) {
    return bankAccounts.get(bank);
  }

  public Set<Customer> getCustomersAtBank(Bank bank) {
    return bankCustomers.get(bank);
  }

  protected void addToMaps(Account account) {
    if (!customerAccounts.containsKey(account.getOwner())) {
      customerAccounts.put(account.getOwner(), new HashSet<>());
    }

    if (!bankAccounts.containsKey(account.getBank())) {
      bankAccounts.put(account.getBank(), new HashSet<>());
    }

    if(!bankCustomers.containsKey(account.getBank())){
      bankCustomers.put(account.getBank(), new HashSet<>());
    }

    customerAccounts.get(account.getOwner()).add(account);
    bankAccounts.get(account.getBank()).add(account);
    bankCustomers.get(account.getBank()).add(account.getOwner());
  }
}
