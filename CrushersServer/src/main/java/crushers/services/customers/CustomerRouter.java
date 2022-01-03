package crushers.services.customers;

import com.sun.net.httpserver.*;

import crushers.common.httpExceptions.HttpException;
import crushers.common.httpExceptions.MethodNotAllowedException;
import crushers.common.models.*;
import crushers.server.Authenticator;
import crushers.server.Router;

public class CustomerRouter extends Router {

  private final CustomerService customerService;

  public CustomerRouter(CustomerService customerService) {
    super("/customers");
    this.customerService = customerService;
  }

  @Override
  public void addEndpoints(HttpServer server) throws Exception {
    super.addEndpoints(server); // add the prewiring

    server.createContext(basePath + "/@me", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "GET":
            getLoggedIn(exchange);
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
   * creates a customer
   * @throws Exception
   */
  @Override
  protected void post(HttpExchange exchange) throws Exception {
    final User requestData = getJsonBodyData(exchange, User.class);
    final User responseData = customerService.create(requestData);
    sendJsonResponse(exchange, responseData, User.class);
  }

  /**
   * @param exchange
   * @return the logged-in Customer as an object
   * @throws Exception
   */
  private void getLoggedIn(HttpExchange exchange) throws Exception {
    final User loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final User responseData = customerService.getLoggedIn(loggedInCustomer);
    sendJsonResponse(exchange, responseData, User.class);
  }
}
