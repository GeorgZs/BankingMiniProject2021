package crushers.services.customers;

import com.sun.net.httpserver.*;

import java.util.Collection;

import crushers.models.users.Customer;
import crushers.server.Router;
import crushers.server.customExceptions.MethodNotAllowedException;

public class CustomerRouter extends Router<Customer> {

  private final CustomerService customerService;

  public CustomerRouter(CustomerService customerService) {
    super("/customers");
    this.customerService = customerService;
  }

  @Override
  public void addEndpoints(HttpServer server) {
    super.addEndpoints(server); // add the prewiring

    server.createContext("/customers/@me", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "GET":
            getLoggedIn(exchange);
            break;
        
          default:
            throw new MethodNotAllowedException();
        }
      }
      catch (MethodNotAllowedException ex) {
        exchange.sendResponseHeaders(405, 0);
      }
      catch (IllegalAccessException ex) {
        sendResponse(exchange, 401, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
      }
      catch (IllegalArgumentException ex) {
        sendResponse(exchange, 400, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
      }
      catch (Exception ex) {
        sendResponse(exchange, 500, String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes());
        ex.printStackTrace();
      }
    });
  }

  @Override
  protected void post(HttpExchange exchange) throws Exception {
    final Customer requestData = getJsonBodyData(exchange, Customer.class);
    final Customer responseData = customerService.create(requestData);
    sendJsonResponse(exchange, responseData);
  }

  private void getLoggedIn(HttpExchange exchange) throws Exception {
    final Customer responseData = customerService.getLoggedIn();
    sendJsonResponse(exchange, responseData);
  }
  
}
