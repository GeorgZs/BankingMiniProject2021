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

  public Customer getLoggedIn(Customer loggedInCustomer) throws Exception {
    return customerStorage.get(loggedInCustomer.getId());
  }

  public Customer get(int id) throws Exception {
    Customer customer = customerStorage.get(id);

    if (customer == null) {
      throw new NotFoundException("No customer with id " + id + " could be found.");
    }

    return customer;
  }

  public Collection<Customer> getAll() throws Exception {
    return customerStorage.getAll();
  }

  public Collection<String> getAllEmail() throws Exception{
    Collection<String> emailList = new ArrayList<>();
    for(Customer customer : customerStorage.getAll()){
      emailList.add(customer.getEmail());
    }
    return emailList;
  }

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
    if(!contact.getName().equals(contact.getAccount().getOwner().getFirstName())){
      invalidDataMessage.add("Contact name does not match Account Owner's name");
    }
    /*
    if(customer.getFirstName().equals(contact.getAccount().getOwner().getFirstName())){
      invalidDataMessage.add("Cannot create contact with own Account");
    }
     */
    if(!invalidDataMessage.isEmpty()){
      throw new BadRequestException(String.join("\n", invalidDataMessage));
    }
    return customerStorage.createContact(customer, contact);
  }
  public Collection<Contact> getContacts(Customer customer){
    return customerStorage.getContacts(customer);
  }


  public ManagerNotification sendNotificationToUsers(Manager loggedInManager, ManagerNotification requestData) throws IOException {
    ManagerNotification newNotification = new ManagerNotification(requestData.getNotification());
    Collection<Customer> customers = customerStorage.getAll();
    for (Customer customer : customers){
      customer.addNotification(newNotification);
      System.out.println(customer.getNotification());
      customerStorage.update(customer.getId(), customer);
    }
    return newNotification;
  }

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

  public LinkedHashMap<LocalDateTime, String> getNotifications(Customer customer){
    Customer customer1 = customerStorage.get(customer.getId());
    return customer1.getNotification();
  }

}
