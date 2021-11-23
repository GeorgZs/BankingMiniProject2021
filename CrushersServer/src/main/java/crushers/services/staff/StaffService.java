package crushers.services.staff;

import crushers.models.Bank;
import crushers.models.users.Clerk;
import crushers.models.users.Manager;
import crushers.server.httpExceptions.*;
import crushers.storage.Storage;

import java.util.Collection;

public class StaffService {
    private final Storage<Clerk> storage;

    public StaffService(Storage<Clerk> storage){
        this.storage = storage;
    }

    //this method gets the staff member by their unique
    //id that is generated upon their creation
    public Clerk get(int id) throws Exception {
        Clerk clerk = storage.get(id);
        if(clerk == null){
            throw new NotFoundException("No Staff member found with ID: " + id);
        }
        return clerk;
    }

    //when http://localhost:8080/staff is first called it uses the function
    //below to get all staff members which, if there are no staff members, creates a new
    //template staff member
    public Collection<Clerk> getAll() throws Exception {
        if(storage.getAll().isEmpty()){
            System.out.println("Empty Staff storage - creating random employee");
            String[] securityQandA = new String[6];
            securityQandA[0] = "Mother's Maiden name";
            securityQandA[1] = "Georg";
            securityQandA[2] = "Pet cat's name";
            securityQandA[3] = "Georg";
            securityQandA[4] = "Highschool name";
            securityQandA[5] = "Georg";
            Bank bank = new Bank(new Manager("First",
                    "Last",
                    "Lindholmen 10",
                    "test@email.com",
                    "HelloWorld",
                    securityQandA,
                    null));
            storage.create(new Clerk(
                    "First",
                    "Last",
                    "Lindholmen 10",
                    "test@email.com",
                    "HelloWorld",
                    securityQandA,
                    bank));
        }
        return storage.getAll();
    }

    //called to create a clerk and this returns the clerk
    //created and adds them to the storage
    public Clerk create(Clerk clerk) throws Exception {
        if(clerk == null){
            throw new BadRequestException("Staff Member invalid!");
        }
        return storage.create(clerk);
    }
}
