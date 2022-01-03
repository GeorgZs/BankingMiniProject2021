package crushers.services.contacts;

import java.util.Collection;

import com.sun.net.httpserver.*;

import crushers.common.models.*;
import crushers.server.Router;
import crushers.server.Authenticator;

public class ContactRouter extends Router {

  private final ContactService service;

  public ContactRouter(ContactService service) {
    super("/contacts");
    this.service = service;
  }

  /**
   * @param exchange
   * creates a Contact and adds it to the Contact list for the logged-in Customer
   * @throws Exception
   */
  @Override
  public void post(HttpExchange exchange) throws Exception{
    final User loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final Contact requestData = getJsonBodyData(exchange, Contact.class);
    final Contact responseData = service.createContact(loggedInCustomer, requestData);
    sendJsonResponse(exchange, responseData, Contact.class);
  }

  /**
   * @param exchange
   * gets the collection of Contacts from the logged-in Customer
   * @throws Exception
   */
  @Override
  public void getAll(HttpExchange exchange) throws Exception{
    final User loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final Collection<Contact> responseData = service.getContacts(loggedInCustomer);
    sendJsonCollectionResponse(exchange, responseData, Contact.class);
  }

}
