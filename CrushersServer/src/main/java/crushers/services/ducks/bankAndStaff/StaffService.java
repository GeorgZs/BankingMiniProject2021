package crushers.services.ducks.bankAndStaff;

import crushers.models.users.Clerk;
import crushers.storage.Storage;

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
            throw new Exception("Empty Staff storage");
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
