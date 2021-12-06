package crushers.services.accounts;

import com.sun.net.httpserver.*;

import java.util.Collection;

import crushers.models.accounts.Account;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.models.users.User;
import crushers.server.Authenticator;
import crushers.server.Router;
import crushers.server.httpExceptions.HttpException;
import crushers.server.httpExceptions.MethodNotAllowedException;

public class AccountRouter extends Router<Account> {

  private final AccountService accountService;

  public AccountRouter(AccountService customerService) {
    super("/accounts");
    this.accountService = customerService;
  }

  @Override
  public void addEndpoints(HttpServer server) throws Exception {
    super.addEndpoints(server); // add the prewiring

    server.createContext(basePath + "/@me", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "GET":
            getOfLoggedInCustomer(exchange);
            break;
        
          default:
            throw new MethodNotAllowedException();
        }
      }
      catch (HttpException ex) {
        sendResponse(exchange, ex.statusCode, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
      }
      catch (Exception ex) {
        sendResponse(exchange, 500, String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes());
        ex.printStackTrace();
      }
    });

    server.createContext(basePath + "/@bank", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "GET":
            getOfLoggedInBank(exchange);
            break;
        
          default:
            throw new MethodNotAllowedException();
        }
      }
      catch (HttpException ex) {
        sendResponse(exchange, ex.statusCode, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
      }
      catch (Exception ex) {
        sendResponse(exchange, 500, String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes());
        ex.printStackTrace();
      }
    });

    server.createContext("/customers/@bank", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "GET":
            getCustomersAtBank(exchange);
            break;
        
          default:
            throw new MethodNotAllowedException();
        }
      }
      catch (HttpException ex) {
        sendResponse(exchange, ex.statusCode, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
      }
      catch (Exception ex) {
        sendResponse(exchange, 500, String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes());
        ex.printStackTrace();
      }
    });
  }

  @Override
  protected void post(HttpExchange exchange) throws Exception {
    final User loggedInUser = Authenticator.instance.authUser(exchange);
    final Account requestData = getJsonBodyData(exchange, Account.class);
    final Account responseData = accountService.create(loggedInUser, requestData);
    sendJsonResponse(exchange, responseData);
  }

  @Override
  protected void get(HttpExchange exchange, int id) throws Exception {
    final User loggedInUser = Authenticator.instance.authUser(exchange);
    final Account responseData = accountService.get(loggedInUser, id);
    sendJsonResponse(exchange, responseData);
  }

  private void getOfLoggedInCustomer(HttpExchange exchange) throws Exception {
    final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final Collection<Account> responseData = accountService.getOfCustomer(loggedInCustomer);
    sendJsonResponse(exchange, responseData);
  }

  private void getOfLoggedInBank(HttpExchange exchange) throws Exception {
    final Clerk loggedInClerk = Authenticator.instance.authClerk(exchange);
    final Collection<Account> responseData = accountService.getOfBank(loggedInClerk.getWorksAt());
    sendJsonResponse(exchange, responseData);
  }

  private void getCustomersAtBank(HttpExchange exchange) throws Exception {
    final Clerk loggedInClerk = Authenticator.instance.authClerk(exchange);
    final Collection<Customer> responseData = accountService.getCustomersAtBank(loggedInClerk.getWorksAt());
    sendJsonResponse(exchange, responseData);
  }

}
