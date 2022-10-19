package crushers.services.customers;

import java.util.ArrayList;
import java.util.List;

import crushers.common.httpExceptions.*;
import crushers.common.models.*;
import crushers.server.Authenticator;
import crushers.storage.JsonStorage;

public class CustomerService {
  
  private final JsonStorage<User> customerStorage;

  public CustomerService(JsonStorage<User> customerStorage) throws Exception {
    this.customerStorage = customerStorage;
    
    for (User customer : customerStorage.getAll()) {
      Authenticator.instance.register(customer);
    }
  }

  /**
   * @param customer for creation
   * @return the created customer once tests are passed
   * @throws Exception if any of the customer fields are invalid
   * if the email, first and last name, address and password are null or blank it throws and Exception
   * for password validation:
   * if password length is less than 8, doesn't contain at least one Capital letter or number, or contains an empty characters --> throws an Exception
   */
  public User create(User customer) throws Exception {
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
    if (!invalidDataMessage.isEmpty()) throw new BadRequestException(String.join("\\n", invalidDataMessage));

    customer.setType("customer");
    Authenticator.instance.register(customer);
    return customerStorage.create(customer);
  }

  /**
   * @param loggedInCustomer
   * @return the customer object of the logged-in Customer
   * @throws Exception
   */
  public User getLoggedIn(User loggedInCustomer) throws Exception {
    return customerStorage.get(loggedInCustomer.getId());
  }

  /**
   * @param id of the desired customer
   * @return the customer with the matching ID
   * @throws Exception if the ID does not match any customer with that ID
   */
  public User get(int id) throws Exception {
    User customer = customerStorage.get(id);

    if (customer == null) {
      throw new NotFoundException("No customer with id " + id + " could be found.");
    }
    return customer;
  }

}
