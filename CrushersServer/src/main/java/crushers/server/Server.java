package crushers.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import crushers.models.Bank;
import crushers.models.Duck;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.services.customers.CustomerRouter;
import crushers.services.customers.CustomerService;
import crushers.services.ducks.DuckRouter;
import crushers.services.ducks.DuckService;
import crushers.services.ducks.bankAndStaff.BankRouter;
import crushers.services.ducks.bankAndStaff.BankService;
import crushers.services.ducks.bankAndStaff.StaffRouter;
import crushers.services.ducks.bankAndStaff.StaffService;
import crushers.storage.JsonStorage;

/**
 * The actual http server of which the port can be configured in the constructor.
 */
public class Server {
  
  public final int port;
  private final HttpServer httpServer;

  public Server(int port) throws IOException {
    this.port = port;
    this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
    addServices();
  }

  /**
   * Here we add our services to the server so that they can be accessed via http.
   */
  private void addServices() throws IOException {
    new File("data").mkdirs();

    final DuckService duckService = new DuckService(new JsonStorage<Duck>(
      new File("data/ducks.json"), 
      Duck.class
    ));
    new DuckRouter(duckService).addEndpoints(this.httpServer);

    final CustomerService customerService = new CustomerService(new JsonStorage<Customer>(
      new File("data/customers.json"), 
      Customer.class
    ));
    new CustomerRouter(customerService).addEndpoints(this.httpServer);

    final StaffService staffService = new StaffService(new JsonStorage<Clerk>(
            new File("data/staff.json"),
            Clerk.class
    ));
    new StaffRouter(staffService).addEndpoints(this.httpServer);

    final BankService bankService = new BankService(new JsonStorage<Bank>(
            new File("data/bank.json"),
            Bank.class
    ));
    new BankRouter(bankService).addEndpoints(this.httpServer);
  }

  /**
   * Starts the server.
   */
  public void start() {
    httpServer.start();
    System.out.println("Server started at http://localhost:" + port);
  }

}
