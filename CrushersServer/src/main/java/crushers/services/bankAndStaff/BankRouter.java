package crushers.services.bankAndStaff;

import com.sun.net.httpserver.HttpExchange;
import crushers.models.Bank;
import crushers.server.Router;

import java.util.Collection;

public class BankRouter extends Router<Bank> {

    private BankService bankService;

    //this constructor means that when the BankRouter object
    //is created it sets the base path from the Router
    //super to /banks therefore setting it as an endpoint
    //for our server - routes us to the bank storage
    public BankRouter(BankService bankService) {
        super("/banks");
        this.bankService = bankService;
    }

    @Override
    protected void get(HttpExchange exchange, int id)throws Exception{
        final Bank responseData = bankService.get(id);
        sendJsonResponse(exchange, responseData);
    }

    @Override
    protected void getAll(HttpExchange exchange)throws Exception{
        final Collection<Bank> responseData = bankService.getAll();
        sendJsonResponse(exchange, responseData);
    }

    @Override
    protected void post(HttpExchange exchange)throws Exception{
        final Bank requestData = getJsonBodyData(exchange, Bank.class);
        final Bank responseData = bankService.create(requestData);
        sendJsonResponse(exchange, responseData);
    }
}
