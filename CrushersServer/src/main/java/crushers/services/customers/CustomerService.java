package crushers.services.customers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import crushers.models.users.Customer;
import crushers.storage.Storage;

public class CustomerService {
  
  private final Storage<Customer> storage;

  public CustomerService(Storage<Customer> storage) {
    this.storage = storage;
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

    // build the error message if there are any errors
    if (!invalidDataMessage.isEmpty()) throw new IllegalArgumentException(String.join("\n", invalidDataMessage));

    // TODO: check if email is already in use
    return storage.create(customer);
  }

  public Customer getLoggedIn() throws Exception {
    // TODO: get the actual logged in customer
    if (storage.getAll().isEmpty()) throw new IllegalAccessException("Needs a customer to be logged in (for now create one and the first customer is always logged in).");
    return storage.getAll().iterator().next();
  }

}
