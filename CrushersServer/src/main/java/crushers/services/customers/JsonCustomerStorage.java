package crushers.services.customers;

import crushers.models.users.Customer;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;

public class JsonCustomerStorage extends JsonStorage<Customer> {
    public JsonCustomerStorage(File jsonFile) throws IOException {
        super(jsonFile, Customer.class);
    }
}
