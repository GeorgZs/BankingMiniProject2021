package crushers.services.staff;

import crushers.models.Bank;
import crushers.models.users.Clerk;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonClerkStorage extends JsonStorage<Clerk> {

    private final Map<Bank, Collection<Clerk>> bankClerks;

    public JsonClerkStorage(File jsonFile) throws IOException {
        super(jsonFile, Clerk.class);
        this.bankClerks = new HashMap<>();

        for(Clerk clerk : this.data.values()){
            addToMap(clerk);
        }
    }

    @Override
    public Clerk create(Clerk clerk) throws Exception {
        Clerk clerkCreated = super.create(clerk);
        if(!bankClerks.containsKey(clerkCreated.getWorksAt())){
            bankClerks.put(clerkCreated.getWorksAt(), new ArrayList<>());
        }
        bankClerks.get(clerkCreated.getWorksAt()).add(clerkCreated);
        return clerkCreated;
    }

    @Override
    public Clerk delete(int id) throws IOException {
        Clerk killedClerk = super.delete(id);
        if(killedClerk != null){
            bankClerks.get(killedClerk.getWorksAt()).remove(killedClerk);
        }
        return killedClerk;
    }

    public Collection<Clerk> getClerksOfBank(Bank bank){
        return bankClerks.get(bank);
    }

    protected void addToMap(Clerk clerk){
        if(!bankClerks.containsKey(clerk.getWorksAt())){
            bankClerks.put(clerk.getWorksAt(), new ArrayList<>());
        }

        bankClerks.get(clerk.getWorksAt()).add(clerk);
    }
}
