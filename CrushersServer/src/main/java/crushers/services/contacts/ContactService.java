package crushers.services.contacts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import crushers.common.httpExceptions.*;
import crushers.common.models.*;
import crushers.services.accounts.AccountService;

public class ContactService {

  private final JsonContactStorage storage;
  private final AccountService accountService;

  public ContactService(AccountService accountService, JsonContactStorage storage) {
    this.storage = storage;
    this.accountService = accountService;
  }

  /**
   * @param customer who is logged-in
   * @param contact to be created
   * @return the contact given it passes the tests
   * @throws Exception if the Contact's details are incorrect:
   * if contact is null, contact name is blank/empty, contact Account is null, or the contact description is blank
   */
  public Contact createContact(User customer, Contact contact) throws Exception{
    List<String> invalidDataMessage = new ArrayList<>();

    if(contact == null){
      throw new NotFoundException("Contact could not be found");
    }

    if(contact.getName().isBlank() || contact.getName().isEmpty()){
      invalidDataMessage.add("Name of contact cannot be empty");
    }

    if(contact.getAccount() == null || !accountService.exists(contact.getAccount().getId())) {
      invalidDataMessage.add("Account could not be found");
    }

    if(!invalidDataMessage.isEmpty()){
      throw new BadRequestException(String.join("\n", invalidDataMessage));
    }
    
    contact.setOf(customer);
    contact.setAccount(accountService.getForContact(contact.getAccount().getId()));
    return storage.create(contact);
  }

  /**
   * @param customer who is logged in
   * @return the collection of Contacts for the logged-in Customer
   */
  public Collection<Contact> getContacts(User customer){
    return storage.getCustomerContacts(customer);
  }
}
