package crushers.services.customers;

import java.util.ArrayList;
import java.util.List;

import crushers.models.users.Customer;

import crushers.server.Authenticator;
import crushers.server.httpExceptions.*;

import crushers.storage.Storage;
import crushers.utils.Security;

public class CustomerService {
  
  private final Storage<Customer> storage;
  private final Security security = new Security();

  public CustomerService(Storage<Customer> storage) throws Exception {
    this.storage = storage;
    
    for (Customer customer : storage.getAll()) {
      Authenticator.instance.register(customer);
    }
  }

  public Customer create(Customer customer) throws Exception {
    List<String> invalidDataMessage = new ArrayList<>();

    // general validation
    if (customer.getEmail() == null || customer.getEmail().isBlank()) invalidDataMessage.add("Email is required and must not be blank.");
    if (customer.getFirstName() == null || customer.getFirstName().isBlank()) invalidDataMessage.add("First name is required and must not be blank.");
    if (customer.getLastName() == null || customer.getLastName().isBlank()) invalidDataMessage.add("Last name is required and must not be blank.");
    if (customer.getAddress() == null || customer.getAddress().isBlank()) invalidDataMessage.add("Address is required and must not be blank.");
    if (customer.getPassword() == null || customer.getPassword().isBlank()) invalidDataMessage.add("Password is required and must not be blank.");

    // password validation
    if (customer.getPassword() != null && customer.getPassword().length() < 8) invalidDataMessage.add("Password must be at least 8 characters long.");
    if (customer.getPassword() != null && !customer.getPassword().matches(".*[A-Z].*")) invalidDataMessage.add("Password must contain at least 1 capital letter.");
    if (customer.getPassword() != null && !customer.getPassword().matches(".*[0-9].*")) invalidDataMessage.add("Password must contain at least 1 digit.");
    if(customer.getPassword() != null && customer.getPassword().contains(" ")){
      invalidDataMessage.add("Password cannot contain an empty character");
    }

    // build the error message if there are any errors
    if (!invalidDataMessage.isEmpty()) throw new BadRequestException(String.join("\n", invalidDataMessage));

    customer.setPassword(security.passwordEncryption(customer.getPassword(), "MD5"));

    Authenticator.instance.register(customer);
    return storage.create(customer);
  }

  public Customer getLoggedIn(Customer loggedInCustomer) throws Exception {
    return storage.get(loggedInCustomer.getId());
  }

}
