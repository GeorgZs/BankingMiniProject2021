package crushers.services.accounts;

import com.sun.net.httpserver.*;

import java.util.Collection;

import crushers.common.httpExceptions.HttpException;
import crushers.common.httpExceptions.MethodNotAllowedException;
import crushers.common.models.*;
import crushers.server.Authenticator;
import crushers.server.Router;

public class AccountRouter extends Router {

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

    server.createContext("/banks/interestRate", (exchange) -> {
      try {
          switch (exchange.getRequestMethod()) {
              case "PUT":
                  changeInterestRate(exchange);
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

  /**
   * @param exchange
   * checks if User is logged in then creates an account with specified details
   */
  @Override
  protected void post(HttpExchange exchange) throws Exception {
    final User loggedInUser = Authenticator.instance.authUser(exchange);
    final BankAccount requestData = getJsonBodyData(exchange, BankAccount.class);
    final BankAccount responseData = accountService.create(loggedInUser, requestData);
    sendJsonResponse(exchange, responseData, BankAccount.class);
  }

  /**
   * @param exchange
   * @return the account of the logged-in User
   */
  @Override
  protected void get(HttpExchange exchange, int id) throws Exception {
    final User loggedInUser = Authenticator.instance.authUser(exchange);
    final BankAccount responseData = accountService.get(loggedInUser, id);
    sendJsonResponse(exchange, responseData, BankAccount.class);
  }

  /**
   * @param exchange
   * @return collection of accounts for the logged-in Customer
   */
  private void getOfLoggedInCustomer(HttpExchange exchange) throws Exception {
    final User loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final Collection<BankAccount> responseData = accountService.getOfCustomer(loggedInCustomer);
    sendJsonCollectionResponse(exchange, responseData, BankAccount.class);
  }

  /**
   * @param exchange
   * @return collection of accounts registered to the Bank: requires logged-in Clerk
   */
  private void getOfLoggedInBank(HttpExchange exchange) throws Exception {
    final User loggedInClerk = Authenticator.instance.authClerk(exchange);
    final Collection<BankAccount> responseData = accountService.getOfBank(loggedInClerk.getWorksAt());
    sendJsonCollectionResponse(exchange, responseData,  BankAccount.class);
  }

  /**
   * @param exchange
   * @return collection of Customers registered to the Bank: requires logged-in Clerk
   */
  private void getCustomersAtBank(HttpExchange exchange) throws Exception {
    final User loggedInClerk = Authenticator.instance.authClerk(exchange);
    final Collection<User> responseData = accountService.getCustomersAtBank(loggedInClerk.getWorksAt());
    sendJsonCollectionResponse(exchange, responseData, User.class);
  }

  /**
   * @param exchange
   * @return the changed interest rate based on the passed InterestRate object
   * @example
   *  {
   *     "rate": 0.142
   *  }
   * @throws Exception
   */
  private void changeInterestRate(HttpExchange exchange) throws Exception {
    final User loggedInManager = Authenticator.instance.authManager(exchange);
    final InterestRate requestData = getJsonBodyData(exchange, InterestRate.class);
    final InterestRate responseData = accountService.changeInterestRate(loggedInManager, requestData);
    sendJsonResponse(exchange, responseData, InterestRate.class);
  }

}
