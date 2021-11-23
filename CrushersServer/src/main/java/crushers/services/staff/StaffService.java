package crushers.services.staff;

import crushers.models.Bank;
import crushers.models.users.Clerk;
import crushers.models.users.Manager;
import crushers.server.httpExceptions.*;
import crushers.storage.Storage;

import java.util.ArrayList;
import java.util.Collection;

public class StaffService {
    private final Storage<Clerk> storage;

    public StaffService(Storage<Clerk> storage){
        this.storage = storage;
    }

    public Clerk get(int id) throws Exception {
        Clerk clerk = storage.get(id);
        if(clerk == null){
            throw new NotFoundException("No Staff member found with ID: " + id);
        }
        return clerk;
    }

    public Collection<Clerk> getAll() throws Exception {
        if(storage.getAll().isEmpty()){
            System.out.println("Empty Staff storage - creating employee 1");
            String[] ge = new String[6];
            ge[0] = "Mother's Maiden name";
            ge[1] = "Georg";
            ge[2] = "Pet cat's name";
            ge[3] = "Georg";
            ge[4] = "Highschool name";
            ge[5] = "Georg";
            Bank bank = new Bank(new Manager("First",
                    "Last",
                    "Lindholmen 10",
                    "test@email.com",
                    "HelloWorld",
                    ge,
                    null));
            storage.create(new Clerk(
                    "First",
                    "Last",
                    "Lindholmen 10",
                    "test@email.com",
                    "HelloWorld",
                    ge,
                    bank));
        }
        return storage.getAll();
    }

    public Clerk create(Clerk clerk) throws Exception {
        if(clerk == null){
            throw new BadRequestException("Staff Member invalid!");
        }
        return storage.create(clerk);
    }
}
