package crushers.common;

import java.util.List;

import crushers.common.utils.Http;
import crushers.common.models.*;

public class ServerFacade {
  // Singleton pattern
  public final static ServerFacade instance = new ServerFacade();


  private String authToken;

  private ServerFacade() {}


  // Authentication

  public void loginUser(Credentials credentials) throws Exception {
    AuthToken token = Http.instance.postLogin("/auth/login", credentials, Credentials.class, AuthToken.class);
    this.authToken = token.getToken();
  }

  public void logoutUser() throws Exception {
    Http.instance.postLogout("/auth/logout", this.authToken);
    this.authToken = null;
  }


  // Customers

  public User createCustomer(User customer) throws Exception {
    return Http.instance.post("/customers", customer, User.class);
  }

  public User getLoggedInCustomer() throws Exception {
    return Http.instance.getWithAuth("/customers/@me", User.class, this.authToken);
  }


  // Banks

  public Bank createBank(Bank bankData) throws Exception {
    return Http.instance.post("/banks", bankData, Bank.class);
  }

  public void updateBankInterestRate(InterestRate interestRate) throws Exception {
    Http.instance.putWithAuth("/banks/interestRate", interestRate, InterestRate.class, this.authToken);
  }

  public List<Bank> listAllBanks() throws Exception {
    return Http.instance.getListWithAuth("/banks", Bank.class, this.authToken);
  }


  // Staff

  public User createClerk(User clerkData) throws Exception {
    return Http.instance.postWithAuth("/staff", clerkData, User.class, this.authToken);
  }

  public User getLoggedInClerk() throws Exception {
    return Http.instance.getWithAuth("/staff/@me", User.class, this.authToken);
  }

  public List<User> listAllClerks() throws Exception {
    return Http.instance.getListWithAuth("/staff/@bank", User.class, this.authToken);
  }


  // Bank Accounts

  public BankAccount createBankAccount(BankAccount bankAccountData) throws Exception {
    return Http.instance.postWithAuth("/accounts", bankAccountData, BankAccount.class, this.authToken);
  }

  public List<BankAccount> listAllBankAccountsOfThisCustomer() throws Exception {
    return Http.instance.getListWithAuth("/accounts/@me", BankAccount.class, this.authToken);
  }

  public List<BankAccount> listAllBankAccountsAtThisBank() throws Exception {
    return Http.instance.getListWithAuth("/accounts/@bank", BankAccount.class, this.authToken);
  }


  // Transactions

  public Transaction createTransaction(Transaction transactionData) throws Exception {
    return Http.instance.postWithAuth("/transactions", transactionData, Transaction.class, this.authToken);
  }

  public Transaction createRecurringTransaction(Transaction transactionData) throws Exception {
    return Http.instance.postWithAuth("/transactions/recurring", transactionData, Transaction.class, this.authToken);
  }

  public Transaction requestsInterests(int accountId) throws Exception {
    return Http.instance.getWithAuth("/transactions/interests/" + accountId, Transaction.class, this.authToken);
  }


  public List<Transaction> listAllTransactionsOfAccount(int accountId) throws Exception {
    return Http.instance.getListWithAuth("/transactions/" + accountId, Transaction.class, this.authToken);
  }

  public List<Transaction> listAllSuspiciousTransactions() throws Exception {
    return Http.instance.getListWithAuth("/transactions/suspicious", Transaction.class, this.authToken);
  }

  public TransactionLabels setTransactionLabels(int transactionId, TransactionLabels labels) throws Exception {
    return Http.instance.putWithAuth("/transactions/labels" + transactionId, labels, TransactionLabels.class, this.authToken);
  }


  // Contacts

  public Contact createContact(Contact contactData) throws Exception {
    return Http.instance.postWithAuth("/contacts", contactData, Contact.class, this.authToken);
  }

  public List<Contact> listAllContactsOfThisCustomer() throws Exception {
    return Http.instance.getListWithAuth("/contacts", Contact.class, this.authToken);
  }


  // Notifications

  public Notification sendNotification(Notification notificationData) throws Exception {
    return Http.instance.postWithAuth("/notifications", notificationData, Notification.class, this.authToken);
  }

  public List<Notification> listAllNotificationsForThisUser() throws Exception {
    return Http.instance.getListWithAuth("/notifications", Notification.class, this.authToken);
  }

}
