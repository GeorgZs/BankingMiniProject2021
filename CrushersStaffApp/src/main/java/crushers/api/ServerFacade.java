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

  public void resetUserPassword(ResetPasswordRequest resetPasswordRequest) {

  }


  // BANKS

  public Bank createBank(Bank bankData) throws Exception {
    return Http.instance.post("/banks", bankData, Bank.class);
  }

  public List<Bank> listAllBanks() {
    return null;
  }

  public Bank getBankById(int id) {
    return null;
  }

  public void updateBankInterestRate(int id, double interestRate) {

  }


  // Staff

  public User createClerk(User clerkData) {
    return null;
  }

  public User getLoggedInClerk() {
    return null;
  }

  public List<User> listAllClerks() {
    return null;
  }


  // Bank Accounts

  public BankAccount createBankAccount(BankAccount bankAccountData) {
    return null;
  }

  public List<BankAccount> listAllBankAccountsAtThisBank() {
    return null;
  }

  public BankAccount getBankAccountById(int id) {
    return null;
  }


  // Transactions

  public Transaction withdraw(Transaction transactionData) {
    return null;
  }

  public Transaction deposit(Transaction transactionData) {
    return null;
  }

  public List<Transaction> listAllTransactionsOfAccount(int accountId) {
    return null;
  }

  public Transaction getTransactionById(int id) {
    return null;
  }


  // Notifications

  public Notification sendNotification(Notification notificationData) {
    return null;
  }

}
