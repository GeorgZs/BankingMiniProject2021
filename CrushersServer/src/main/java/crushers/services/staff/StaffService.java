package crushers.services.staff;

import crushers.common.httpExceptions.*;
import crushers.common.models.*;

import crushers.server.Authenticator;
import crushers.services.banks.BankService;

import java.util.*;

public class StaffService {
    private final JsonStaffStorage storage;
    private final BankService bankService;

    public StaffService(BankService bankService, JsonStaffStorage storage) throws Exception {
        this.storage = storage;
        this.bankService = bankService;

        for (User clerk : storage.getAll()) {
            Authenticator.instance.register(clerk);
        }
    }

    /**
     * @param id of the requested Clerk
     * @return the Clerk with the ID matching that passed in the method signature
     * @throws Exception if the clerk is null or the ID doesn't match any Clerk's ID
     */
    public User get(int id) throws Exception {
        User clerk = storage.get(id);
        if(clerk == null){
            throw new NotFoundException("No Staff member found with ID: " + id);
        }
        return clerk;
    }

    /**
     * @return the Collection of Clerks
     * @throws Exception
     */
    public Collection<User> getAll() throws Exception {
        return storage.getAll();
    }

    /**
     * @param id of the Clerk being updated
     * @param clerk data that will update the clerk with ID passed in method signature
     * @return the updated Clerk
     * @throws Exception
     */
    public User updateClerk(int id, User clerk) throws Exception {
        storage.update(id, clerk);
        return get(id);
    }

    /**
     * Validates a clerk
     * @param clerk being validated
     * @throws Exception if the Clerk doesn't meet requirements:
     * if the clerk is null or the Clerk's details are invalid:
     * if the email, first and last name, and password are null or blank it throws and Exception
     * for password validation:
     * if password length is less than 8, doesn't contain at least one Capital letter or number, or
     * contains an empty characters --> throws an Exception
     */
    public void validate(User clerk) throws BadRequestException {
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
    public User create(Bank bank, User clerk) throws Exception {
        validate(clerk);
        bank = bankService.get(bank.getId());
        clerk.setWorksAt(bank);

        if (bank.getManager().getId() > 0 || !clerk.isManager()) {
            clerk.setType("clerk");
        }

        User createdClerk = storage.create(clerk);

        try {
            Authenticator.instance.register(createdClerk);
        }
        catch (Exception ex) {
            storage.delete(createdClerk.getId());
        }
        
        return createdClerk;
    }

    /**
     * @param loggedInClerk
     * @return the Clerk object of the logged-in Clerk
     * @throws Exception
     */
    public User getLoggedIn(User loggedInClerk) throws Exception {
        return storage.get(loggedInClerk.getId());
    }

    /**
     * @param bank at which the logged-in Manager works
     * @return the Collection of Clerks that work at the Bank at which the logged-in Manager works at
     * @throws Exception
     */
    public Collection<User> getClerksOfBank(Bank bank) throws Exception {
        Collection<User> clerks = storage.getClerksOfBank(bank);
        if(clerks == null){
            clerks = new ArrayList<>();
        }
        return clerks;
    }
}
