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
  private final Map<Customer, Collection<String>> idFromAccounts;

  public JsonAccountStorage(File jsonFile) throws IOException {
    super(jsonFile, Account.class);
    this.customerAccounts = new HashMap<>();
    this.bankAccounts = new HashMap<>();
    this.idFromAccounts = new HashMap<>();

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

  public Collection<String> getIDsOfSpecificAccounts(Customer customer){
    Collection<String> ids = new ArrayList<>();
    for(Account account : customerAccounts.get(customer)){
      ids.add(account.getNumber());
    }
    return ids;
  }

  public Collection<String> getIDsFromAllAccounts(){
    Collection<String> id = new ArrayList<>();
    for(Customer customer : customerAccounts.keySet()){
      for(Account account : customerAccounts.get(customer)){
        id.add(account.getNumber());
      }
    }
    return id;
  }
  

  protected void addToMaps(Account account) {
    if (!customerAccounts.containsKey(account.getOwner())) {
      customerAccounts.put(account.getOwner(), new ArrayList<>());
    }

    if (!bankAccounts.containsKey(account.getBank())) {
      bankAccounts.put(account.getBank(), new ArrayList<>());
    }

    if(!idFromAccounts.containsKey(account.getOwner())){
      idFromAccounts.put(account.getOwner(), new ArrayList<>());
    }

    customerAccounts.get(account.getOwner()).add(account);
    bankAccounts.get(account.getBank()).add(account);
    idFromAccounts.get(account.getOwner()).add(account.getNumber());
  }
}
