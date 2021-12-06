package crushers.services.staff;

import crushers.models.Bank;
import crushers.models.users.Clerk;

import crushers.server.Authenticator;
import crushers.server.httpExceptions.*;
import crushers.services.accounts.JsonAccountStorage;
import crushers.utils.Security;

import java.util.*;

public class StaffService {
    private final JsonClerkStorage storage;
    private final JsonAccountStorage accountStorage;
    private final Security security = new Security();

    public StaffService(JsonClerkStorage storage, JsonAccountStorage accountStorage) throws Exception {
        this.storage = storage;
        this.accountStorage = accountStorage;

        for (Clerk clerk : storage.getAll()) {
            Authenticator.instance.register(clerk);
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

    public Clerk updateClerk(int id, Clerk clerk) throws Exception {
        storage.update(id, clerk);
        return get(id);
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
        if(clerk.getPassword() != null && !clerk.getPassword().matches(".*[A-Z].*")){
            invalidDataMessage.add("Password must contain at least 1 Captial Letter");}
        if (clerk.getPassword() != null && !clerk.getPassword().matches(".*[0-9].*")){
            invalidDataMessage.add("Password must contain at least 1 digit (0-9)");}
        if(clerk.getPassword() != null && clerk.getPassword().contains(" ")){
            invalidDataMessage.add("Password cannot contain an empty character");}

        if (!invalidDataMessage.isEmpty()) {
            throw new BadRequestException(String.join("\n", invalidDataMessage));
        }


        //for testing out commented
        //clerk.setPassword(security.passwordEncryption(clerk.getPassword(), "MD5"));


        clerk.setWorksAt(bank);
        Authenticator.instance.register(clerk);
        return storage.create(clerk);
    }

    public Collection<String> getAllEmail() throws Exception{
        Collection<String> emailList = new ArrayList<>();
        for(Clerk clerk : storage.getAll()){
            emailList.add(clerk.getEmail());
        }
        return emailList;
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

    public JsonAccountStorage getAccountStorage() {
        return accountStorage;
    }
}
