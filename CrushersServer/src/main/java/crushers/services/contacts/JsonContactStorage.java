package crushers.services.contacts;

import java.io.File;
import java.io.IOException;
import java.util.*;

import crushers.common.models.*;
import crushers.storage.JsonStorage;

public class JsonContactStorage extends JsonStorage<Contact> {

  private final Map<User, Set<Contact>> customerContacts;

  public JsonContactStorage(File jsonFile) throws IOException {
    super(jsonFile, Contact.class);
    this.customerContacts = new HashMap<>();

    for (Contact contact : this.data.values()) {
      addToMaps(contact);
    }
  }

  /**
   * @param newcontact to be added to the Contact Storage
   * @return the Contact that was successfully added to the Storage
   * @throws Exception if the Contact creation was in any way unsuccessful
   */
  @Override
  public Contact create(Contact newcontact) throws Exception {
    Contact createdContact = super.create(newcontact);
    addToMaps(createdContact);
    return createdContact;
  }

  /**
   * @param id of the specified contact
   * @return the contact that was deleted just as a precaution to see what contact was deleted
   * @throws IOException if contact deletion was in any way unsuccessful
   */
  @Override
  public Contact delete(int id) throws IOException {
    Contact deletedContact = super.delete(id);

    if (deletedContact != null) {
      customerContacts.get(deletedContact.getOf()).remove(deletedContact);
    }

    return deletedContact;
  }

  /**
   * @param user the logged-in customer
   * @return the Set of contacts of the logged-in customer
   */
  public Set<Contact> getCustomerContacts(User user) {
    return customerContacts.get(user) == null ? new HashSet<>() : customerContacts.get(user);
  }

  /**
   * @param contact added to the Local Map of Contacts which are retrieved from the JSON-files
   * adds the Contact specified in the method signature to each of the maps
   *
   * checks whether each Map key has a set associated to it, if not it creates a new empty set for the Key
   */
  protected void addToMaps(Contact contact) {
    if (!customerContacts.containsKey(contact.getOf())) {
      customerContacts.put(contact.getOf(), new HashSet<>());
    }

    customerContacts.get(contact.getOf()).add(contact);
  }
}
