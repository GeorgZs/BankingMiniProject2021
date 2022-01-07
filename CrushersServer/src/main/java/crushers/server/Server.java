package crushers.server;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Files;

import com.sun.net.httpserver.HttpServer;

import crushers.common.models.*;
import crushers.services.accounts.AccountRouter;
import crushers.services.accounts.AccountService;
import crushers.services.accounts.JsonAccountStorage;
import crushers.services.banks.BankRouter;
import crushers.services.banks.BankService;
import crushers.services.contacts.ContactRouter;
import crushers.services.contacts.ContactService;
import crushers.services.contacts.JsonContactStorage;
import crushers.services.customers.CustomerRouter;
import crushers.services.customers.CustomerService;
import crushers.services.notifications.JsonNotificationStorage;
import crushers.services.notifications.NotificationRouter;
import crushers.services.notifications.NotificationService;
import crushers.services.staff.JsonStaffStorage;
import crushers.services.staff.StaffRouter;
import crushers.services.staff.StaffService;
import crushers.services.transactions.JsonTransactionStorage;
import crushers.services.transactions.TransactionRouter;
import crushers.services.transactions.TransactionService;
import crushers.storage.JsonStorage;

/**
 * The actual http server of which the port can be configured in the constructor.
 */
public class Server {
  
  public final int port;
  private final HttpServer httpServer;

  public Server(int port) throws Exception {
    this.port = port;
    this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
    addServices();
  }

  /**
   * Here we add our services to the server so that they can be accessed via http.
   */
  private void addServices() throws Exception {
    // Create the folder for all our json storage files and load inital data if existing.
    File dataFolder = new File("data");
    File initialDataFolder = new File("initial-data");
    
    if (!dataFolder.exists()) {
      dataFolder.mkdir();
      
      if (initialDataFolder.exists()) {
        for (File file : initialDataFolder.listFiles()) {
          Files.copy(file.toPath(), dataFolder.toPath().resolve(file.getName()));
        }
      }
    }

    final CustomerService customerService = new CustomerService(
      new JsonStorage<User>(new File("data/customers.json"), User.class)
    );

    final BankService bankService = new BankService(
      new JsonStorage<Bank>(new File("data/banks.json"), Bank.class)
    );

    final StaffService staffService = new StaffService(
      bankService,
      new JsonStaffStorage(new File("data/staff.json"))
    );

    final AccountService accountService = new AccountService(
      customerService,
      bankService,
      new JsonAccountStorage(new File("data/accounts.json"))
    );
    
    final TransactionService transactionService = new TransactionService(
      accountService,
      new JsonTransactionStorage(new File("data/transactions.json")),
      new JsonStorage<Transaction>(new File("data/recurring-transactions.json"), Transaction.class)
    );

    final ContactService contactService = new ContactService(
      accountService,
      new JsonContactStorage(new File("data/contacts.json"))
    );

    final NotificationService notificationService = new NotificationService(
      accountService,
      new JsonNotificationStorage(new File("data/notifications.json"))
    );

    new AuthenticationRouter().addEndpoints(httpServer);
    new CustomerRouter(customerService).addEndpoints(this.httpServer);
    new BankRouter(bankService, staffService).addEndpoints(this.httpServer);
    new StaffRouter(staffService).addEndpoints(this.httpServer);
    new AccountRouter(accountService).addEndpoints(this.httpServer);
    new TransactionRouter(transactionService).addEndpoints(this.httpServer);
    new ContactRouter(contactService).addEndpoints(this.httpServer);
    new NotificationRouter(notificationService).addEndpoints(this.httpServer);
  }

  /**
   * Starts the server.
   */
  public void start() {
    httpServer.start();
    System.out.println("=============== [CRUSHERS SERVER] ===============");
    System.out.println(" The server is running on http://localhost:" + port);
    System.out.println(" To use this server, we recommend using our  ");
    System.out.println(" tested customer and staff applications.");
    System.out.println();
    System.out.println(" Press Ctrl+C to stop this server.");
    System.out.println("=============== [CRUSHERS SERVER] ===============");
  }

}
