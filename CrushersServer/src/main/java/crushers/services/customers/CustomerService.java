package crushers.services.customers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import crushers.models.exchangeInformation.Contact;
import crushers.models.exchangeInformation.CustomerNotification;
import crushers.models.exchangeInformation.ManagerNotification;
import crushers.models.users.Customer;

import crushers.models.users.Manager;
import crushers.server.Authenticator;
import crushers.server.httpExceptions.*;

import crushers.utils.Security;

public class CustomerService {
  
  private final JsonCustomerStorage customerStorage;
  private final Security security = new Security();

  public CustomerService(JsonCustomerStorage customerStorage) throws Exception {
    this.customerStorage = customerStorage;
    
    for (Customer customer : customerStorage.getAll()) {
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
    if (!invalidDataMessage.isEmpty()) throw new BadRequestException(String.join("\\n", invalidDataMessage));

    //for testing out commented
    //customer.setPassword(security.passwordEncryption(customer.getPassword(), "MD5"));

    Authenticator.instance.register(customer);
    return customerStorage.create(customer);
  }

  /**
   * @param loggedInCustomer
   * @return the customer object of the logged-in Customer
   * @throws Exception
   */
  public Customer getLoggedIn(Customer loggedInCustomer) throws Exception {
    return customerStorage.get(loggedInCustomer.getId());
  }

  /**
   * @param id of the desired customer
   * @return the customer with the matching ID
   * @throws Exception if the ID does not match any customer with that ID
   */
  public Customer get(int id) throws Exception {
    Customer customer = customerStorage.get(id);

    if (customer == null) {
      throw new NotFoundException("No customer with id " + id + " could be found.");
    }
    return customer;
  }

  /**
   * @param customer who is logged-in
   * @param contact to be created
   * @return the contact given it passes the tests
   * @throws Exception if the Contact's details are incorrect:
   * if contact is null, contact name is blank/empty, contact Account is null, or the contact description is blank
   */
  public Contact createContact(Customer customer, Contact contact) throws Exception{
    List<String> invalidDataMessage = new ArrayList<>();

    if(contact == null){
      throw new NotFoundException("Contact could not be found");
    }
    if(contact.getName().isBlank() || contact.getName().isEmpty()){
      invalidDataMessage.add("Name of contact cannot be empty");
    }
    if(contact.getAccount() == null) {
      invalidDataMessage.add("Account could not be found");
    }
    if(contact.getDescription().isBlank()){
      invalidDataMessage.add("Description cannot be empty");
    }
    if(!invalidDataMessage.isEmpty()){
      throw new BadRequestException(String.join("\n", invalidDataMessage));
    }
    return customerStorage.createContact(customer, contact);
  }

  /**
   * @param customer who is logged in
   * @return the collection of Contacts for the logged-in Customer
   */
  public Collection<Contact> getContacts(Customer customer){
    return customerStorage.getContacts(customer);
  }

  /**
   * @param loggedInManager
   * @param requestData
   * @return the Notification that was sent to all Users of the Manager's Bank
   * @throws Exception if the Customer storage for the Bank is empty
   */
  public ManagerNotification sendNotificationToUsers(Manager loggedInManager, ManagerNotification requestData) throws Exception {
    ManagerNotification newNotification = new ManagerNotification(requestData.getNotification());
    Collection<Customer> customers = customerStorage.getAll();
    if(customers.isEmpty()){
      throw new BadRequestException("Cannot send notifications to customers as there are none registered");
    }
    for (Customer customer : customers){
      customer.addNotification(newNotification);
      System.out.println(customer.getNotification());
      customerStorage.update(customer.getId(), customer);
    }
    return newNotification;
  }

  /**
   * @param customer who is logged-in
   * @param requestData
   * @return the Notification that the logged-in Customer sent to another Customer
   * @throws Exception if the recipient Customer doesn't exist/ cannot be found
   */
  public CustomerNotification sendNotification(Customer customer, CustomerNotification requestData) throws Exception{
    CustomerNotification customerNotification = new CustomerNotification(requestData.getNotification(), requestData.getTargetCustomer());
    if(customerNotification.getTargetCustomer() != null){
      Customer targetCustomer = customerStorage.get(customerNotification.getTargetCustomer().getId());
      customerNotification.setNotification(customerNotification.getNotification() + " ---- Kind regards, " + customer.getFirstName());
      targetCustomer.addNotification(customerNotification);
      customerStorage.update(targetCustomer.getId(), targetCustomer);
    }
    else{
      throw new BadRequestException("Customer specified in Notification cannot be found");
    }
    return customerNotification;
  }

  /**
   * @param customer who is logged-in
   * @return the LinkedHashMap of all Notifications of the logged-in Customer
   */
  public LinkedHashMap<LocalDateTime, String> getNotifications(Customer customer){
    Customer customer1 = customerStorage.get(customer.getId());
    return customer1.getNotification();
  }

}
