package crushers.services.staff;

import crushers.models.Bank;
import crushers.models.users.Clerk;
import crushers.models.users.Manager;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonClerkStorage extends JsonStorage<Clerk> {

    private final Map<Bank, Set<Clerk>> bankClerks;

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

    /**
     * @param clerk to be created and added to the map of Clerks
     * @return the clerk created
     * if the bank at which the clerk works at doesn't exist in the map of BankClerks,
     * it creates a new value in the map with Key being the Bank at which the Clerk works at and a new hash set
     * @throws Exception
     */
    @Override
    public Clerk create(Clerk clerk) throws Exception {
        Clerk clerkCreated = super.create(clerk);
        if(!bankClerks.containsKey(clerkCreated.getWorksAt())){
            bankClerks.put(clerkCreated.getWorksAt(), new HashSet<>());
        }
        bankClerks.get(clerkCreated.getWorksAt()).add(clerkCreated);
        return clerkCreated;
    }

    /**
     * @param id of the Clerk that needs deleting
     * @return the deleted Clerk to see what was just deleted
     * @throws IOException
     */
    @Override
    public Clerk delete(int id) throws IOException {
        Clerk killedClerk = super.delete(id);
        if(killedClerk != null){
            bankClerks.get(killedClerk.getWorksAt()).remove(killedClerk);
        }
        return killedClerk;
    }

    /**
     * @param id of the Clerk that needs updating
     * @param clerk information that will update Clerk with provided ID
     * @return the updated Clerk
     * updates the bankClerks map with the new Clerk using the if-statement
     * @throws IOException
     */
    @Override
    public Clerk update(int id, Clerk clerk) throws IOException{
        Clerk updatedClerk = super.update(id, clerk);
        if(!updatedClerk.equals(get(id))){
            bankClerks.remove(get(id).getWorksAt()).remove(get(id));
            bankClerks.get(updatedClerk.getWorksAt()).add(updatedClerk);
        }
        return updatedClerk;
    }

    /**
     * @param bank at which the Manager works at
     * @return the Set of Clerks of the Bank provided
     */
    public Set<Clerk> getClerksOfBank(Bank bank){
        return bankClerks.get(bank);
    }

    /**
     * @param clerk being added to the Map filled with Transaction procured from the JSON files
     * if the map doesn't contain the value with Clerk's Bank as Key,
     * it creates a new value in the map with Key being the Bank and the value being an empty HashSet
     */
    protected void addToMap(Clerk clerk){
        if(!bankClerks.containsKey(clerk.getWorksAt())){
            bankClerks.put(clerk.getWorksAt(), new HashSet<>());
        }
        bankClerks.get(clerk.getWorksAt()).add(clerk);
    }
}
