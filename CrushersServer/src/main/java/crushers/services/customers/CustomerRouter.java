package crushers.services.customers;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.sun.net.httpserver.*;

import crushers.models.exchangeInformation.Contact;
import crushers.models.exchangeInformation.CustomerNotification;
import crushers.models.exchangeInformation.ManagerNotification;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.models.users.Manager;
import crushers.server.Authenticator;
import crushers.server.Router;
import crushers.server.httpExceptions.HttpException;
import crushers.server.httpExceptions.MethodNotAllowedException;

public class CustomerRouter extends Router<Customer> {

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

    server.createContext(basePath + "/contact", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "POST":
            addContact(exchange);
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

    server.createContext(basePath + "/@contact", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "GET":
            getContacts(exchange);
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

    server.createContext(basePath + "/notificationToUsers", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "POST":
            sendNotificationToUsers(exchange);
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



    server.createContext(basePath + "/notification", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "POST":
            sendNotification(exchange);
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

    server.createContext(basePath + "/notification", (exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "GET":
            getNotifications(exchange);
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
    final Customer requestData = getJsonBodyData(exchange, Customer.class);
    final Customer responseData = customerService.create(requestData);
    sendJsonResponse(exchange, responseData);
  }

  private void getLoggedIn(HttpExchange exchange) throws Exception {
    final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final Customer responseData = customerService.getLoggedIn(loggedInCustomer);
    sendJsonResponse(exchange, responseData);
  }

  private void addContact(HttpExchange exchange) throws Exception{
    final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final Contact requestData = getJsonBodyData(exchange, Contact.class);
    final Contact responseData = customerService.createContact(loggedInCustomer, requestData);
    sendJsonResponse(exchange, responseData);
  }

  private void getContacts(HttpExchange exchange) throws Exception{
    final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final Collection<Contact> responseData = customerService.getContacts(loggedInCustomer);
    sendJsonResponse(exchange, responseData);
  }

  private void sendNotificationToUsers(HttpExchange exchange) throws Exception{
    final Manager loggedInManager = Authenticator.instance.authManager(exchange);
    final ManagerNotification requestData = getJsonBodyData(exchange, ManagerNotification.class);
    final ManagerNotification responseData = customerService.sendNotificationToUsers(loggedInManager, requestData);
    sendJsonResponse(exchange, responseData);
  }

  private void sendNotification(HttpExchange exchange) throws Exception{
    final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final CustomerNotification requestData = getJsonBodyData(exchange, CustomerNotification.class);
    final CustomerNotification responseData = customerService.sendNotification(loggedInCustomer, requestData);
    sendJsonResponse(exchange, responseData);
  }

  private void getNotifications(HttpExchange exchange) throws Exception{
    final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final LinkedHashMap<LocalDateTime, String> responseData = customerService.getNotifications(loggedInCustomer);
    sendJsonResponse(exchange, responseData);
  }
}