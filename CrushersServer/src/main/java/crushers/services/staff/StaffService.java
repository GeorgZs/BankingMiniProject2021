package crushers.services.staff;

import crushers.models.Bank;
import crushers.models.users.Clerk;
import crushers.models.users.Manager;

import crushers.server.Authenticator;
import crushers.server.httpExceptions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StaffService {
    private final JsonClerkStorage storage;

    public StaffService(JsonClerkStorage storage) throws Exception {
        this.storage = storage;

        for (Clerk clerk : storage.getAll()) {
            if (clerk.getStaffType().equals("clerk")) {
                Authenticator.instance.register(clerk);
            }
            else {
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

                storage.update(clerk.getId(), manager);
                Authenticator.instance.register(manager);
            }
        }
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
        return storage.getAll();
    }

    //called to create a clerk and this returns the clerk
    //created and adds them to the storage
    public Clerk create(Bank bank, Clerk clerk) throws Exception {
        List<String> invalidDataMessage = new ArrayList<>();
        if(clerk == null){
            throw new BadRequestException("Staff Member invalid!");
        }

        //general validation of details
        if(clerk.getEmail().isBlank() || clerk.getEmail() == null){
            invalidDataMessage.add("Email cannot be blank");}
        if(clerk.getFirstName().isBlank() || clerk.getFirstName() == null){
            invalidDataMessage.add("First Name cannot be blank");}
        if(clerk.getLastName().isBlank() || clerk.getLastName() == null){
            invalidDataMessage.add("Last Name cannot be blank");}
        if(clerk.getPassword().isBlank() || clerk.getPassword() == null){
            invalidDataMessage.add("Password cannot be blank");}

        //password validation - not allow creating of simple passwords
        if(clerk.getPassword() != null && clerk.getPassword().length() < 8){
            invalidDataMessage.add("Password must be longer that 8 characters");}
        if(clerk.getPassword() != null && clerk.getPassword().matches(".*[A-Z].*")){
            invalidDataMessage.add("Password must contain at least 1 Captial Letter");}
        if(clerk.getPassword() != null && clerk.getPassword().matches(".*[0-9].*")){
            invalidDataMessage.add("Password must contain at least 1 digit (0-9)");}
        if(clerk.getPassword() != null && clerk.getPassword().contains(" ")){
            invalidDataMessage.add("Password cannot contain an empty character");}

        clerk.setWorksAt(bank);
        Authenticator.instance.register(clerk);
        return storage.create(clerk);
    }

    public Clerk getLoggedIn(Clerk loggedInClerk) throws Exception {
        return storage.get(loggedInClerk.getId());
    }

    public Collection<Clerk> getClerksOfBank(Bank bank) throws Exception{
        Collection<Clerk> clerks = storage.getClerksOfBank(bank);
        if(clerks == null){
            clerks = new ArrayList<>();
        }
        return clerks;
    }
}
