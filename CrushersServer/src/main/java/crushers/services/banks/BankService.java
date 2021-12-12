package crushers.services.banks;


import crushers.models.Bank;
import crushers.models.accounts.Account;
import crushers.models.accounts.SavingsAccount;
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

    public Bank get(int id) throws Exception {
        Bank bank = storage.get(id);
        if(bank == null) {
            throw new NotFoundException("No Bank found with ID: " + id);
        }
        return bank;
    }

    public Collection<Bank> getAll() throws Exception {
        return storage.getAll();
    }

    public Bank create(Bank bank) throws Exception {
        if(bank == null){
            throw new BadRequestException("User invalid!");
        }

        Bank createdBank = storage.create(bank);
        staffService.create(createdBank, bank.getManager());
        return createdBank;
    }

    public Bank updateBank(int id, Bank bank) throws Exception {
        storage.update(id, bank);
        return bank;
    }

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
