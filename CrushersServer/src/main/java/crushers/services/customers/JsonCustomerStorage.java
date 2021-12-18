package crushers.services.customers;

import crushers.models.Bank;
import crushers.models.exchangeInformation.Contact;
import crushers.models.users.Customer;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonCustomerStorage extends JsonStorage<Customer> {
    private static int idConst = 1001;

    private final Map<Customer, Collection<Contact>> contactList;

    public JsonCustomerStorage(File jsonFile) throws IOException {
        super(jsonFile, Customer.class);
        this.contactList = new HashMap<>();

        for(Customer customer : this.data.values()){
            //addToMaps(customer);
            //needs implementing so that notifications show correctly
        }
    }


    protected Contact createContact(Customer customer, Contact contact) throws Exception{
        contact.setID(idConst);
        idConst++;
        /*
        if (!contactList.containsKey(customer)) {
            contactList.put(customer, new ArrayList<>());
        }
        contactList.get(customer).add(contact);
         */
        addToMaps(customer, contact);
        return contact;
    }

    protected Collection<Contact> getContacts(Customer customer){
        return contactList.get(customer);
    }

    protected void addToMaps(Customer customer, Contact contact) {
        if (!contactList.containsKey(customer)) {
            contactList.put(customer, new ArrayList<>());
        }
        contactList.get(customer).add(contact);
    }
}
