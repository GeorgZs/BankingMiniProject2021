package crushers.common;

import java.util.List;

import crushers.common.utils.Http;
import crushers.common.models.*;

public class ServerFacade {
  // Singleton pattern
  public final static ServerFacade instance = new ServerFacade();


  private String authToken;

  private ServerFacade() {}


  // AUTHENTICATION

  public void loginUser(Credentials credentials) throws Exception {
    AuthToken token = Http.instance.postLogin("/auth/login", credentials, Credentials.class, AuthToken.class);
    this.authToken = token.getToken();
  }

  public void logoutUser() throws Exception {
    Http.instance.postLogout("/auth/logout", this.authToken);
    this.authToken = null;
  }


  // BANKS

  public Bank createBank(Bank bankData) throws Exception {
    return Http.instance.post("/banks", bankData, Bank.class);
  }

  public void updateBankInterestRate(InterestRate interestRate) throws Exception {
    Http.instance.putWithAuth("/banks/interestRate", interestRate, InterestRate.class, this.authToken);

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

  public List<BankAccount> listAllBankAccountsAtThisBank() throws Exception {
    return Http.instance.getListWithAuth("/accounts/@bank", BankAccount.class, this.authToken);
  }


  // Transactions

  public Transaction withdraw(Transaction transactionData) throws Exception {
    return Http.instance.postWithAuth("/transactions", transactionData, Transaction.class, this.authToken);

  }

  public Transaction deposit(Transaction transactionData) throws Exception {
    return Http.instance.postWithAuth("/transactions", transactionData, Transaction.class, this.authToken);
  }

  public List<Transaction> listAllTransactionsOfAccount(int accountId) throws Exception {
    return Http.instance.getListWithAuth("/transactions/" + accountId, Transaction.class, this.authToken);
  }

  public List<Transaction> listAllSuspiciousTransactions() throws Exception {
    return Http.instance.getListWithAuth("/transactions/suspicious", Transaction.class, this.authToken);
  }


  // Notifications

  public Notification sendNotification(Notification notificationData) throws Exception {
    return Http.instance.postWithAuth("/notifications", notificationData, Notification.class, this.authToken);
  }

}
