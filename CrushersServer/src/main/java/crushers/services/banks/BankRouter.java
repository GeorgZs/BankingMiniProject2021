package crushers.services.banks;

import com.sun.net.httpserver.HttpExchange;


import crushers.common.models.*;
import crushers.server.Router;
import crushers.services.staff.StaffService;

import java.util.Collection;

public class BankRouter extends Router {

    private BankService bankService;
    private final StaffService staffService;

    //this constructor means that when the BankRouter object
    //is created it sets the base path from the Router
    //super to /banks therefore setting it as an endpoint
    //for our server - routes us to the bank storage
    public BankRouter(BankService bankService, StaffService staffService) {
        super("/banks");
        this.bankService = bankService;
        this.staffService = staffService;
    }

    /**
     * @param exchange
     * @param id of the specified Bank
     * @return the Bank with the specified ID, displaying all of its information
     * @throws Exception
     */
    @Override
    protected void get(HttpExchange exchange, int id) throws Exception {
        final Bank responseData = bankService.get(id);
        sendJsonResponse(exchange, responseData, Bank.class);
    }

    /**
     * @param exchange
     * @return the Collection of all the Banks
     * @throws Exception
     */
    @Override
    protected void getAll(HttpExchange exchange) throws Exception {
        final Collection<Bank> responseData = bankService.getAll();
        sendJsonCollectionResponse(exchange, responseData, Bank.class);
    }

    /**
     * @param exchange
     * creates a Bank with the information specified in the HTTP body
     * @throws Exception
     */
    @Override
    protected void post(HttpExchange exchange) throws Exception {
        final Bank bank = getJsonBodyData(exchange, Bank.class);
        staffService.validate(bank.getManager());
        final Bank createdBank = bankService.create(bank);

        User manager = bank.getManager();
        manager.setType("manager");
        User createdManager = staffService.create(createdBank, manager);

        bank.setManager(createdManager);
        bankService.updateBank(bank.getId(), bank);
        sendJsonResponse(exchange, createdBank, Bank.class);
    }

    /**
     * @param exchange
     * @param id of the Bank that needs updating
     * @return the updated Bank - HTTP body is the entire bank object
     * @throws Exception
     */
    @Override
    protected void put(HttpExchange exchange, int id) throws Exception {
        final Bank requestData = getJsonBodyData(exchange, Bank.class);
        final Bank responseData = bankService.updateBank(id, requestData);
        sendJsonResponse(exchange, responseData, Bank.class);
    }
}
