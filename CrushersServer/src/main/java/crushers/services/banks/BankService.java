package crushers.services.banks;

import crushers.models.Bank;
import crushers.models.accounts.Account;
import crushers.models.exchangeInformation.BankDetails;
import crushers.models.exchangeInformation.InterestRate;
import crushers.models.users.Clerk;
import crushers.models.users.Manager;
import crushers.server.httpExceptions.*;
import crushers.services.staff.StaffService;

import java.util.*;

public class BankService {
    private final JsonBankStorage storage;
    private final StaffService staffService;

    public BankService(JsonBankStorage storage, StaffService staffService) {
        this.storage = storage;
        this.staffService = staffService;
    }

    /**
     * @param id of the Bank
     * @return the Bank with the specified ID
     * @throws Exception if the ID does not match any Bank's ID
     */
    public Bank get(int id) throws Exception {
        Bank bank = storage.get(id);
        if(bank == null) {
            throw new NotFoundException("No Bank found with ID: " + id);
        }
        return bank;
    }

    /**
     * @return the Collection of Banks in the Bank Storage
     * @throws Exception
     */
    public Collection<Bank> getAll() throws Exception {
        return storage.getAll();
    }

    /**
     * @param bank to be created
     * @return the Bank that
     * @throws Exception
     */
    public Bank create(Bank bank) throws Exception {
        if(bank == null){
            throw new BadRequestException("User invalid!");
        }
        Bank createdBank = storage.create(bank);
        staffService.create(createdBank, bank.getManager());
        return createdBank;
    }

    /**
     * @param id of the bank
     * @param bank with updated information
     * @return the updated bank, given there were no exceptions
     * @throws Exception
     */
    public Bank updateBank(int id, Bank bank) throws Exception {
        storage.update(id, bank);
        return bank;
    }

    /**
     * @param manager who is logged-in
     * @param newInterestRate in the form of the InterestRate class
     * @return the new Interest Rate in the form of a double
     * @throws Exception if the interest Rate is the same as the already existing Interest Rate number
     */
    public double changeInterestRate(Manager manager, InterestRate newInterestRate) throws Exception {
        Collection<Account> accounts = staffService.getAccountStorage().getAccountsOfBank(manager.getWorksAt());
        for (Account account : accounts) {
            if(newInterestRate.getRate() == account.getInterestRate()){
                throw new BadRequestException("Bank Interest Rate already equals input number");
            }
            if (account.getInterestRate() != 0.00) {
                account.setInterestRate(newInterestRate.getRate());
                staffService.getAccountStorage().update(account.getId(), account);
            }
        }
        return newInterestRate.getRate();
    }

    /**
     * @param manager logged-in
     * @param updatedInformation in the form of the Bank Details class
     * @example
     *  {
     *      "logo": "logo.png",
     *      "details": "This is a test bank",
     *      "name": "TestBank"
     *  }
     * @return the updated BankDetails
     * @throws Exception
     */
    public BankDetails editBank(Manager manager, BankDetails updatedInformation) throws Exception {
        Bank bank = new Bank(manager);
        bank.setId(manager.getWorksAt().getId());
        bank.setName(updatedInformation.getName());
        bank.setLogo(updatedInformation.getLogo());
        bank.setDetails(updatedInformation.getDetails());
        manager.setWorksAt(bank);
        staffService.getStorage().update(manager.getId(), manager);
        for(Clerk clerk : staffService.getClerksOfBank(bank)){
            clerk.setWorksAt(bank);
            staffService.getStorage().update(clerk.getId(), clerk);
        }
        storage.update(manager.getWorksAt().getId(), bank);
        return updatedInformation;
    }
}
