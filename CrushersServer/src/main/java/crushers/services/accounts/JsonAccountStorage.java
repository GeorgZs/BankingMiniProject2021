package crushers.services.accounts;

import java.io.File;
import java.io.IOException;
import java.util.*;

import crushers.common.models.*;
import crushers.storage.JsonStorage;

public class JsonAccountStorage extends JsonStorage<BankAccount> {

  private final Map<User, Set<BankAccount>> customerAccounts;
  private final Map<Bank, Set<BankAccount>> bankAccounts;
  private final Map<Bank, Set<User>> bankCustomers;

  public JsonAccountStorage(File jsonFile) throws IOException {
    super(jsonFile, BankAccount.class);
    this.customerAccounts = new HashMap<>();
    this.bankAccounts = new HashMap<>();
    this.bankCustomers = new HashMap<>();

    for (BankAccount account : this.data.values()) {
      addToMaps(account);
    }
  }

  /**
   * @param newAccount to be added to the Account Storage
   * @return the Account that was successfully added to the Storage
   * @throws Exception if the Account creation was in any way unsuccessful
   */
  @Override
  public BankAccount create(BankAccount newAccount) throws Exception {
    BankAccount createdAccount = super.create(newAccount);
    addToMaps(createdAccount);
    return createdAccount;
  }

  /**
   * @param id of the specified account
   * @return the account that was deleted just as a precaution to see what account was deleted
   * @throws IOException if Account deletion was in any way unsuccessful
   */
  @Override
  public BankAccount delete(int id) throws IOException {
    BankAccount deletedAccount = super.delete(id);

    if (deletedAccount != null) {
      customerAccounts.get(deletedAccount.getOwner()).remove(deletedAccount);
      bankAccounts.get(deletedAccount.getBank()).remove(deletedAccount);
    }

    return deletedAccount;
  }

  /**
   * @param customer who is logged-in
   * @return the set of Accounts for the logged-in Customer
   */
  public Set<BankAccount> getAccountsOfCustomer(User customer) {
    return customerAccounts.get(customer) == null ? new HashSet<>() : customerAccounts.get(customer);
  }

  /**
   * @param bank of the logged-in Clerk
   * @return the Set of Customers registered to the Bank
   */
  public Set<User> getCustomersAtBank(Bank bank) {
    return bankCustomers.get(bank) == null ? new HashSet<>() : bankCustomers.get(bank);
  }

  /**
   * @param bank of the logged-in Clerk
   * @return the Set of Accounts registered to the Bank
   */
  public Set<BankAccount> getAccountsOfBank(Bank bank) {
    return bankAccounts.get(bank) == null ? new HashSet<>() : bankAccounts.get(bank);
  }

  /**
   * @param account added to the Local Map of Accounts which are retrieved from the JSON-files
   * adds the Account specified in the method signature to each of the maps
   *
   * checks whether each Map key has a set associated to it, if not it creates a new empty set for the Key
   */
  protected void addToMaps(BankAccount account) {
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
