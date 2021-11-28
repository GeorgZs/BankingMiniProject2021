package crushers.services.customers;

import crushers.models.Bank;
import crushers.models.users.Customer;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JsonCustomerStorage extends JsonStorage<Customer> {
    Collection<Customer> customers;

    public JsonCustomerStorage(File jsonFile) throws IOException {
        super(jsonFile, Customer.class);
        this.customers = new ArrayList<>();
        for(Customer customer : this.data.values()){
            addToMap(customer);
        }
    }

    protected void addToMap(Customer customer){
        if(!customers.contains(customer)){
            customers.add(customer);
        }

        customers.add(customer);
    }
}
