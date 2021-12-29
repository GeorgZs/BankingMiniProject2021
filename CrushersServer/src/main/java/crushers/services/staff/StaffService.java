package crushers.services.staff;

import crushers.models.Bank;
import crushers.models.users.Clerk;

import crushers.server.Authenticator;
import crushers.server.httpExceptions.*;
import crushers.services.accounts.JsonAccountStorage;

import java.util.*;

public class StaffService {
    private final JsonClerkStorage storage;
    private final JsonAccountStorage accountStorage;

    public StaffService(JsonClerkStorage storage, JsonAccountStorage accountStorage) throws Exception {
        this.storage = storage;
        this.accountStorage = accountStorage;

        for (Clerk clerk : storage.getAll()) {
            Authenticator.instance.register(clerk);
        }
    }

    /**
     * @param id of the requested Clerk
     * @return the Clerk with the ID matching that passed in the method signature
     * @throws Exception if the clerk is null or the ID doesn't match any Clerk's ID
     */
    public Clerk get(int id) throws Exception {
        Clerk clerk = storage.get(id);
        if(clerk == null){
            throw new NotFoundException("No Staff member found with ID: " + id);
        }
        return clerk;
    }

    /**
     * @return the Collection of Clerks
     * @throws Exception
     */
    public Collection<Clerk> getAll() throws Exception {
        return storage.getAll();
    }

    /**
     * @param id of the Clerk being updated
     * @param clerk data that will update the clerk with ID passed in method signature
     * @return the updated Clerk
     * @throws Exception
     */
    public Clerk updateClerk(int id, Clerk clerk) throws Exception {
        storage.update(id, clerk);
        return get(id);
    }

    /**
     * @param bank in which the logged-in Manager works
     * @param clerk being created
     * @return the created Clerk given it passes all tests
     * @throws Exception if the Clerk doesn't meet requirements:
     * if the clerk is null or the Clerk's details are invalid:
     * if the email, first and last name, and password are null or blank it throws and Exception
     * for password validation:
     * if password length is less than 8, doesn't contain at least one Capital letter or number, or
     * contains an empty characters --> throws an Exception
     */
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
            throw new BadRequestException(String.join("\\n", invalidDataMessage));
        }
        clerk.setWorksAt(bank);
        Authenticator.instance.register(clerk);
        return storage.create(clerk);
    }

    /**
     * @param loggedInClerk
     * @return the Clerk object of the logged-in Clerk
     * @throws Exception
     */
    public Clerk getLoggedIn(Clerk loggedInClerk) throws Exception {
        return storage.get(loggedInClerk.getId());
    }

    /**
     * @param bank at which the logged-in Manager works
     * @return the Collection of Clerks that work at the Bank at which the logged-in Manager works at
     * @throws Exception
     */
    public Collection<Clerk> getClerksOfBank(Bank bank) throws Exception{
        Collection<Clerk> clerks = storage.getClerksOfBank(bank);
        if(clerks == null){
            clerks = new ArrayList<>();
        }
        return clerks;
    }

    /**
     * @return the JsonAccountStorage
     */
    public JsonAccountStorage getAccountStorage() {
        return accountStorage;
    }

    /**
     * @return the JsonClerkStorage
     */
    public JsonClerkStorage getStorage() {
        return storage;
    }
}
