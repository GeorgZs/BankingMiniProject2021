package crushers.services.staff;

import crushers.common.models.*;
import crushers.storage.JsonStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonStaffStorage extends JsonStorage<User> {

    private final Map<Bank, Set<User>> bankClerks;

    public JsonStaffStorage(File jsonFile) throws IOException {
        super(jsonFile, User.class);
        this.bankClerks = new HashMap<>();

        for(User clerk : this.data.values()){
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
    public User create(User clerk) throws Exception {
        User clerkCreated = super.create(clerk);
        addToMap(clerkCreated);
        return clerkCreated;
    }

    /**
     * @param id of the Clerk that needs deleting
     * @return the deleted Clerk to see what was just deleted
     * @throws IOException
     */
    @Override
    public User delete(int id) throws IOException {
        User killedClerk = super.delete(id);
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
    public User update(int id, User clerk) throws IOException {
        User oldClerk = get(id);
        bankClerks.get(oldClerk.getWorksAt()).remove(oldClerk);
        
        User updatedClerk = super.update(id, clerk);
        bankClerks.get(updatedClerk.getWorksAt()).add(updatedClerk);
        return updatedClerk;
    }

    /**
     * @param bank at which the Manager works at
     * @return the Set of Clerks of the Bank provided
     */
    public Set<User> getClerksOfBank(Bank bank){
        return bankClerks.get(bank);
    }

    /**
     * @param clerk being added to the Map filled with Transaction procured from the JSON files
     * if the map doesn't contain the value with Clerk's Bank as Key,
     * it creates a new value in the map with Key being the Bank and the value being an empty HashSet
     */
    protected void addToMap(User clerk){
        if(!bankClerks.containsKey(clerk.getWorksAt())){
            bankClerks.put(clerk.getWorksAt(), new HashSet<>());
        }
        bankClerks.get(clerk.getWorksAt()).add(clerk);
    }
}
