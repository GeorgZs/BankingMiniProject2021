package crushers.services.ducks.bankAndStaff;

import crushers.models.users.Clerk;
import crushers.storage.Storage;

import java.util.ArrayList;
import java.util.Collection;

public class StaffService {
    private final Storage<Clerk> storage;

    public StaffService(Storage<Clerk> storage){
        this.storage = storage;
    }

    public Clerk get(int id) throws Exception{
        Clerk clerk = storage.get(id);
        if(clerk == null){
            throw new Exception("No Staff member found with ID: " + id);
        }
        return clerk;
    }

    public Collection<Clerk> getAll() throws Exception {
        if(storage.getAll().isEmpty()){
            System.out.println("Empty Staff storage - creating employee 1");
            ArrayList<String> ge = new ArrayList<>();
            ge.add( "Mother's Maiden name");
            ge.add("Georg");
            ge.add("Pet cat's name");
            ge.add("Georg");
            ge.add("Highschool name");
            ge.add("Georg");
            storage.create(new Clerk(
                    "First",
                    "Last",
                    "Lindholmen 10",
                    "test@email.com",
                    "HelloWorld",
                    ge,
                    "Swedbank"));
        }
        return storage.getAll();
    }

    public Clerk create(Clerk clerk) throws Exception {
        if(clerk == null){
            throw new Exception("Staff Member invalid!");
        }
        return storage.create(clerk);
    }
}
