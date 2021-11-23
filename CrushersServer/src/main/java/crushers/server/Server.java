package crushers.server;

import java.io.File;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import crushers.models.Bank;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;

import crushers.services.AuthenticationRouter;
import crushers.services.accounts.AccountRouter;
import crushers.services.accounts.AccountService;
import crushers.services.accounts.JsonAccountStorage;
import crushers.services.banks.BankRouter;
import crushers.services.banks.BankService;
import crushers.services.customers.CustomerRouter;
import crushers.services.customers.CustomerService;
import crushers.services.staff.JsonClerkStorage;
import crushers.services.staff.StaffRouter;
import crushers.services.staff.StaffService;

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
    new File("data").mkdirs();

    new AuthenticationRouter().addEndpoints(httpServer);

    final CustomerService customerService = new CustomerService(new JsonStorage<Customer>(
      new File("data/customers.json"), 
      Customer.class
    ));
    new CustomerRouter(customerService).addEndpoints(this.httpServer);

    final StaffService staffService = new StaffService(new JsonClerkStorage(new File("data/staff.json")
    ));
    new StaffRouter(staffService).addEndpoints(this.httpServer);

    final BankService bankService = new BankService(new JsonStorage<Bank>(
      new File("data/bank.json"),
      Bank.class
    ), staffService);
    new BankRouter(bankService).addEndpoints(this.httpServer);
    
    final AccountService accountService = new AccountService(
      bankService,
      new JsonAccountStorage(new File("data/accounts.json"))
    );
    new AccountRouter(accountService).addEndpoints(this.httpServer);
  }

  /**
   * Starts the server.
   */
  public void start() {
    httpServer.start();
    System.out.println("Server started at http://localhost:" + port);
  }

}
