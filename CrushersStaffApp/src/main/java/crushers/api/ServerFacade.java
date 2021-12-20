package crushers.api;

import java.util.List;

import crushers.datamodels.*;

public class ServerFacade {
  // Singleton pattern
  public final static ServerFacade instance = new ServerFacade();


  private String authToken;

  private ServerFacade() {}


  // AUTHENTICATION

  public void loginUser(Credentials credentials) throws Exception {
    AuthToken token = Http.instance.post("/auth/login", credentials, Credentials.class, AuthToken.class);
    this.authToken = token.getToken();
  }

  public void logoutUser() throws Exception {
    Http.instance.post("/auth/logout", null, null, this.authToken);
    this.authToken = null;
  }

  public void resetUserPassword(ResetPasswordRequest resetPasswordRequest) throws Exception {
    Http.instance.put("/auth/password", resetPasswordRequest, ResetPasswordRequest.class);
  }


  // BANKS

  public Bank createBank(Bank bankData) throws Exception {
    return Http.instance.post("/banks", bankData, Bank.class);
  }

  public void updateBankInterestRate(double interestRate) throws Exception {
    Http.instance.put("/banks/@interestRate", interestRate, Double.class, this.authToken);

  }


  // Staff

  public User createClerk(User clerkData) throws Exception {
    return Http.instance.post("/staff", clerkData, User.class, this.authToken);
  }

  public User getLoggedInClerk() throws Exception {
    return Http.instance.get("/staff/@me", User.class, this.authToken);
  }

  public List<User> listAllClerks() {
    return null;
  }


  // Bank Accounts

  public BankAccount createBankAccount(BankAccount bankAccountData) throws Exception {
    return Http.instance.post("/accounts", bankAccountData, BankAccount.class, this.authToken);
  }

  public List<BankAccount> listAllBankAccountsAtThisBank() throws Exception {
    return Http.instance.getList("/accounts/@bank", BankAccount.class, this.authToken);
  }

  public BankAccount getBankAccountById(int id) {
    return null;
  }


  // Transactions

  public Transaction withdraw(Transaction transactionData) throws Exception {
    return Http.instance.post("/transactions", transactionData, Transaction.class, this.authToken);

  }

  public Transaction deposit(Transaction transactionData) throws Exception {
    return Http.instance.post("/transactions", transactionData, Transaction.class, this.authToken);

  }

  public List<Transaction> listAllTransactionsOfAccount(int accountId) {
    return null;
  }

  public Transaction getTransactionById(int id) {
    return null;
  }


  // Notifications

  public Notification sendNotification(Notification notificationData) throws Exception {
    return Http.instance.post("/customers/notificationToUsers", notificationData, Notification.class, this.authToken);
  }

}
