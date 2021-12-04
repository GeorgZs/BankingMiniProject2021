package crushers.services.customers;

import crushers.models.exchangeInformation.Contact;
import crushers.models.exchangeInformation.Transaction;
import crushers.models.users.Customer;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class JsonCustomerStorage extends JsonStorage<Customer> {

    private final Map<Customer, Collection<Contact>> contactList;

    public JsonCustomerStorage(File jsonFile) throws IOException {
        super(jsonFile, Customer.class);
        this.contactList = new HashMap<>();

        for(Customer customer : this.data.values()){
            //addToMaps(customer);
        }
    }


    protected Contact createContact(Customer customer, Contact contact) throws Exception{
        contactList.get(customer).add(contact);
        return contact;
    }

    protected Collection<Contact> getContacts(Customer customer){
        return contactList.get(customer);
    }
    /*
    protected void addToMaps(Customer customer) {
        if (!contactList.containsKey(customer)) {
            contactList.put(customer, new HashSet<>());
        }
        contactList.get(customer).add(transaction);
    }
     */
}
