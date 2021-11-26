package crushers.services.staff;

import crushers.models.Bank;
import crushers.models.users.Clerk;
import crushers.models.users.Manager;
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
            if (clerk.getStaffType().equals("manager")) {
                System.out.println("Manager: " + clerk.getId());

                // turn managers into managers again
                Manager manager = new Manager(
                    clerk.getEmail(),
                    clerk.getFirstName(), 
                    clerk.getLastName(), 
                    clerk.getAddress(), 
                    clerk.getPassword(), 
                    clerk.getSecurityQuestions(), 
                    clerk.getWorksAt()
                );
                    
                manager.setId(clerk.getId());
                update(clerk.getId(), manager);
            }
            
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

    @Override
    public Clerk update(int id, Clerk clerk) throws IOException{
        Clerk updatedClerk = super.update(id, clerk);
        if(!updatedClerk.equals(get(id))){
            bankClerks.remove(get(id).getWorksAt()).remove(get(id));
            bankClerks.get(updatedClerk.getWorksAt()).add(updatedClerk);
        }
        return updatedClerk;
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
